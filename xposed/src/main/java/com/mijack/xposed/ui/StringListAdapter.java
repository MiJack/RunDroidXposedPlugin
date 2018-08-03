package com.mijack.xposed.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mijack.xposed.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Mi&Jack
 */
public class StringListAdapter extends RecyclerView.Adapter<StringListAdapter.ViewHolder> implements View.OnClickListener {
    private List<String> data;
    private RecyclerViewItemClickListener<String> listener;

    public StringListAdapter(RecyclerViewItemClickListener<String> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout, null);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(data.get(position));
        holder.itemView.setTag(R.integer.position, position);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setData(Collection<String> data) {
        if (this.data == null) {
            this.data = new ArrayList();
        } else {
            this.data.clear();
        }
        this.data.addAll(data);
        Collections.sort(this.data, String.CASE_INSENSITIVE_ORDER);
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (listener == null) {
            return;
        }
        Object tag = v.getTag(R.integer.position);
        if (!(tag instanceof Integer)) {
            return;
        }
        int position = (int) tag;
        listener.onItemClick(v, position, data.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemName);
        }

        public void setData(String data) {
            textView.setText(data);
        }
    }
}
