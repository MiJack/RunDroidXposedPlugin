package com.mijack.xposed.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AdapterView;

import com.mijack.xposed.XposedUtils;

/**
 * @author Mi&Jack
 * @since 2018/7/15
 */
public abstract class BaseActivity extends Activity implements AdapterView.OnItemLongClickListener {

    public final Context getContext() {
        return this;
    }

    public final SharedPreferences getSharedPreferences() {
        String preferenceName = XposedUtils.getPreferenceName();
        return getSharedPreferences(preferenceName, Context.MODE_WORLD_READABLE);
    }
}
