package com.mijack.xposed;

import android.text.TextUtils;


import com.mijack.xposed.log.SystemLogHookCallBack;
import com.mijack.xposed.log.XlogBuilder;
import com.mijack.xposed.log.XlogUtils;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author Mr.Yuan
 * @since 2017/2/23.
 */
public class XposedLoadPackageHook implements IXposedHookLoadPackage {

    public static final String XPOSED_PLUGIN_PACKAGE = "com.mijack.xposed";
    public static final String KEY_IS_LOG_STATE = "is_log_state";
    public static final String KEY_DEBUG_STATE = "debug_state";
    public static final Set<String> EMPTY_SET = new HashSet<String>();
    public static final String FRAMEWORK = "android";
    public static final String TARGET_APPS = "targetApps";
    private XSharedPreferences prefs;

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam)
            throws Throwable {
        XposedBridge.log("load process:" + lpparam.processName);
        prefs = new XSharedPreferences(XPOSED_PLUGIN_PACKAGE);
        if ("android".equals(lpparam.processName)) {
            return;
        }
        boolean makeWorldReadable = prefs.makeWorldReadable();
        XposedBridge.log("makeWorldReadable：" + makeWorldReadable);
        XlogBuilder.IS_LOG_STATE = prefs.getBoolean(KEY_IS_LOG_STATE, false);
        XposedBridge.log("is log state:" + XlogBuilder.IS_LOG_STATE);
        XlogBuilder.DEBUG_STATE = prefs.getBoolean(KEY_DEBUG_STATE, false);
        XposedBridge.log("debug state:" + XlogBuilder.DEBUG_STATE);

        Set<String> targetApps = prefs.getStringSet(TARGET_APPS, EMPTY_SET);
        XposedBridge.log("Target app size:" + targetApps.size());
        boolean isHit = false;
        for (String app : targetApps) {
            XposedBridge.log("Target app:" + app);
            if (app.equals(lpparam.packageName)) {
                isHit = true;
            }
        }
        if (!isHit) {
            XposedBridge.log("不加载apk\n" + "包名: " + lpparam.packageName);
            // 不可加载应用
            return;
        }
        XlogUtils.setProcessName(lpparam.processName);
        XposedBridge.log("开始加载apk\n" + "包名: " + lpparam.packageName);

        hookApplicationMethods(lpparam, FRAMEWORK);
        //load app hook
        hookApplicationMethods(lpparam, lpparam.packageName);
        //load x-log hook
        hookEmptyLogMethods(lpparam, lpparam.packageName);

    }

    private void hookEmptyLogMethods(XC_LoadPackage.LoadPackageParam lpparam, String packageName) {
        try {
            // logMethodEnterInfo(int hookId, MethodType methodType, String methodSign,
            // Object instance, Object... args)
            Class xlogBuilderClazz = lpparam.classLoader.loadClass("com.mijack.XlogBuilder");
            Class<?> methodTypeClazz = lpparam.classLoader.loadClass("com.mijack.XlogBuilder$MethodType");
            Class<?> resultTypeClazz = lpparam.classLoader.loadClass("com.mijack.XlogBuilder$MethodExecuteResultType");

            EmptyMethodEnterLogHookCallBack enterLogHook = new EmptyMethodEnterLogHookCallBack();
            String enterLogMethodName = "logMethodEnterInfo";
            Object[] enterLogArgArray = new Object[]{int.class, methodTypeClazz, String.class, Object.class, Object[].class,
                    enterLogHook};
            XposedHelpers.findAndHookMethod(xlogBuilderClazz, enterLogMethodName, enterLogArgArray);

            //  logMethodExitInfo(int hookId, MethodType methodType, String methodSign,
            //                                         Object instance, MethodExecuteResultType resultType, Object result, Throwable throwable) {
            //
            EmptyMethodExitLogHookCallBack methodExitLogHookCallBack = new EmptyMethodExitLogHookCallBack();
            String exitLogMethodName = "logMethodExitInfo";
            Object[] exitLogArgArray = new Object[]{int.class, methodTypeClazz, String.class, Object.class, resultTypeClazz,
                    Object.class, Throwable.class,
                    methodExitLogHookCallBack};
            XposedHelpers.findAndHookMethod(xlogBuilderClazz, exitLogMethodName, exitLogArgArray);


            XposedBridge.log("hook 应用日志成功");
        } catch (ClassNotFoundException e) {
            XposedBridge.log("hook 应用日志失败");
        }
    }

    @Deprecated
    private void hookXLogMethods(XC_LoadPackage.LoadPackageParam lpparam, String packageName) {
        try {
            SystemLogHookCallBack callBack = new SystemLogHookCallBack();
            Class clazz = lpparam.classLoader.loadClass("com.mijack.LogWriter");
            String methodName = "d";
            Object[] argArray = new Object[]{int.class, int.class, int.class, String.class, callBack};
            XposedHelpers.findAndHookMethod(clazz, methodName, argArray);
            XposedBridge.log("hook 应用日志成功");
        } catch (ClassNotFoundException e) {
            XposedBridge.log("hook 应用日志失败");
        }


    }

    private void hookApplicationMethods(XC_LoadPackage.LoadPackageParam lpparam, String packageName) {
        if (prefs == null) {
            return;
        }
        Set<String> methods = prefs.getStringSet(packageName, EMPTY_SET);
        if (methods != null && methods.size() > 0) {
            for (String method : methods) {
                prepareHookMethod(method, lpparam);
            }
        }
    }

    private void prepareHookMethod(String line, XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            if (TextUtils.isEmpty(line)) {
                return;
            }
            if (line.startsWith("//") || line.startsWith("#")) {
                XposedBridge.log("注释无效：" + line);
                return;
            }
            XposedBridge.log("开始加载配置：" + line);
            hookMethod(line, lpparam);
            XposedBridge.log("加载配置成功：" + line);
        } catch (Exception e) {
            XposedBridge.log("加载配置失败：" + line + "\t" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void hookMethod(String line, XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        String[] raw = line.split(" ");
        if (raw == null || raw.length < 2) {
            throw new IllegalArgumentException("缺少类名或者函数名");
        }
        int index = 0;
        boolean isStatic = false;
        for (int i = 0; i < raw.length; i++) {
            if ("[static]".equals(raw[i]) || "[abstract]".equals(raw[i])) {
                index++;
            }
            if ("[static]".equals(raw[i])) {
                isStatic = true;
            }
        }
        String[] split = new String[raw.length - index];
        System.arraycopy(raw, index, split, 0, split.length);
        if (split == null || split.length < 2) {
            throw new IllegalArgumentException("缺少类名或者函数名");
        }
        //拷args的类型
        String[] argsType = new String[split.length - 2];
        System.arraycopy(split, 2, argsType, 0, argsType.length);
        Class clazz = lpparam.classLoader.loadClass(split[0]);
        String methodName = split[1];
        //最后一位是MethodHook
        Object[] argArray = new Object[split.length - 1];
        for (int i = 0; i < argArray.length - 1; i++) {
            argArray[i] = loadClass(split[i + 2], lpparam);
        }
        argArray[argArray.length - 1] = new HookCallBack(argsType, isStatic);
        XposedBridge.log("method:" + methodName);
        if (methodName.equals("<init>")) {
            XposedHelpers.findAndHookConstructor(clazz, argArray);
        } else if (methodName.equals("<clinit>")) {
            XposedHelpers.findAndHookConstructor(clazz, argArray);
        } else {
            XposedHelpers.findAndHookMethod(clazz, methodName, argArray);
        }
    }

    private Class loadClass(String type, XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        return loadClass(type, lpparam.classLoader);
    }

    public static Class loadClass(String type, ClassLoader classLoader) throws ClassNotFoundException {
        int lastIndexOf = type.lastIndexOf("[");
        int dimens = lastIndexOf + 1;
        if (dimens > 0) {
            String rawType = type.substring(dimens);
            int[] ds = new int[dimens];
            return Array.newInstance(loadClass(rawType, classLoader), ds).getClass();
        }
        if (boolean.class.getName().equals(type)) {
            return boolean.class;
        } else if (byte.class.getName().equals(type)) {
            return byte.class;
        } else if (char.class.getName().equals(type)) {
            return char.class;
        } else if (double.class.getName().equals(type)) {
            return double.class;
        } else if (float.class.getName().equals(type)) {
            return float.class;
        } else if (int.class.getName().equals(type)) {
            return int.class;
        } else if (long.class.getName().equals(type)) {
            return long.class;
        } else if (short.class.getName().equals(type)) {
            return short.class;
        } else {
            return classLoader.loadClass(type);
        }
    }

    public static String getPackageName() {
        return "com.mijack.xposed";
    }
}
