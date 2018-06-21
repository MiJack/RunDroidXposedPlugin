package com.mijack.xposed;

import com.mijack.Xlog;

import de.robv.android.xposed.XC_MethodHook;

import static com.mijack.XlogUtils.method2String;

/**
 * Created by Mr.Yuan on 2017/2/23.
 */
public class HookCallBack extends XC_MethodHook {
    public static final ThreadLocal<String> THREAD_INFO_LOCAL = new ThreadLocal<>();
    private String[] argsType;
    private boolean isStatic;
    public static final char LINE_SPLIT_CHAR = '\t';

    public HookCallBack(String[] argsType, boolean isStatic) {
        this.argsType = argsType;
        this.isStatic = isStatic;
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        if (!isStatic) {
            Xlog.logSystemMethodEnter(System.identityHashCode(param), method2String(param.method), param.thisObject, param.args);
        } else {
            Xlog.logSystemStaticMethodEnter(System.identityHashCode(param), method2String(param.method), param.args);
        }
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        boolean hasThrowable = param.hasThrowable();
        if (isStatic && hasThrowable) {
            Xlog.logSystemStaticMethodExitWithThrowable(System.identityHashCode(param), method2String(param.method), param.getThrowable());
        } else if (isStatic && !hasThrowable) {
            Xlog.logSystemStaticMethodExitWithResult(System.identityHashCode(param), method2String(param.method), param.getResult());
        } else if (!isStatic && hasThrowable) {
            Xlog.logSystemMethodExitWithThrowable(System.identityHashCode(param), method2String(param.method), param.thisObject, param.getThrowable());
        } else {
            Xlog.logSystemMethodExitWithResult(System.identityHashCode(param), method2String(param.method), param.thisObject, param.getResult());
        }
    }


}
