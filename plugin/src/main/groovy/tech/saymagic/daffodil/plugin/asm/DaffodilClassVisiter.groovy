package tech.saymagic.daffodil.plugin.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes


/**
 * Created by caoyanming on 2017/6/11.
 */

public class DaffodilClassVisiter extends ClassVisitor{

    int api
    String className
    String superName
    boolean  ignore

    DaffodilClassVisiter(int i) {
        super(i)
        this.api = api
    }

    DaffodilClassVisiter(int i, ClassVisitor classVisitor) {
        super(i, classVisitor)
        this.api = i
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name
        this.superName = superName
        println String.format("className = %s, superName = %s, signature = %s", name, superName, signature)
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            // interface method has no code
            log.debug "ignored class ${className}, because it is interface"
            ignore = true;
        }
        super.visit(version, access, name, signature, superName, interfaces)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        if (!ignore) {
            mv = new DaffodilMethodVisiter(api, mv, className, superName, name, access, desc, exceptions);
        }
        return mv
    }
}
