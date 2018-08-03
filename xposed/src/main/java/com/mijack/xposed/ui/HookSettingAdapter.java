package com.mijack.xposed.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mijack.xposed.R;

import java.util.List;

/**
 * @author Mi&Jack
 * @since 2018/8/4
 */
public class HookSettingAdapter extends RecyclerView.Adapter<HookSettingAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener {
    private List<AppHookSetting> appHookSettings;

    public HookSettingAdapter(HookSetting hookSetting) {
        this.appHookSettings = hookSetting.getList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_hook_setting, null);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.switcher.setOnCheckedChangeListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppHookSetting appHookSetting = appHookSettings.get(position);
        holder.hookName.setText(appHookSetting.name);
        holder.switcher.setChecked(appHookSetting.selected);
        List<String> methodSignList = appHookSetting.getMethodSignList();
        holder.switcher.setTag(R.integer.position, position);
        if (methodSignList.isEmpty()) {
            holder.methodList.setText("无专属hook方法");
        } else {
            StringBuffer sb = new StringBuffer("专属hook方法:" + methodSignList.size());
            for (String methodSign : methodSignList) {
                sb.append("\n").append(methodSign);
            }
            holder.methodList.setText(sb.toString());
        }
    }

    @Override
    public int getItemCount() {
        return appHookSettings != null ? appHookSettings.size() : 0;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Object tag = buttonView.getTag(R.integer.position);
        if (!(tag instanceof Integer)) {
            return;
        }
        int position = (int) tag;
        appHookSettings.get(position).setSelected(isChecked);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView hookName;
        public CheckBox switcher;
        public TextView methodList;

        public ViewHolder(View itemView) {
            super(itemView);
            hookName = itemView.findViewById(R.id.hookName);
            switcher = itemView.findViewById(R.id.switcher);
            methodList = itemView.findViewById(R.id.methodList);
        }
    }
}
