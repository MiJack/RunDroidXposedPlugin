package com.mijack.xposed.log;

import de.robv.android.xposed.XC_MethodHook;

/**
 * @author Mi&Jack
 * @since 2018/6/22
 */
public class SystemLogHookCallBack extends XC_MethodHook {
    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        int hookId = (int) param.args[0];
        int pid = (int) param.args[1];
        int threadId = (int) param.args[2];
        String msg = (String) param.args[3];
        LogWriter.d(hookId, pid, threadId, msg);
    }
}
