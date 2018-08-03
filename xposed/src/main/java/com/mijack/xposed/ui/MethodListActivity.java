package com.mijack.xposed.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import com.mijack.xposed.R;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Mi&Jack
 */
public class MethodListActivity extends BaseActivity {
    public static final String APP_NAME = "APP_NAME";
    ListView listView;
    StringDataAdapter methodNameAdapter = new StringDataAdapter();
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
    }

}
