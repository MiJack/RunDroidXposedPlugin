package com.mijack.xposed;

import com.mijack.xposed.log.Xlog;
import com.mijack.xposed.log.XlogBuilder;
import com.mijack.xposed.log.XlogUtils;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodReplacement;

/**
 * @author Mi&Jack
 * @since 2018/7/19
 */
public class EmptyMethodEnterLogHookCallBack extends BaseMethodReplacement {

    @Override
    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
        // logMethodEnterInfo(int hookId, MethodType methodType, String methodSign,
        // Object instance, Object... args)

        XlogBuilder.MethodType methodType = (XlogBuilder.MethodType) param.args[1];
        String methodSign = (String) param.args[2];
        Object instance = param.args[3];
        Object[] args = (Object[]) param.args[4];
        Member method = toMethod(methodSign);
        if (methodType.hasInstance()) {
            Xlog.logMethodEnter(method, instance, args);
        } else {
            Xlog.logStaticMethodEnter(method, args);
        }
        return null;
    }

}
