package com.mijack.xposed.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mijack.xposed.R;
import com.mijack.xposed.XposedLoadPackageHook;

import java.util.HashSet;

import java.util.Set;

import static com.mijack.xposed.XposedLoadPackageHook.KEY_IS_LOG_STATE;
import static com.mijack.xposed.XposedLoadPackageHook.KEY_DEBUG_STATE;


/**
 * @author Mi&Jack
 */
public class DebugAppListActivity extends BaseActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    ListView listView;
    Switch stateSwitcher;
    Switch debugAppListSwitcher;
    StringDataAdapter appAdapter = new StringDataAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_app_list);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> appList = sharedPreferences.getStringSet(XposedLoadPackageHook.TARGET_APPS,
                new HashSet<String>());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppHookMethodList("android");
            }
        });
        listView = findViewById(R.id.list);
        stateSwitcher = findViewById(R.id.state_switcher);
        debugAppListSwitcher = findViewById(R.id.debug_app_list_switcher);

        boolean isLogState = sharedPreferences.getBoolean(KEY_IS_LOG_STATE, false);
        boolean debugState = sharedPreferences.getBoolean(KEY_DEBUG_STATE, false);
        stateSwitcher.setChecked(isLogState);
        stateSwitcher.setOnCheckedChangeListener(this);
        debugAppListSwitcher.setChecked(debugState);
        debugAppListSwitcher.setOnCheckedChangeListener(this);
        appAdapter.setData(appList);
        listView.setAdapter(appAdapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_app:
                showAddAppDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddAppDialog() {
        final View addView = LayoutInflater.from(this).inflate(R.layout.dialog_add_app, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加app")
                .setView(addView)
                .setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addView == null) {
                            return;
                        }
                        EditText editText = addView.findViewById(R.id.editTextApp);
                        if (TextUtils.isEmpty(editText.getText())) {
                            Toast.makeText(DebugAppListActivity.this, "请输入app名称", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String app = editText.getText().toString();
                        addApp(app);

                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .create().show();
    }

    private void addApp(String app) {
        appAdapter.addData(app);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> appList = sharedPreferences.getStringSet(XposedLoadPackageHook.TARGET_APPS, new HashSet<String>());
        appList.add(app);
        sharedPreferences.edit().putStringSet(XposedLoadPackageHook.TARGET_APPS, appList).apply();
    }

    private void removeApp(String app) {
        appAdapter.removeData(app);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> appList = sharedPreferences.getStringSet(XposedLoadPackageHook.TARGET_APPS, new HashSet<String>());
        appList.remove(app);
        sharedPreferences.edit().putStringSet(XposedLoadPackageHook.TARGET_APPS, appList).apply();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // remove
        View removeView = LayoutInflater.from(this).inflate(R.layout.dialog_remove_app, null);
        final String app = appAdapter.getItem(position);
        TextView textView = removeView.findViewById(R.id.textApp);
        textView.setText(app);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("移除app")
                .setView(removeView)
                .setNegativeButton("移除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeApp(app);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String app = appAdapter.getItem(position);
        showAppHookMethodList(app);
    }

    private void showAppHookMethodList(String app) {

        Intent intent = new Intent(this, MethodListActivity.class);
        intent.putExtra(MethodListActivity.APP_NAME, app);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.debug_app_list_switcher:
                getSharedPreferences().edit().putBoolean(KEY_DEBUG_STATE, isChecked).commit();
                return;
            case R.id.state_switcher:
                getSharedPreferences().edit().putBoolean(KEY_IS_LOG_STATE, isChecked).commit();
                return;
        }
    }
}
