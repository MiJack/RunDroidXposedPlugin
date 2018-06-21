package com.mijack;

import com.mijack.XlogBuilder.MethodType;

/**
 * the XLog Facade class
 *
 * @author Mi&Jack
 */
public class Xlog {
    //****************************************
    //*                                      *
    //*    the log method for user method    *
    //*                                      *
    //****************************************

    /**
     * log user no-static method enter
     */
    public static void logMethodEnter(String methodSign, Object instance, Object... params) {
        XlogBuilder.logMethodEnterInfo(-1, MethodType.USER_NOT_STATIC_METHOD, methodSign, instance, params);
    }

    /**
     * log user static method enter
     */
    public static void logStaticMethodEnter(String methodSign, Object... params) {
        XlogBuilder.logMethodEnterInfo(-1, MethodType.USER_STATIC_METHOD, methodSign, null, params);
    }

    /**
     * log user static method exit with throwable
     */
    public static void logStaticMethodExitWithThrowable(String methodSign, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_STATIC_METHOD, methodSign, null, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log user no-static method exit with throwable
     */
    public static void logMethodExitWithThrowable(String methodSign, Object instance, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_NOT_STATIC_METHOD, methodSign, instance, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log user static method exit without result
     *
     * @see #logStaticMethodExitWithResult(String, Object)
     */
    @Deprecated
    public static void logStaticMethodExit(String methodSign) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_STATIC_METHOD, methodSign, null, XlogBuilder.MethodExecuteResultType.NO_THING, null, null);
    }


    /**
     * log user no-static method exit without result
     *
     * @see #logMethodExitWithResult(String, Object, Object)
     */
    @Deprecated
    public static void logMethodExit(String methodSign, Object instance,Object... params) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_NOT_STATIC_METHOD, methodSign, instance, XlogBuilder.MethodExecuteResultType.NO_THING, null, null);
    }

    /**
     * log user static method exit with result
     */
    public static void logStaticMethodExitWithResult(String methodSign, Object result) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_STATIC_METHOD, methodSign, null, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    /**
     * log user no-static method exit with result
     */
    public static void logMethodExitWithResult(String methodSign, Object instance, Object result) {
        XlogBuilder.logMethodExitInfo(-1, MethodType.USER_NOT_STATIC_METHOD, methodSign, instance, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    //******************************************************
    //*                                                    *
    //*    the log method for system method from xposed    *
    //*                                                    *
    //******************************************************

    /**
     * log system no-static method enter
     */
    public static void logSystemMethodEnter(int hookId, String methodSign, Object instance, Object... params) {
        XlogBuilder.logMethodEnterInfo(hookId, MethodType.SYSTEM_NOT_STATIC_METHOD, methodSign, instance, params);
    }

    /**
     * log system static method enter
     */
    public static void logSystemStaticMethodEnter(int hookId, String methodSign, Object... params) {
        XlogBuilder.logMethodEnterInfo(hookId, MethodType.SYSTEM_STATIC_METHOD, methodSign, null, params);
    }

    /**
     * log system static method exit with throwable
     */
    public static void logSystemStaticMethodExitWithThrowable(int hookId, String methodSign, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(hookId, MethodType.SYSTEM_STATIC_METHOD, methodSign, null, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log system static method exit with result
     */
    public static void logSystemStaticMethodExitWithResult(int hookId, String methodSign, Object result) {
        XlogBuilder.logMethodExitInfo(hookId, MethodType.SYSTEM_STATIC_METHOD, methodSign, null, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    /**
     * log system no-static method exit with throwable
     */
    public static void logSystemMethodExitWithThrowable(int hookId, String methodSign, Object instance, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(hookId, MethodType.SYSTEM_NOT_STATIC_METHOD, methodSign, instance, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);

    }

    /**
     * log system no-static method exit with result
     */
    public static void logSystemMethodExitWithResult(int hookId, String methodSign, Object instance, Object result) {
        XlogBuilder.logMethodExitInfo(hookId, MethodType.SYSTEM_NOT_STATIC_METHOD, methodSign, instance, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);

    }
}
