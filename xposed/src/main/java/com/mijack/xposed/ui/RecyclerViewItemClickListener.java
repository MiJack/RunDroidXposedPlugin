package com.mijack.xposed.ui;

import android.view.View;

/**
 * @author Mi&Jack
 * @since 2018/8/4
 */
public interface RecyclerViewItemClickListener<T> {
    void onItemClick(View view, int position, T item);
}
