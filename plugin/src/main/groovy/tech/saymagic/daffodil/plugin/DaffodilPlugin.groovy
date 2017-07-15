package tech.saymagic.daffodil.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.saymagic.daffodil.plugin.transform.DaffodilTransform

public class DaffodilPlugin implements Plugin<Project> {

    AppExtension androidExtension

    LibraryExtension libraryExtension

    DaffodilExtension daffodilExtension

    DaffodilTransform daffodilTransform

    void apply(Project project) {
        project.extensions.create(Constants.EXTENSION_NAME, DaffodilExtension)
        daffodilExtension = project.extensions.findByType(DaffodilExtension.class)
        androidExtension = project.extensions.findByType(AppExtension.class)
        libraryExtension = project.extensions.findByType(LibraryExtension.class)

        daffodilTransform = new DaffodilTransform()
        if (androidExtension) {
            println "register daffodilTransform for android project " + project.name
            androidExtension.registerTransform(daffodilTransform)
        }else if (libraryExtension) {
            println "register daffodilTransform for library project " + project.name
            libraryExtension.registerTransform(daffodilTransform)
        } else {
            throw new IllegalStateException("'android' or 'android-library' plugin does't exist!")
        }

        project.afterEvaluate {
            boolean enable = daffodilExtension == null ? true : daffodilExtension.enabled
            println "daffodilExtension.enabled = " + enable
            if(!enable){
                println "daffodil extension was disabled."
                daffodilTransform.enabled = false
            }else{
                println "start daffodil transform"
                daffodilTransform.enabled = true
            }
        }

    }
}