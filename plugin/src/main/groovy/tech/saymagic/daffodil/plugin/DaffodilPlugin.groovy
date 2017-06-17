package tech.saymagic.daffodil.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import tech.saymagic.daffodil.plugin.transform.DaffodilTransform

public class DaffodilPlugin implements Plugin<Project> {

    AppPlugin androidPlugin

    AppExtension androidExtension

    DaffodilExtension daffodilExtension

    DaffodilTransform daffodilTransform

    void apply(Project project) {
        project.extensions.create(Constants.EXTENSION_NAME, DaffodilExtension)
        androidPlugin = project.plugins.findPlugin(AppPlugin.class)
        androidExtension = project.android
        daffodilExtension = project.daffodil
        daffodilTransform = new DaffodilTransform()
        androidExtension.registerTransform(daffodilTransform)

        project.afterEvaluate {
            println "daffodilExtension.enabled = " + daffodilExtension.enabled
            if(daffodilExtension && !daffodilExtension.enabled){
                println "daffodil extension was disabled."
                daffodilTransform.enabled = false
            }else{
                println "start daffodil transform"
                daffodilTransform.enabled = true
            }
        }

    }
}