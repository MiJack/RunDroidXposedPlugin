package com.mijack.xposed.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mijack.xposed.R;
import com.mijack.xposed.XposedLoadPackageHook;
import com.mijack.xposed.XposedUtils;

import java.util.HashSet;

import java.util.Set;


/**
 * @author Mi&Jack
 */
public class DebugAppListActivity extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    ListView listView;
    StringDataAdapter appAdapter = new StringDataAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> appList = sharedPreferences.getStringSet(XposedLoadPackageHook.TARGET_APPS,
                new HashSet<>());
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAppHookMethodList("android");
            }
        });
        listView = findViewById(R.id.list);

        appAdapter.setData(appList);
        listView.setAdapter(appAdapter);
        listView.setOnItemLongClickListener(this);
        listView.setOnItemClickListener(this);
    }

    private SharedPreferences getSharedPreferences() {
        String preferenceName = XposedUtils.getPreferenceName();
        return getSharedPreferences(preferenceName, Context.MODE_WORLD_READABLE);
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
        View addView = LayoutInflater.from(this).inflate(R.layout.dialog_add_app, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加app")
                .setView(addView)
                .setNegativeButton("添加", (dialog, which) -> {
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
                })
                .setPositiveButton("取消", (dialog, which) -> {
                    return;

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

    public Context getContext() {
        return this;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // remove
        View removeView = LayoutInflater.from(this).inflate(R.layout.dialog_remove_app, null);
        String app = appAdapter.getItem(position);
        TextView textView = removeView.findViewById(R.id.textApp);
        textView.setText(app);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("移除app")
                .setView(removeView)
                .setNegativeButton("移除", (dialog, which) -> removeApp(app))
                .setPositiveButton("取消", (DialogInterface dialog, int which) -> {
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
        intent.putExtra(MethodListActivity.APP_NAME,app);
        startActivity(intent);
    }
}
