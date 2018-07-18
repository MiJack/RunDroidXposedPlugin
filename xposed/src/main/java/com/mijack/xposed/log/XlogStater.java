package com.mijack.xposed.log;

import android.app.Activity;
import android.view.View;


import java.lang.reflect.Field;
import java.lang.reflect.Member;

import static com.mijack.xposed.log.XlogBuilder.KEY_TO_VALUE2;


/**
 * @author Mi&Jack
 * @since 2018/7/15
 */

public class XlogStater {
    public static final String[] ACTIVITY_LIFECYCLE_METHOD_LIST = new String[]{
            ".onCreate(android.os.Bundle)",
            ".onStart()",
            ".onResume()",
            ".onPause()",
            ".onStop()",
            ".onDestroy()",};
    public static final String[] WIDGET_CLICK_METHOD_LIST = new String[]{
            ".onClick(android.view.View)"
    };

    public static String activityState(Object instance, Object[] args) {
        if (instance == null || !(instance instanceof Activity)) {
            return "{}";
        }
        Activity activity = (Activity) instance;
        return activityDetailState(activity);
    }

    public static String activityDetailState(Activity activity) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
//        int i=0;
        Field[] declaredFields = activity.getClass().getDeclaredFields();
        for (int i = 0; declaredFields != null && i < declaredFields.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            Field field = declaredFields[i];
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            try {
                sb.append(String.format(KEY_TO_VALUE2, field.getName(), XlogUtils.object2String(field.get(activity))));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        sb.append("}");
        return sb.toString();
    }

    public static String widgetState(Object instance, Object[] args) {
        if (args == null
                || args.length != 1
                || args[0] == null
                || !(args[0] instanceof View)
                ) {
            return "{}";
        }
        View view = (View) args[0];
        if (view.getContext() == null || !(view.getContext() instanceof Activity)) {
            return "{}";
        }
        Activity activity = (Activity) view.getContext();
        return activityDetailState(activity);
    }

    public enum StateMethodType {
        /**
         * Activity的生命周期
         */
        ACTIVITY,
        /**
         * 点击事件
         */
        ON_CLICK,
        /**
         * 无需记录
         */
        NO_LOG;
    }

    public static StateMethodType getStateType(Member method) {
        if (method == null) {
            return StateMethodType.NO_LOG;
        }
        if (Activity.class.isAssignableFrom(method.getDeclaringClass())) {
            for (String methodSign : ACTIVITY_LIFECYCLE_METHOD_LIST) {
                if (method.toString().endsWith(methodSign)) {
                    return StateMethodType.ACTIVITY;
                }
            }
            return StateMethodType.NO_LOG;
        }
        if (View.OnClickListener.class.isAssignableFrom(method.getDeclaringClass())) {
            for (String methodSign : WIDGET_CLICK_METHOD_LIST) {
                if (method.toString().endsWith(methodSign)) {
                    return StateMethodType.ON_CLICK;
                }
            }
            return StateMethodType.NO_LOG;
        }
        return StateMethodType.NO_LOG;
    }
}
