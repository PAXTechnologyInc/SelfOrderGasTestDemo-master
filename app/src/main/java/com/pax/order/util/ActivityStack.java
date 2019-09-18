/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/10/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.app.Activity;

import java.util.Deque;
import java.util.LinkedList;

import com.pax.order.logger.AppLog;

public class ActivityStack {
    private static final String TAG = "ActivityStack";

    private Deque<Activity> activities;
    private static ActivityStack instance;

    private ActivityStack() {
        activities = new LinkedList<Activity>();
    }

    public static ActivityStack getInstance() {
        if (instance == null)
            instance = new ActivityStack();

        return instance;
    }

    public void pop() {
        try {
            Activity activity = top();
            if (activity != null) {
                remove(activity);
            }
        } catch (Exception e) {
            AppLog.w(TAG, e);
        }
    }

    /**
     * 从栈的后面开始删除，直到删除自身界面为止
     *
     * @param activity the specific activity
     */
    public void popToTop(Activity activity) {
        if (activity != null) {
            while (true) {
                Activity lastCurrent = top();
                if (activity == top()) {
                    return;
                }
                remove(lastCurrent);
            }
        }
    }

    /**
     * 从栈的前面开始删除，直到删除自身界面为止
     *
     * @param activity the specific activity
     */
    public void popToBottom(Activity activity) {
        if (activity != null) {
            while (true) {
                Activity fristCurrent = bottom();
                if (activity == bottom()) {
                    return;
                }
                remove(fristCurrent);
            }
        }
    }

    /**
     * 除指定界面外，其它全部pop掉
     *
     * @param activity the specific activity
     */
    public void popAllButThis(Activity activity) {
        popToTop(activity);
        popToBottom(activity);
    }

    public Activity top() {
        try {
            return activities.getLast();
        } catch (Exception e) {
            AppLog.w(TAG, e);
        }
        return null;
    }

    public void push(Activity activity) {
        activities.addLast(activity);
    }

    // 查找栈中是否存在指定的activity
    public boolean find(Class<?> cls) {
        for (Activity activity : activities) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 除栈底外，其他pop掉
     */
    public void popAllButBottom() {
        while (true) {
            Activity topActivity = top();
            if (topActivity == null || topActivity == bottom()) {
                break;
            }
            remove(topActivity);
        }

    }

    /**
     * 结束所有栈中的activity
     */
    public void popAll() {
        if (activities == null) {
            return;
        }
        while (true) {
            Activity activity = top();
            if (activity == null) {
                break;
            }
            remove(activity);
        }
    }

    public Activity bottom() {
        return activities.getFirst();
    }

    private void remove(Activity activity) {
        activity.finish();
        activities.remove(activity);
    }
}
