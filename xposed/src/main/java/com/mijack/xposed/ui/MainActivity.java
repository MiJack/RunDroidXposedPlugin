package com.mijack.xposed.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.mijack.xposed.R;
import com.mijack.xposed.XposedLoadPackageHook;
import com.mijack.xposed.XposedUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static com.mijack.xposed.XposedLoadPackageHook.KEY_DEBUG_STATE;
import static com.mijack.xposed.XposedLoadPackageHook.KEY_IS_LOG_STATE;

/**
 * @author Mi&Jack
 */
public class MainActivity extends Activity implements View.OnClickListener, RecyclerViewItemClickListener<String> {
    private static final int CODE_LOAD_HOOK_SETTINGS = 1;
    Button btnDefaultConfig;
    Button btnSDCardConfig;
    RecyclerView recyclerView;
    Switch stateSwitcher;
    Switch debugAppListSwitcher;
    StringListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 申请sdcard权限
        btnDefaultConfig = findViewById(R.id.btnDefaultConfig);
        btnSDCardConfig = findViewById(R.id.btnSDCardConfig);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StringListAdapter(this);
        recyclerView.setAdapter(adapter);
        stateSwitcher = findViewById(R.id.state_switcher);
        debugAppListSwitcher = findViewById(R.id.debug_app_list_switcher);
        btnDefaultConfig.setOnClickListener(this);
        loadFromSharedPreferences();

    }

    private void loadFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        boolean isLogState = sharedPreferences.getBoolean(KEY_IS_LOG_STATE, false);
        boolean debugState = sharedPreferences.getBoolean(KEY_DEBUG_STATE, false);
        stateSwitcher.setChecked(isLogState);
        debugAppListSwitcher.setChecked(debugState);
        List<String> appList = new ArrayList<>(sharedPreferences.getStringSet(XposedLoadPackageHook.TARGET_APPS,
                new HashSet<String>()));
        appList.add(0, "android");
        adapter.setData(appList);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDefaultConfig:
                Intent intent = new Intent(this, HookSettingActivity.class);
                intent.putExtra(HookSettingActivity.SOURCE, HookSettingActivity.SOURCE_ASSETS);
                startActivityForResult(intent, CODE_LOAD_HOOK_SETTINGS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_LOAD_HOOK_SETTINGS && resultCode == RESULT_OK) {
            loadFromSharedPreferences();
        }
    }

    public final SharedPreferences getSharedPreferences() {
        String preferenceName = XposedUtils.getPreferenceName();
        return getSharedPreferences(preferenceName, Context.MODE_WORLD_READABLE);
    }

    @Override
    public void onItemClick(View view, int position, String item) {
        Intent intent = new Intent(this, MethodListActivity.class);
        intent.putExtra(MethodListActivity.APP_NAME, item);
        startActivity(intent);
    }
}
