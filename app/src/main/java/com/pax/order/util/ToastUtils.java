/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2017 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 17-11-3 上午9:56  	     zenglc           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.R;

public class ToastUtils {

    /**
     * 之前显示的内容
     */
    private static String oldMsg;
    /**
     * Toast对象
     */
    private static Toast toast = null;
    /**
     * 第一次时间
     */
    private static long oneTime = 0;
    /**
     * 第二次时间
     */
    private static long twoTime = 0;
    private static boolean firstFlag = true;

    public static void showMessage(Context context, String message) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.toast_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.message);
        if (firstFlag) {
            textView.setText(message);
            toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);// 设置Toast屏幕居中显示
            toast.setView(view);
            toast.show();
            oneTime = System.currentTimeMillis();
            firstFlag = false;
        } else {
            twoTime = System.currentTimeMillis();

            if (message.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_LONG) {
                    toast.show();
                }
            } else {
                oldMsg = message;
                textView.setText(message);
                toast.setView(view);
                toast.show();
            }
        }

        oneTime = twoTime;
    }


    @SuppressLint("InflateParams")
    public static void showMessage(Context context, CharSequence text, int duration) {
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflate.inflate(R.layout.toast_layout, null);

        TextView textView = (TextView) view.findViewById(R.id.message);
        textView.setText(text);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);//设置Toast屏幕居中显示
        toast.setView(view);
        toast.show();
    }
}

