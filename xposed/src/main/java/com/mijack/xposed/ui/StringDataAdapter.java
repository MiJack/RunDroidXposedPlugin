package com.mijack.xposed.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mijack.xposed.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Mi&Jack
 */
public class StringDataAdapter extends BaseAdapter {
    private List<String> data;

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return System.identityHashCode(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_layout, parent, false);
        }
        TextView tv = convertView.findViewById(R.id.itemName);
        tv.setText(getItem(position));
        return convertView;
    }

    public void setData(Collection<String> data) {
        if (this.data == null) {
            this.data = new ArrayList();
        } else {
            this.data.clear();
        }
        this.data.addAll(data);
        Collections.sort(this.data);
        this.notifyDataSetChanged();
    }

    public void addData(String app) {
        if (this.data == null) {
            this.data = new ArrayList();
        }
        if (this.data.contains(app)) {
            return;
        }
        this.data.add(app);
        Collections.sort(this.data);
        this.notifyDataSetChanged();
    }

    public void removeData(String app) {
        if (this.data == null) {
            this.data = new ArrayList();
        }
        if (this.data.contains(app)) {
            this.data.remove(app);
            this.notifyDataSetChanged();
        }
    }

    public void addDatas(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (this.data == null) {
            this.data = new ArrayList();
        }

        for (String app : list) {
            if (!this.data.contains(app)) {
                this.data.add(app);
            }
        }
        Collections.sort(this.data);
        this.notifyDataSetChanged();
    }
}
