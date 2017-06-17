package tech.saymagic.daffodil.lib;

/**
 * Created by caoyanming on 2017/6/10.
 */

public class MethodInfo {

    private String mCallerMethodName;

    private String mCallerClass;

    private int mCallerLineNumber;

    private String mCallerFileName;

    private boolean mCallerMethodIsNative;

    private long mEnterTime;

    private Object[] mArgs;

    private Object mReturn;

    private long mExitTime;

    private String mThreadName;

    private long mThreadId;

    public MethodInfo(Object[] args, StackTraceElement stackTraceElement) {
        mEnterTime = System.currentTimeMillis();
        mArgs = args;
        initVarFromElement(stackTraceElement);
    }

    private void initVarFromElement(StackTraceElement stackTraceElement) {
        if (stackTraceElement != null) {
            mCallerClass = stackTraceElement.getClassName();
            mCallerFileName = stackTraceElement.getFileName();
            mCallerLineNumber = stackTraceElement.getLineNumber();
            mCallerMethodIsNative = stackTraceElement.isNativeMethod();
            mCallerMethodName = stackTraceElement.getMethodName();
        }
    }

    public long getEnterTime() {
        return mEnterTime;
    }

    public void setEnterTime(long enterTime) {
        mEnterTime = enterTime;
    }

    public Object[] getArgs() {
        return mArgs;
    }

    public void setArgs(Object[] args) {
        mArgs = args;
    }

    public Object getReturn() {
        return mReturn;
    }

    public void setReturn(Object aReturn) {
        mReturn = aReturn;
        this.mExitTime = System.currentTimeMillis();
    }

    public String getCallerMethodName() {
        return mCallerMethodName;
    }

    public void setCallerMethodName(String callerMethodName) {
        mCallerMethodName = callerMethodName;
    }

    public String getCallerClass() {
        return mCallerClass;
    }

    public void setCallerClass(String callerClass) {
        mCallerClass = callerClass;
    }

    public int getCallerLineNumber() {
        return mCallerLineNumber;
    }

    public void setCallerLineNumber(int callerLineNumber) {
        mCallerLineNumber = callerLineNumber;
    }

    public String getCallerFileName() {
        return mCallerFileName;
    }

    public void setCallerFileName(String callerFileName) {
        mCallerFileName = callerFileName;
    }

    public boolean isCallerMethodIsNative() {
        return mCallerMethodIsNative;
    }

    public void setCallerMethodIsNative(boolean callerMethodIsNative) {
        mCallerMethodIsNative = callerMethodIsNative;
    }

    public long getExitTime() {
        return mExitTime;
    }

    public void setExitTime(long exitTime) {
        mExitTime = exitTime;
    }

    public String getThreadName() {
        return mThreadName;
    }

    public void setThreadName(String threadName) {
        mThreadName = threadName;
    }

    public long getThreadId() {
        return mThreadId;
    }

    public void setThreadId(long threadId) {
        mThreadId = threadId;
    }
}
