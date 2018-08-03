package com.mijack.xposed.ui;

import java.util.List;

/**
 * @author Mi&Jack
 * @since 2018/8/4
 */
public class HookSetting {
    boolean isLogState;
    boolean debugState;
    List<AppHookSetting> list;

    public boolean isLogState() {
        return isLogState;
    }

    public void setLogState(boolean logState) {
        isLogState = logState;
    }

    public boolean isDebugState() {
        return debugState;
    }

    public void setDebugState(boolean debugState) {
        this.debugState = debugState;
    }

    public List<AppHookSetting> getList() {
        return list;
    }

    public void setList(List<AppHookSetting> list) {
        this.list = list;
    }


}
