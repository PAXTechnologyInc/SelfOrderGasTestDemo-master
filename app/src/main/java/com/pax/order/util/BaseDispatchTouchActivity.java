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
 * 2018/8/27 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.util;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.pax.order.FinancialApplication;
import com.pax.order.adver.GuideActivity;
import com.pax.order.ParamConstants;
import com.pax.order.logger.AppLog;

import java.io.File;

public class BaseDispatchTouchActivity extends BaseActivity {
    private CountDownTimer mCountTimerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void timeStart() {
        new Handler(getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mCountTimerView.start();
            }
        });
    }

    private void init() {
        String standby_time = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
                .getString(ParamConstants.STANDBY_TIME, "30");
        AppLog.e("BaseDispatchTouch", "standby_time = " + standby_time);


        //初始化CountTimer，设置倒计时为1分钟。
        mCountTimerView = new CountDownTimer(Integer.parseInt(standby_time) * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                File fileDr = new File(FinancialApplication.getApp().getFilesDir().getPath() + "/AdImage/");
                if (fileDr.exists()) {
                    if (fileDr.list().length > 0) {
                        startActivity(GuideActivity.class);
//                        finish();
                    }
                }
            }
        };
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
                mCountTimerView.start();
                break;
            //否则其他动作计时取消
            default:
                mCountTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCountTimerView.cancel();
    }

    @Override
    protected void onResume() {

        super.onResume();
        timeStart();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
}
