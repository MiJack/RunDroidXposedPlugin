package com.mijack.xposed;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodReplacement;

/**
 * @author Mi&Jack
 * @since 2018/7/19
 */
public abstract class BaseMethodReplacement extends XC_MethodReplacement{

    public Member toMethod(String methodSign) {
        try {
            //void com.mijack.XlogBuilder.logMethodExitInfo(int,com.mijack.XlogBuilder$MethodType,java.lang.String,java.lang.Object,com.mijack.XlogBuilder$MethodExecuteResultType,java.lang.Object,java.lang.Throwable)
            String substring = methodSign.substring(methodSign.indexOf(" "));
            String pre = substring.substring(0, substring.indexOf("("));
            String className = pre.substring(0, pre.lastIndexOf("."));
            String methodName = pre.substring(pre.lastIndexOf("."));
            String args = substring.substring(substring.indexOf("("));
            Class<?> instanceClazz = Class.forName(className);
            String[] split = args.split(",");
            Class<?>[] objects = new Class[]{};
            if (split.length != 0) {
                objects = new Class[split.length];
                for (int i = 0; i < split.length; i++) {
                    objects[i] = XposedLoadPackageHook.loadClass(split[i], getClass().getClassLoader());
                }
            }
            return instanceClazz.getMethod(methodName, objects);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
