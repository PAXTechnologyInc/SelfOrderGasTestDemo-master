/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 18-10-15 上午10:50           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.posdk;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class PosdkPayMessageFragment extends Fragment {

    private static final String TAG = PosdkPayMessageFragment.class.getSimpleName();
    private final int WARN_MSG = 1;
    private final int ERROR_MSG = 2;
    private final int PROGRESS_MSG = 3;
    private final int OK_MSG = 0;
    private Pay.PayListener listener;

    public PosdkPayMessageFragment() {
        // Required empty public constructor
        EventBus.getDefault().register(this);
    }

    @SuppressLint("ValidFragment")
    public PosdkPayMessageFragment(Pay.PayListener listener) {
        this.listener = listener;
        EventBus.getDefault().register(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final PosdkPayMessage posdkPayMessage) {

        AppLog.d(TAG, "onMessageEvent: " + posdkPayMessage.getResultMessage() + posdkPayMessage.isFinishProgressDialog() + posdkPayMessage.isFinishActivity());

        if (posdkPayMessage.isFinishProgressDialog()) {
            MProgressDialog.dismissProgress();
        }
        if ((posdkPayMessage.getResultMessage() != null)) {
            if (posdkPayMessage.getMessageType() == WARN_MSG) {
                AppLog.d(TAG, "onMessageEvent: WARN_MSG: " + posdkPayMessage.getResultMessage());
                new MStatusDialog(getActivity(), new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (posdkPayMessage.isFinishActivity()) {
                            AppLog.d(TAG, "onDismiss: WARNFINISH finish!");
                            listener.onResult(posdkPayMessage.getResponseVar());
                        }
                    }
                }).build()).show(posdkPayMessage.getResultMessage(), this.getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
            } else if (posdkPayMessage.getMessageType() == ERROR_MSG) {
                AppLog.d(TAG, "onMessageEvent: ERROR_MSG: " + posdkPayMessage.getResultMessage());
                new MStatusDialog(getActivity(), new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (posdkPayMessage.isFinishActivity()) {
                            listener.onResult(posdkPayMessage.getResponseVar());
                        }
                    }
                }).build()).show(posdkPayMessage.getResultMessage(), this.getResources().getDrawable(R.drawable.mn_icon_dialog_error));
            } else if (posdkPayMessage.getMessageType() == PROGRESS_MSG) {
                MProgressDialog.showProgress(getActivity(), posdkPayMessage.getResultMessage());
            } else if (posdkPayMessage.getMessageType() == OK_MSG) {
            }

        } else {
            if (posdkPayMessage.isFinishActivity()) {
                AppLog.d(TAG, "onMessageEvent: final finish");
                listener.onResult(posdkPayMessage.getResponseVar());
            }
        }

    }

}
