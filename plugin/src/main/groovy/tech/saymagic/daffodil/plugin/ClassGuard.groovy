package tech.saymagic.daffodil.plugin


import java.util.regex.Pattern;
/**
 * Created by caoyanming on 2017/6/11.
 */

public class ClassGuard {

    private static final Pattern R_PATTERN = Pattern.compile("R[\$]?[^.]*+\\.class");

    static boolean shouldTransform(String className){

        if (null == className || className.isEmpty())
            return false

        if (className.startsWith("android") || R_PATTERN.matcher(className).matches()) {
            return false
        }

        if (className.contains("/R\$") || className.endsWith("/BuildConfig.class") || className.endsWith("/R.class") ) {
            return false
        }

        return true
    }
}
