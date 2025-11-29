package com.erudito.mainstaypeople.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by nonstop on 30/10/17.
 */

public class CustomGridLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGridLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}