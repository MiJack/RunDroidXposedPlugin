package com.mijack.xposed.ui;

import java.util.List;

/**
 * @author Mi&Jack
 * @since 2018/8/4
 */
public class AppHookSetting {
    String name;
    boolean selected;
    boolean isFramework;
    List<String> methodSignList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isFramework() {
        return isFramework;
    }

    public void setFramework(boolean framework) {
        isFramework = framework;
    }

    public List<String> getMethodSignList() {
        return methodSignList;
    }

    public void setMethodSignList(List<String> methodSignList) {
        this.methodSignList = methodSignList;
    }

    @Override
    public String toString() {
        return "AppHookSetting{" +
                "name='" + name + '\'' +
                ", selected=" + selected +
                ", isFramework=" + isFramework +
                ", methodSignList=" + methodSignList.size() +
                '}';
    }
}