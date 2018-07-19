package com.mijack.xposed;


import com.mijack.xposed.log.Xlog;
import com.mijack.xposed.log.XlogBuilder;

import java.lang.reflect.Member;

/**
 * @author Mi&Jack
 * @since 2018/7/19
 */
public class EmptyMethodExitLogHookCallBack extends BaseMethodReplacement {
    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        //  logMethodExitInfo(int hookId,
        // MethodType methodType, String methodSign,
        // Object instance, MethodExecuteResultType resultType,
        // Object result, Throwable throwable) {
        //

        XlogBuilder.MethodType methodType = (XlogBuilder.MethodType) param.args[1];
        String methodSign = (String) param.args[2];
        Object instance = param.args[3];
        XlogBuilder.MethodExecuteResultType resultType = (XlogBuilder.MethodExecuteResultType) param.args[4];
        Object result = param.args[5];
        Throwable throwable = param.args[6] != null ? (Throwable) param.args[6] : null;
        Member method = toMethod(methodSign);
        boolean hasThrowable = XlogBuilder.MethodExecuteResultType.HAS_THROWABLE.equals(resultType);
        boolean isStatic = !methodType.hasInstance();
        if (isStatic && hasThrowable) {
            Xlog.logStaticMethodExitWithThrowable(method, throwable);
        } else if (isStatic && !hasThrowable) {
            Xlog.logStaticMethodExitWithResult(method, result);
        } else if (!isStatic && hasThrowable) {
            Xlog.logMethodExitWithThrowable(method, instance, throwable);
        } else {
            Xlog.logMethodExitWithResult(method, instance, result);
        }
        return null;
    }
}
