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
 * 18-8-13 下午2:22           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.print;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;

import java.util.List;

public class PrintPreviewActivity extends BaseActivity implements View.OnClickListener {
    //放大倍数，乃经验值
    private static final double PRNTIME_MULTI = 1.8;
    private static final int START_PRN = 0;
    private static final int PRN_SUCC = 100;
    private static final int PRN_NOPAPER = 2;
    private static final int PRN_BROKEN = 4;
    private static final int PRN_LOWVOL = 9;
    private static final int PRN_COVEROPEN = -5;
    private Button mCancelButton;
    private Button mPrintButton;
    private ImageView mPrintInfoImage;
    private Bitmap printBitMap;
    private Animation receiptOutAnim;
    private ScrollView mPrintScrollView;
    private List<String> mPrnInfoList;
    private int mPrnInfoTotal;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START_PRN:
                    mPrintScrollView.scrollTo(0, 0);
                    mPrintInfoImage.startAnimation(receiptOutAnim);
                    break;
                case PRN_SUCC:
                    //没有打印完，还有其他凭条
                    mPrintButton.setEnabled(true);
                    mCancelButton.setEnabled(true);
                    if (mPrnInfoTotal > 0) {
                        mPrintInfoImage.clearAnimation();
                        reLoadPrintView();
                    } else {
                        finish();
                    }
                    break;
                case PRN_NOPAPER:
                case PRN_BROKEN:
                case PRN_LOWVOL:
                case PRN_COVEROPEN:
                    mPrintInfoImage.clearAnimation();
                    dispErrorMsg(msg.what);
                    break;
                default:
                    mCancelButton.setEnabled(true);
                    mPrintButton.setEnabled(true);
                    break;
            }
        }
    };

    private void dispErrorMsg(int msgCode) {
        MStatusDialog statusDialog = new MStatusDialog(this, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                //recover
                mCancelButton.setEnabled(true);
                mPrintButton.setEnabled(true);
            }
        }).build());

        switch (msgCode) {
            case PRN_NOPAPER:
                statusDialog.show(getString(R.string.prn_nopaper), this.getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
                break;
            case PRN_BROKEN:
                statusDialog.show(getString(R.string.prn_broken), this.getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
                break;
            case PRN_LOWVOL:
                statusDialog.show(getString(R.string.prn_lowpow), this.getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
                break;
            case PRN_COVEROPEN:
                statusDialog.show(getString(R.string.prn_coveropen), this.getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
                break;
            default:
                break;
        }
    }

    private void reLoadPrintView() {
        printBitMap = null;
        if ((mPrnInfoTotal > 0) &&
                ((mPrnInfoList != null) && (mPrnInfoList.size() > 0))) {
            for (int i = 0; i < mPrnInfoTotal; i++) {
                if (mPrnInfoList.get(i).equals(PrintBitmapFactory.PAYPRN_NAME)) {
                    printBitMap = PrintBitmapFactory.getPrnBitmap(PrintBitmapFactory.PAYPRN_NAME);
                    mPrnInfoList.remove(i);
                    mPrnInfoTotal -= 1;
                    break;
                } else if (i == mPrnInfoTotal - 1) {
                    printBitMap = PrintBitmapFactory.getPrnBitmap(PrintBitmapFactory.ORDERPRN_NAME);
                    mPrnInfoList.remove(PrintBitmapFactory.ORDERPRN_NAME);
                    mPrnInfoTotal -= 1;
                }
            }
        }
        if (printBitMap != null) {
            mPrintInfoImage.setImageBitmap(printBitMap);
            int printImageHeight = printBitMap.getHeight();
            receiptOutAnim.setDuration((long) (printImageHeight * PRNTIME_MULTI));
            mPrintScrollView.scrollTo(0, 0);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPrnInfoList = intent.getStringArrayListExtra(PrintBitmapFactory.RPRN_TAG);
        if ((mPrnInfoList != null) && (mPrnInfoList.size() != 0)) {
//            mPrnInfoTotal = mPrnInfoList.size();
//            for (int i = 0; i < mPrnInfoTotal; i++) {
//                if (mPrnInfoList.get(i).equals(PrintBitmapFactory.PAYPRN_NAME)) {
//                    printBitMap = PrintBitmapFactory.getPrnBitmap(PrintBitmapFactory.PAYPRN_NAME);
//                    mPrnInfoList.remove(i);
//                    mPrnInfoTotal -= 1;
//                    break;
//                } else if (i == mPrnInfoTotal - 1) {
//                    printBitMap = PrintBitmapFactory.getPrnBitmap(PrintBitmapFactory.ORDERPRN_NAME);
//                    mPrnInfoList.remove(PrintBitmapFactory.ORDERPRN_NAME);
//                    mPrnInfoTotal -= 1;
//                }
//            }
            for (String prnString : mPrnInfoList) {
                printBitMap = PrintBitmapFactory.getPrnBitmap(prnString);
                mPrnInfoList.remove(prnString);
            }
        }
        String prnBitmapname = intent.getStringExtra(PrintBitmapFactory.RPRN_TAG);
        if ((prnBitmapname != null) && (prnBitmapname.length() != 0)) {
            printBitMap = PrintBitmapFactory.getPrnBitmap(prnBitmapname);
        }
        if (printBitMap != null) {
            initView();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initView() {
        setContentView(R.layout.activity_print_preview);
        TextView mTitleView = (TextView) findViewById(R.id.header_title);
        mTitleView.setText(R.string.prn_header);
        ImageView headerBack = (ImageView) findViewById(R.id.header_back);
        headerBack.setVisibility(View.GONE);
        mPrintInfoImage = (ImageView) findViewById(R.id.printPreview);
        mPrintInfoImage.setImageBitmap(printBitMap);
        mCancelButton = (Button) findViewById(R.id.cancel_button);
        mPrintButton = (Button) findViewById(R.id.print_button);
        mCancelButton.setOnClickListener(this);
        mPrintButton.setOnClickListener(this);
        mPrintScrollView = (ScrollView) findViewById(R.id.print_scrollview);
        receiptOutAnim = AnimationUtils.loadAnimation(this, R.anim.receipt_out);
        //放大倍数，乃经验值
        int printImageHeight = printBitMap.getHeight();
        receiptOutAnim.setDuration((long) (printImageHeight * PRNTIME_MULTI));
//        receiptOutAnim.setDuration(byPix*2);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.print_button:
                mCancelButton.setEnabled(false);
                mPrintButton.setEnabled(false);

                FinancialApplication.getApp().runInBackground(new Runnable() {
                    @Override
                    public void run() {
                        new Printer(handler).printBitmap(printBitMap);
                    }
                });
                break;
            case R.id.cancel_button:
                if (mPrnInfoTotal > 0) {
                    reLoadPrintView();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }

    }

}
