package tech.saymagic.daffodil.lib;


import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by caoyanming on 2017/6/10.
 */

public class DaffodilPrinter {

    private static final String TAG = "DaffodilPrinter";

    private static AtomicBoolean sEnabled = new AtomicBoolean(true);

    private static DaffodilPrinterDelegate mPrintDelegate = new DaffodilPrinterDelegate() {

        @Override
        public void printMethod(MethodInfo info) {
            StringBuilder builder = new StringBuilder();
            builder.append(info.getCallerMethodName());
            builder.append("(");
            Object[] args = info.getArgs();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    builder.append(String.valueOf(arg))
                            .append(",");
                }
                builder.deleteCharAt(builder.length() - 1);
            }
            builder.append(")");

            Object ret = info.getReturn();
            if (ret != null) {
                builder.append(" = ").append(String.valueOf(ret));
            }
            builder.append(" {")
                    .append(info.getExitTime() - info.getEnterTime()).append("ms, ")
                    .append(info.getThreadName()).append("}");
            Log.i(removeSuffix(info.getCallerFileName()), builder.toString());
        }

        @Override
        public void log(String msg) {
            Log.i(TAG, msg);
        }

    };

    public static void setEnabled(boolean enabled) {
        sEnabled.compareAndSet(!enabled, enabled);
    }

    public static boolean isEnabled() {
        return sEnabled.get();
    }

    public static void setPrintDelegate(DaffodilPrinterDelegate mPrintDelegate) {
        DaffodilPrinter.mPrintDelegate = mPrintDelegate;
    }

    public static void printMethod(MethodInfo info) {
        if (mPrintDelegate != null && sEnabled.get()) {
            mPrintDelegate.printMethod(info);
        }
    }

    public static void log(String msg) {
        if (mPrintDelegate != null && sEnabled.get()) {
            mPrintDelegate.log(msg);
        }
    }

    public static final String removeSuffix(String source) {
        if (TextUtils.isEmpty(source) ) {
            return source;
        }
        int dot = source.lastIndexOf(".");
        if (dot >= 0) {
            return source.substring(0, dot);
        }
        return source;
    }

    public interface DaffodilPrinterDelegate {
        
        void printMethod(MethodInfo info);

        void log(String msg);
    }
}
