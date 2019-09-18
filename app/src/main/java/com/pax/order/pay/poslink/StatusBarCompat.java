package com.pax.order.pay.poslink;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.ViewGroup;

public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    public static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }
    }

    public static void setRootViewProperty(ViewGroup rootView) {
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

}
