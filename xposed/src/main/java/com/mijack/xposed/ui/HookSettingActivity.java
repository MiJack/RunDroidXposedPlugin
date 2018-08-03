package com.mijack.xposed.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.mijack.xposed.R;
import com.mijack.xposed.XposedLoadPackageHook;
import com.mijack.xposed.XposedUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author mijack
 */
public class HookSettingActivity extends BaseActivity {

    public static final String SOURCE = "SOURCE";
    public static final String SOURCE_ASSETS = "SOURCE_ASSETS";
    public static final String DEFAULT_ASSETS_NAME = "hook_info.properties";
    public static final String SETTING_FORMAT = "settings[%s]";
    RecyclerView recyclerView;
    Switch stateSwitcher;
    Switch debugAppListSwitcher;
    private HookSetting hookSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hook_setting);
        stateSwitcher = findViewById(R.id.state_switcher);
        debugAppListSwitcher = findViewById(R.id.debug_app_list_switcher);
        recyclerView = findViewById(R.id.recyclerView);
        Properties properties = loadProperties();
        if (properties == null) {
            Toast.makeText(this, "配置加载失败", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            return;
        }
        hookSetting = loadHookSetting(properties);
        setupUi(hookSetting);
    }

    private void setupUi(HookSetting hookSetting) {
        stateSwitcher.setChecked(hookSetting.isLogState);
        debugAppListSwitcher.setChecked(hookSetting.debugState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new HookSettingAdapter(hookSetting));
    }

    private HookSetting loadHookSetting(Properties properties) {
        HookSetting hookSetting = new HookSetting();
        hookSetting.setDebugState(Boolean.parseBoolean(properties.getProperty(String.format(SETTING_FORMAT, XposedLoadPackageHook.KEY_DEBUG_STATE))));
        hookSetting.setLogState(Boolean.parseBoolean(properties.getProperty(String.format(SETTING_FORMAT, XposedLoadPackageHook.KEY_IS_LOG_STATE))));
        Enumeration<Object> keys = properties.keys();
        List<AppHookSetting> settings = new ArrayList<>();
        hookSetting.setList(settings);
        while (keys.hasMoreElements()) {
            String keyValue = String.valueOf(keys.nextElement());
            if (TextUtils.isEmpty(keyValue) || !keyValue.startsWith("hook") || keyValue.length() < 6) {
                continue;
            }
            AppHookSetting appHookSetting = new AppHookSetting();
            String name = "";
            if (keyValue.startsWith("hook.system.")) {
                name = keyValue.substring("hook.".length());
                appHookSetting.setFramework(true);
            } else {
                appHookSetting.setFramework(false);
                name = keyValue.substring("hook.".length(), keyValue.length() - 1);
            }
            appHookSetting.setName(name);
            String property = properties.getProperty(keyValue);
            String[] split = property.split(";");
            List<String> methodSigns = new ArrayList<>();
            if (split != null && split.length > 0) {
                for (String methodSign : split) {
                    if (TextUtils.isEmpty(methodSign)) {
                        continue;
                    }
                    methodSigns.add(methodSign);
                }
            }
            appHookSetting.setMethodSignList(methodSigns);
            appHookSetting.setSelected(true);
            settings.add(appHookSetting);
        }
        return hookSetting;
    }

    private Properties loadProperties() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        String source = intent.getStringExtra(SOURCE);
        if (SOURCE_ASSETS.equals(source)) {
            Properties properties = new Properties();
            try {
                properties.load(getAssets().open(DEFAULT_ASSETS_NAME));
                return properties;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hook_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_hook_settings:
                Toast.makeText(this, "正在保存配置文件", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(XposedLoadPackageHook.KEY_DEBUG_STATE, hookSetting.debugState);
                editor.putBoolean(XposedLoadPackageHook.KEY_IS_LOG_STATE, hookSetting.isLogState);
                Set<String> frameworkMethods = new HashSet<>();
                Set<String> appSet = new HashSet<>();
                for (AppHookSetting appHookSetting : hookSetting.getList()) {
                    if (!appHookSetting.isSelected()) {
                        continue;
                    }
                    if (appHookSetting.isFramework()) {
                        frameworkMethods.addAll(appHookSetting.methodSignList);
                    } else {
                        appSet.add(appHookSetting.getName());
                        editor.putStringSet(appHookSetting.getName(), new HashSet<>(appHookSetting.methodSignList));
                    }
                }
                editor.putStringSet(XposedLoadPackageHook.FRAMEWORK, frameworkMethods);
                editor.putStringSet(XposedLoadPackageHook.TARGET_APPS, appSet);
                editor.apply();
                setResult(RESULT_OK);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
