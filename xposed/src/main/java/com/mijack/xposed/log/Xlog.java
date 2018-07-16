package com.mijack.xposed.log;


import java.lang.reflect.Member;
import java.lang.reflect.Method;

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
    public static void logMethodEnter(Member method, Object instance, Object... params) {
        XlogBuilder.logMethodEnterInfo(-1, XlogBuilder.MethodType.USER_NOT_STATIC_METHOD, method, instance, params);
    }

    /**
     * log user static method enter
     */
    public static void logStaticMethodEnter(Member method, Object... params) {
        XlogBuilder.logMethodEnterInfo(-1, XlogBuilder.MethodType.USER_STATIC_METHOD, method, null, params);
    }

    /**
     * log user static method exit with throwable
     */
    public static void logStaticMethodExitWithThrowable(Member method, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_STATIC_METHOD, method, null, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log user no-static method exit with throwable
     */
    public static void logMethodExitWithThrowable(Member method, Object instance, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_NOT_STATIC_METHOD, method, instance, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log user static method exit without result
     *
     * @see #logStaticMethodExitWithResult(String, Object)
     */
    @Deprecated
    public static void logStaticMethodExit(Member method) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_STATIC_METHOD, method, null, XlogBuilder.MethodExecuteResultType.NO_THING, null, null);
    }


    /**
     * log user no-static method exit without result
     *
     * @see #logMethodExitWithResult(String, Object, Object)
     */
    @Deprecated
    public static void logMethodExit(Member method, Object instance,Object... params) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_NOT_STATIC_METHOD, method, instance, XlogBuilder.MethodExecuteResultType.NO_THING, null, null);
    }

    /**
     * log user static method exit with result
     */
    public static void logStaticMethodExitWithResult(Member method, Object result) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_STATIC_METHOD, method, null, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    /**
     * log user no-static method exit with result
     */
    public static void logMethodExitWithResult(Member method, Object instance, Object result) {
        XlogBuilder.logMethodExitInfo(-1, XlogBuilder.MethodType.USER_NOT_STATIC_METHOD, method, instance, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    //******************************************************
    //*                                                    *
    //*    the log method for system method from xposed    *
    //*                                                    *
    //******************************************************

    /**
     * log system no-static method enter
     */
    public static void logSystemMethodEnter(int hookId, Member method, Object instance, Object... params) {
        XlogBuilder.logMethodEnterInfo(hookId, XlogBuilder.MethodType.SYSTEM_NOT_STATIC_METHOD, method, instance, params);
    }

    /**
     * log system static method enter
     */
    public static void logSystemStaticMethodEnter(int hookId, Member method, Object... params) {
        XlogBuilder.logMethodEnterInfo(hookId, XlogBuilder.MethodType.SYSTEM_STATIC_METHOD, method, null, params);
    }

    /**
     * log system static method exit with throwable
     */
    public static void logSystemStaticMethodExitWithThrowable(int hookId, Member method, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(hookId, XlogBuilder.MethodType.SYSTEM_STATIC_METHOD, method, null, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);
    }

    /**
     * log system static method exit with result
     */
    public static void logSystemStaticMethodExitWithResult(int hookId, Member method, Object result) {
        XlogBuilder.logMethodExitInfo(hookId, XlogBuilder.MethodType.SYSTEM_STATIC_METHOD, method, null, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);
    }

    /**
     * log system no-static method exit with throwable
     */
    public static void logSystemMethodExitWithThrowable(int hookId, Member method, Object instance, Throwable throwable) {
        XlogBuilder.logMethodExitInfo(hookId, XlogBuilder.MethodType.SYSTEM_NOT_STATIC_METHOD, method, instance, XlogBuilder.MethodExecuteResultType.HAS_THROWABLE, null, throwable);

    }

    /**
     * log system no-static method exit with result
     */
    public static void logSystemMethodExitWithResult(int hookId, Member method, Object instance, Object result) {
        XlogBuilder.logMethodExitInfo(hookId, XlogBuilder.MethodType.SYSTEM_NOT_STATIC_METHOD, method, instance, XlogBuilder.MethodExecuteResultType.HAS_RESULT, result, null);

    }
}
