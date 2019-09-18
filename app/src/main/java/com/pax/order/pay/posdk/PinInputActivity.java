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
 * 18-9-29 上午9:28           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.posdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.logger.AppLog;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.ToastUtils;
import com.pax.posdk.PaxTransLink;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PinInputActivity extends BaseActivity implements PaxTransLink.EnterPinCallBack, PaxTransLink.CurrentStepCallback {
    static class PinInputMessage {
        private boolean isFinishAcitivity;

        public boolean isFinishAcitivity() {
            return isFinishAcitivity;
        }

        public void setFinishAcitivity(boolean finishAcitivity) {
            isFinishAcitivity = finishAcitivity;
        }
    }

    TextView mPromptTitle;
    TextView mPwdInputText;
    ImageView mHeadrBack;
    private static final String TAG = PinInputActivity.class.getSimpleName();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPinInputMessageEvent(PinInputMessage pinInputMessage) {
        if (pinInputMessage.isFinishAcitivity()) {
            finish();
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PinInputActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputpin);
        initViews();
        EventBus.getDefault().register(this);
        PaxTransLink.getInstance().handleInputPinStart(this, this);
    }

    private void initViews() {
        mPromptTitle = (TextView) findViewById(R.id.pininput_prompt_title);
        mPwdInputText = (TextView) findViewById(R.id.pin_input_text);
        mHeadrBack = (ImageView) findViewById(R.id.header_back);
        mHeadrBack.setVisibility(View.GONE);

        mPromptTitle.setText(getString(R.string.enter_pin));
        clearContentText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onSuccess() {
        AppLog.d(TAG, "onSuccess: ");
        finish();
    }

    @Override
    public void onFail(String code, String msg) {
        ToastUtils.showMessage(PinInputActivity.this, msg);
        initViews();
    }

    private void addContentText(final String content) {
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPwdInputText != null) {
                    mPwdInputText.setText(String.format("%s%s", mPwdInputText.getText(), content));
                    mPwdInputText.setTextSize(50f);
                }
            }
        });
    }

    @Override
    public void onAddedPinCharacter() {
        addContentText("*");
    }

    private void clearContentText() {
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mPwdInputText != null) {
                    mPwdInputText.setText("");
                    mPwdInputText.setTextSize(50f);
                }
            }
        });
    }


    @Override
    public void onClearPin() {
        clearContentText();
    }
}
