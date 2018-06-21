package com.mijack;

/**
 * @author Mi&Jack
 */
public class XlogBuilder {

    public enum MethodType {
        USER_STATIC_METHOD(1), USER_NOT_STATIC_METHOD(2),
        SYSTEM_STATIC_METHOD(3), SYSTEM_NOT_STATIC_METHOD(4);

        public static final String INSTANCE_METHOD_TYPE = "instance_method_type";
        public static final String STATIC_METHOD_TYPE = "static_method_type";
        private int id;

        MethodType(int id) {
            this.id = id;
        }

        public boolean hasInstance() {
            return id % 2 == 0;
        }

        public String typename() {
            return id % 2 == 0 ? INSTANCE_METHOD_TYPE : STATIC_METHOD_TYPE;
        }
    }

    public enum MethodExecuteResultType {
        NO_THING, HAS_RESULT, HAS_THROWABLE
    }

    public static void logMethodEnterInfo(int hookId, MethodType methodType, String methodSign, Object instance, Object... args) {
        int pid = XlogUtils.getProcessId();
        int threadId = XlogUtils.getCurrentThreadId();

        StringBuilder sb = new StringBuilder("{").append(String.format(KEY_TO_VALUE, "logType", LOG_TYPE_ENTER));
        sb.append(",").append(String.format(KEY_TO_VALUE, "time", XlogUtils.currentTime()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "processName", XlogUtils.getProcessName()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "threadName", XlogUtils.getCurrentThreadInfo()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "pid", pid));
        if (hookId > 0) {
            sb.append(",").append(String.format(KEY_TO_VALUE, "hookId", hookId));
        }
        XlogUtils.appendInvokeLine(hookId, sb);
        sb.append(",").append(String.format(KEY_TO_VALUE, "methodType", methodType.typename()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "methodSign", methodSign));
//判断是否是方法开始
        if (methodType.hasInstance()) {
            sb.append(",").append(String.format(KEY_TO_VALUE2, "instance", XlogUtils.object2String(instance)));
        }
        sb.append(",").append(XlogUtils.paramsToString(args));
        sb.append("}");
        LogWriter.d(hookId, pid, threadId, sb.toString());
    }

    public static void logMethodExitInfo(int hookId, MethodType methodType, String methodSign,
                                         Object instance, MethodExecuteResultType resultType, Object result, Throwable throwable) {

        int pid = XlogUtils.getProcessId();
        int threadId = XlogUtils.getCurrentThreadId();

        StringBuilder sb = new StringBuilder("{").append(String.format(KEY_TO_VALUE, "logType", LOG_TYPE_EXIT));
        sb.append(",").append(String.format(KEY_TO_VALUE, "time", XlogUtils.currentTime()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "processName", XlogUtils.getProcessName()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "threadName", XlogUtils.getCurrentThreadInfo()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "pid", pid));
        XlogUtils.appendInvokeLine(hookId, sb);
        if (hookId > 0) {
            sb.append(",").append(String.format(KEY_TO_VALUE, "hookId", hookId));
        }
        sb.append(",").append(String.format(KEY_TO_VALUE, "methodType", methodType.typename()));
        sb.append(",").append(String.format(KEY_TO_VALUE, "methodSign", methodSign));
//判断是否是方法开始
        if (methodType.hasInstance()) {
            sb.append(",").append(String.format(KEY_TO_VALUE2, "instance", XlogUtils.object2String(instance)));
        }
        switch (resultType) {
            case HAS_RESULT:
                sb.append(",").append(String.format(KEY_TO_VALUE2, "result", XlogUtils.object2String(result)));
                break;
            case HAS_THROWABLE:
                sb.append(",").append(String.format(KEY_TO_VALUE, "throwable", XlogUtils.object2String(throwable)));
                break;
            default:
            case NO_THING:
                // do nothing
                break;
        }
        sb.append("}");
        LogWriter.d(hookId, pid, threadId, sb.toString());
    }


    public static final String KEY_TO_VALUE = "\"%s\":\"%s\"";
    public static final String KEY_TO_VALUE2 = "\"%s\":%s";

    public static final String LOG_TYPE_ENTER = "enter";
    public static final String LOG_TYPE_EXIT = "exit";
}
