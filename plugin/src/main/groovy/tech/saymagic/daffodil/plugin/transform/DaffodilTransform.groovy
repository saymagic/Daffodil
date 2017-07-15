package tech.saymagic.daffodil.plugin.transform

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.android.build.api.transform.Format
import com.google.common.collect.Sets
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import tech.saymagic.daffodil.plugin.ClassGuard
import tech.saymagic.daffodil.plugin.asm.DaffodilClassVisiter

import java.security.MessageDigest;


/**
 * Created by caoyanming on 2017/5/19.
 */
public class DaffodilTransform extends Transform {

    boolean enabled;

    @Override
    String getName() {
        return "DaffodilTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
        )
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
        inputs.each { TransformInput input ->
            input.directoryInputs.each { DirectoryInput directoryInput ->
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                if(dest.exists()){
                    dest.delete()
                }
                dest.mkdirs()
                println "Copying ${directoryInput.name} to ${dest.absolutePath}"
                FileUtils.copyDirectory(directoryInput.file, dest)
                if (enabled) {
                    transformFolder(dest)
                }
            }

            //ignore third part libs
            input.jarInputs.each { JarInput jarInput ->

                def jarName = jarInput.name
                def md5Name = generateMD5(jarInput.file.getAbsolutePath())
                if (jarName.endsWith(".jar")) {
                    jarName = jarName.substring(0, jarName.length() - 4)
                }

                def dest = outputProvider.getContentLocation(jarName + md5Name,
                        jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println "Copying Jar ${jarInput.file.absolutePath} to ${dest.absolutePath}"
                if(dest.exists()){
                    dest.delete()
                }
                dest.getParentFile().mkdirs()
                dest.createNewFile()
                FileUtils.copyFile(jarInput.file, dest)

            }
        }
    }

    def generateMD5(String s) {
        MessageDigest digest = MessageDigest.getInstance("MD5")
        digest.update(s.bytes);
        new BigInteger(1, digest.digest()).toString(16).padLeft(32, '0')
    }

    def transformFolder(File dest) {
        dest.eachFile {File file ->
            if (file.absolutePath.endsWith(".class")) {
                println "Handle class " + file.absolutePath
                if(ClassGuard.shouldTransform(file.absolutePath)){
                    File clone = new File(file.getParentFile(), file.getName() + ".clone")
                    FileInputStream fileInputStream = new FileInputStream(file)
                    clone.createNewFile()
                    ClassReader cr = new ClassReader(fileInputStream)
                    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
                    DaffodilClassVisiter cv = new DaffodilClassVisiter(Opcodes.ASM5, cw);
                    cr.accept(cv, 0)
                    byte[] classBytes = cw.toByteArray()
                    FileOutputStream outputStream = new FileOutputStream(clone)
                    outputStream.write(classBytes)
                    if (file.exists()) {
                        file.delete()
                    }
                    clone.renameTo(file)
                    org.apache.commons.io.IOUtils.closeQuietly(fileInputStream)
                    org.apache.commons.io.IOUtils.closeQuietly(outputStream)
                }

            }else if (file.isDirectory()) {
                transformFolder(file)
            }
        }
    }

}
