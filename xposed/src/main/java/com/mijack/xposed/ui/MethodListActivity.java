package com.mijack.xposed.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.mijack.xposed.XposedUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mi&Jack
 */
public class MethodListActivity extends Activity implements AdapterView.OnItemLongClickListener {
    public static final String APP_NAME = "APP_NAME";
    ListView listView;
    StringDataAdapter methodNameAdapter = new StringDataAdapter();
    private String[] methodList = new String[]{
            "android.app.Activity onCreate android.os.Bundle",
            "android.app.Activity onStart",
            "android.app.Activity onResume",
            "android.app.Activity onPause",
            "android.app.Activity onStop",
            "android.app.Activity onDestroy",
            "java.lang.Thread start",
            "android.view.View setOnClickListener android.view.View$OnClickListener"
    };
    private String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_list);
        appName = getIntent().getStringExtra(APP_NAME);
        setTitle(appName);

        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> appList = sharedPreferences.getStringSet(appName, new HashSet<String>());
        listView = findViewById(R.id.method_list);

        methodNameAdapter.setData(appList);
        listView.setAdapter(methodNameAdapter);
        listView.setOnItemLongClickListener(this);
    }

    private SharedPreferences getSharedPreferences() {
        String preferenceName = XposedUtils.getPreferenceName();
        return getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_method, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset_methods:
                showResetMethodsDialog();
                return true;
            case R.id.menu_add_method:
                showAddMethodDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResetMethodsDialog() {
        StringBuffer sb = new StringBuffer();
        for (String s : methodList) {
            sb.append(s).append("\n");
        }
        new AlertDialog.Builder(getContext()).setTitle("添加系统默认拦截方法")
                .setMessage(sb.toString())
                .setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addMethodNames(Arrays.asList(methodList));
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();

    }

    private void showAddMethodDialog() {
        final View addView = LayoutInflater.from(this).inflate(R.layout.dialog_add_method, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("添加方法名")
                .setView(addView)
                .setNegativeButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (addView == null) {
                            return;
                        }
                        EditText editText = addView.findViewById(R.id.editTextApp);
                        if (TextUtils.isEmpty(editText.getText())) {
                            Toast.makeText(MethodListActivity.this, "请输入方法名", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        List<String> methodNames = new ArrayList<>();
                        for (String methodName : editText.getText().toString().split(";")) {
                            methodNames.add(methodName);
                        }
                        addMethodNames(methodNames);
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }

    private void addMethodNames(List<String> methodNames) {
        methodNameAdapter.addDatas(methodNames);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> methodNameList = sharedPreferences.getStringSet(appName, new HashSet<String>());
        methodNameList.addAll(methodNames);
        sharedPreferences.edit().putStringSet(appName, methodNameList).apply();
    }

    private void removeMethodName(String methodName) {
        methodNameAdapter.removeData(methodName);
        SharedPreferences sharedPreferences = getSharedPreferences();
        Set<String> methodNameList = sharedPreferences.getStringSet(appName, new HashSet<String>());
        methodNameList.remove(methodName);
        sharedPreferences.edit().putStringSet(appName, methodNameList).apply();
    }

    public Context getContext() {
        return this;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // remove
        View removeView = LayoutInflater.from(this).inflate(R.layout.dialog_remove_method, null);
        final String methodName = methodNameAdapter.getItem(position);
        TextView textView = removeView.findViewById(R.id.textApp);
        textView.setText(methodName);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("移除方法名")
                .setView(removeView)
                .setNegativeButton("移除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeMethodName(methodName);
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
}
