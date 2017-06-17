package tech.saymagic.daffodil.lib;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by caoyanming on 2017/6/10.
 */

public class MethodRemember {



    private static ThreadLocal<Map<String, MethodInfo>> mMethodInfoContainter = new ThreadLocal<>();

    public static void onMethodEnter(Object[] args) {
        Map<String, MethodInfo> methodInfoMap = mMethodInfoContainter.get();
        if (methodInfoMap == null) {
            methodInfoMap = new HashMap<String, MethodInfo>();
            mMethodInfoContainter.set(methodInfoMap);
        }
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        if (stackTraceElement != null && stackTraceElement.length > 3) {
            StackTraceElement traceElement = stackTraceElement[3];
            if (traceElement != null) {
                MethodInfo method = new MethodInfo(args, traceElement);
                methodInfoMap.put(hashElement(traceElement), method);
            } else {
                DaffodilPrinter.log("traceElement is null");
            }
        } else {
            DaffodilPrinter.log("stackTraceElement is illegal");
        }
    }

    public static void onMethodExit(Object ret) {
        Map<String, MethodInfo> methodInfoMap = mMethodInfoContainter.get();
        if (methodInfoMap == null) {
            DaffodilPrinter.log("no  methodInfoMap for thread " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
            return;
        }
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();
        if (stackTraceElement != null && stackTraceElement.length > 3) {
            StackTraceElement traceElement = stackTraceElement[3];
            String hash = hashElement(traceElement);
            if (methodInfoMap.containsKey(hash)) {
                MethodInfo method = methodInfoMap.get(hash);
                method.setReturn(ret);
                method.setThreadId(Thread.currentThread().getId());
                method.setThreadName(Thread.currentThread().getName());
                methodInfoMap.remove(hash);
                DaffodilPrinter.printMethod(method);
            } else {
                DaffodilPrinter.log("traceElement is null");
            }
        } else {
            DaffodilPrinter.log("stackTraceElement is illegal");
        }
    }

    private static String hashElement(StackTraceElement element) {
        if (element == null) {
            return "";
        }
        return String.valueOf(element.getClassName()) + "-" + String.valueOf(element.getMethodName());
    }

}
