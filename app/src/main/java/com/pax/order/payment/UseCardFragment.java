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
 * 2018/8/15 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.payment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.commonui.segmentedbarview.Segment;
import com.pax.order.commonui.segmentedbarview.SegmentedBarView;
import com.pax.order.commonui.segmentedbarview.SegmentedBarViewSideStyle;
import com.pax.order.db.PrintDataDb;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.Order;
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.PayData;
import com.pax.order.entity.PrintData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.entity.Transaction;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;
import com.pax.order.pay.PayActivity;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.order.print.PrintBitmapFactory;
import com.pax.order.print.PrintPreviewActivity;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.IView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UseCardFragment extends BaseFragment implements UseCardContract.View {
    private static final String TRANSTYPE = "transType";
    private static final String SALE_AMOUNT = "saleAmout";
    private static final String SALE_TAXAMOUNT = "saleTaxAmout";
    private static final String SALE_TIPAMOUNT = "saleTipAmout";

    private static final String IS_PAYING = "#169BD5";
    private static final String BEEN_PAID = "#FFCB0E";
    private static final String NOT_PAID = "#F1EFED";
    private static final int PRN_CODE = 1001;
    private static final int SALE_CODE = 1002;
    private static final String TAG = UseCardFragment.class.getSimpleName();
    private PaymentActivity mPaymentActivity;

    private String mTotalAmt;
    private String mTaxAmt;
    private String mTipAmt;
    private String mSubAmt;
    private String mSplitSubAmt;//不分单支付时=mSubAmt


    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");
    private LinearLayout mUseCardLayOut;

    private UseCardPresenter mUseCardPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPaymentActivity = (PaymentActivity) mActivity;
        mTaxAmt = mDecimalFormat.format(mPaymentActivity.getTax());
        mTotalAmt = mDecimalFormat.format(mPaymentActivity.getNeedPayAmout());
        mTipAmt = mDecimalFormat.format(mPaymentActivity.getTip());
        mSubAmt = mDecimalFormat.format(mPaymentActivity.getSubTotal());
        mSplitSubAmt = mDecimalFormat.format(mPaymentActivity.getmSubSplitPay());

        mUseCardPresenter.start();
    }

    @Override
    public void dealPayFailResult() {
        if (PayData.getInstance().isIfSplit()) {
            if (PayData.getInstance().getSplitType() == SplitType.TWO) {
                if (PayData.getInstance().getSplitStep() != SplitStep.TWO) {
                    //跳转到add tip
                    mPaymentActivity.setTabSelected(3);
                }
            } else if (PayData.getInstance().getSplitType() == SplitType.THREE) {
                if (PayData.getInstance().getSplitStep() != SplitStep.THREE) {
                    //跳转到add tip
                    mPaymentActivity.setTabSelected(3);
                }
            } else if (null != FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods()) {
                //跳转到add tip
                mPaymentActivity.setTabVisibility(2, View.VISIBLE);
                mPaymentActivity.setTabSelected(2);
            }
        } else {
            mPaymentActivity.setTabSelected(3);
        }
    }

    private void decideCallWaiter(final Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Other Choice");
        dialog.setMessage(getString(R.string.pay_fail));
        dialog.setCancelable(false);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                OrderInstance.getInstance().asyncSendNotification("1");
                //3S之后关闭进度框并退出应用,恢复导航功能
                MProgressDialog.showProgress(context, "Auto exit...", new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        FinancialApplication.finishAll();
                    }
                }).build(), 3000);
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PRN_CODE:
                if (PayData.getInstance().isIfSplit()) {

                    if (PayData.getInstance().getSplitType() == SplitType.TWO) {
                        if (PayData.getInstance().getSplitStep() != SplitStep.TWO) {
                            //跳转到add tip
                            mPaymentActivity.setTabSelected(3);
                        }
                    } else if (PayData.getInstance().getSplitType() == SplitType.THREE) {
                        if (PayData.getInstance().getSplitStep() != SplitStep.THREE) {
                            //跳转到add tip
                            mPaymentActivity.setTabSelected(3);
                        }
                    } else if (null != FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods()) {
                        //跳转到add tip
                        mPaymentActivity.setTabVisibility(2, View.VISIBLE);
                        mPaymentActivity.setTabSelected(2);
                    }
                } else {
                    mPaymentActivity.finish();
                }
                break;

            case SALE_CODE:
                IPayResponse payResponse = (IPayResponse) data.getSerializableExtra(IBaseResponse.KEY);
                mUseCardPresenter.dealPayFlow(payResponse);
                break;

            default:
                break;
        }
    }

    @Override
    public void startPay() {
        Intent intent;
        PayData.getInstance().setIfPaymentProcess(true);
        intent = new Intent(mPaymentActivity, PayActivity.class);
        intent.putExtra(TRANSTYPE, RequestVar.TRANS_SALE);
        intent.putExtra(SALE_AMOUNT, mTotalAmt.replaceAll("\\D", ""));
        intent.putExtra(SALE_TAXAMOUNT, mTaxAmt.replaceAll("\\D", ""));
        intent.putExtra(SALE_TIPAMOUNT, mTipAmt.replaceAll("\\D", ""));

        startActivityForResult(intent, SALE_CODE);
    }

    @Override
    public void retryPay(int payCounter, String messageText) {
        if (payCounter < 3) {
            payCounter++;
            mUseCardPresenter.setPayCounter(payCounter);
            AlertDialog.Builder dialog = new AlertDialog.Builder(mPaymentActivity);
            if (messageText != null) {
                dialog.setTitle(messageText);
            } else {
                dialog.setTitle("Pay Failed!");
            }

            dialog.setMessage("Please re-pay" + "(" + String.valueOf(payCounter) + "/3)");
            dialog.setCancelable(false);
            dialog.setPositiveButton("Re-Pay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startPay();
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mUseCardPresenter.setPayCounter(0);
                    boolean ifSplit = PayData.getInstance().isIfSplit();
                    SplitStep splitStep = PayData.getInstance().getSplitStep();
                    if ((ifSplit) && (splitStep != SplitStep.ZERO)) {
                        dealPayFailResult();
                    } else {
                        mPaymentActivity.finish();
                    }
                }
            });
            dialog.show();
        } else {
            if ((PayData.getInstance().isIfSplit()) && (PayData.getInstance().getSplitStep() != SplitStep.ZERO)) {
                decideCallWaiter(mPaymentActivity);
            } else {
                new MStatusDialog(mPaymentActivity, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        mPaymentActivity.finish();
                    }
                }).build()).show(getString(R.string.pay_fail), getResources().getDrawable(R.drawable.mn_icon_dialog_warn), 3000);
            }
        }
    }

    @Override
    public void startPrint(List<String> prnInfoList) {
        Intent intent = new Intent(mPaymentActivity, PrintPreviewActivity.class);
        intent.putStringArrayListExtra(PrintBitmapFactory.RPRN_TAG, (ArrayList<String>) prnInfoList);
        startActivityForResult(intent, PRN_CODE);
    }

    @Override
    public void finishView() {
        mPaymentActivity.finish();
    }

    @Override
    public String getTotalAmt() {
        return mTotalAmt;
    }

    @Override
    public String getTax() {
        return mTaxAmt;
    }

    @Override
    public String getTip() {
        return mTipAmt;
    }

    @Override
    public String getBaseAmt() {
        return mSubAmt;
    }

    @Override
    public String getUpLoadAmt() {
        return  mDecimalFormat.format(mPaymentActivity.getNeedPayAmout()-mPaymentActivity.getTip());
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_use_card;
    }

    @Override
    protected void initView(View view) {

        ImageView useCardView = (ImageView) view.findViewById(R.id.use_card);
        //Map<ETermInfoKey, String> eTermInfoKey = FinancialApplication.getDal().getSys().getTermInfo();
        //String model = eTermInfoKey.get(ETermInfoKey.MODEL);
        String model = "A920";
        if (model.equals("A930") || model.equals("A920") || model.equals("A920C")) {
            Glide.with(UseCardFragment.this).load(R.drawable.use_card_no_q20)
                    .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(useCardView);
        } else {
            Glide.with(UseCardFragment.this).load(R.drawable.use_card_ani)
                    .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(useCardView);
        }
        mUseCardLayOut = (LinearLayout) view.findViewById(R.id.use_card_layout);
    }

    @Override
    public void displayPayStatus(SplitType splitType, SplitStep splitStep) {
        ArrayList<Segment> segments = new ArrayList<>();
        String[] splitAmt = new String[3];

        if (splitType == SplitType.TWO) {
            if (splitStep == SplitStep.ZERO) {
                splitAmt[0] = mTotalAmt;
                PayData.getInstance().setSplitAmt(splitAmt);
                Segment segment = new Segment("$" + mTotalAmt, null, Color.parseColor(IS_PAYING));
                segments.add(segment);
                Segment segment2 = new Segment("waiting", null, Color.parseColor(NOT_PAID));
                segments.add(segment2);
            } else if (splitStep == SplitStep.ONE) {
                splitAmt = PayData.getInstance().getSplitAmt();
                splitAmt[1] = mTotalAmt;
                PayData.getInstance().setSplitAmt(splitAmt);
                Segment segment = new Segment("$" + splitAmt[0], null, Color.parseColor(BEEN_PAID));
                segments.add(segment);
                Segment segment2 = new Segment("$" + mTotalAmt, null, Color.parseColor(IS_PAYING));
                segments.add(segment2);
            }
        }
        if (splitType == SplitType.THREE) {
            if (splitStep == SplitStep.ZERO) {
                splitAmt[0] = mTotalAmt;
                PayData.getInstance().setSplitAmt(splitAmt);
                Segment segment = new Segment("$" + mTotalAmt, null, Color.parseColor(IS_PAYING));
                segments.add(segment);
                Segment segment2 = new Segment("waiting", null, Color.parseColor(NOT_PAID));
                segments.add(segment2);
                Segment segment3 = new Segment("waiting", null, Color.parseColor(NOT_PAID));
                segments.add(segment3);
            } else if (splitStep == SplitStep.ONE) {
                splitAmt = PayData.getInstance().getSplitAmt();
                splitAmt[1] = mTotalAmt;
                PayData.getInstance().setSplitAmt(splitAmt);
                Segment segment = new Segment("$" + splitAmt[0], null, Color.parseColor(BEEN_PAID));
                segments.add(segment);
                Segment segment2 = new Segment("$" + mTotalAmt, null, Color.parseColor(IS_PAYING));
                segments.add(segment2);
                Segment segment3 = new Segment("waiting", null, Color.parseColor(NOT_PAID));
                segments.add(segment3);
            } else if (splitStep == SplitStep.TWO) {
                splitAmt = PayData.getInstance().getSplitAmt();
                splitAmt[2] = mTotalAmt;
                PayData.getInstance().setSplitAmt(splitAmt);
                Segment segment = new Segment("$" + splitAmt[0], null, Color.parseColor(BEEN_PAID));
                segments.add(segment);
                Segment segment2 = new Segment("$" + splitAmt[1], null, Color.parseColor(BEEN_PAID));
                segments.add(segment2);
                Segment segment3 = new Segment("$" + mTotalAmt, null, Color.parseColor(IS_PAYING));
                segments.add(segment3);
            }
        }

        SegmentedBarView barView = new SegmentedBarView(mPaymentActivity);
        barView.setSideStyle(SegmentedBarViewSideStyle.NORMAL);
        barView.setSegments(segments);
        if (splitType == SplitType.TWO) {
            if (splitStep == SplitStep.ZERO) {
                barView.setValueSegment(0);
                barView.setValueSegmentText("1 Paying...");
            } else if (splitStep == SplitStep.ONE) {
                barView.setValueSegment(1);
                barView.setValueSegmentText("2 Paying...");
            }
        }
        if (splitType == SplitType.THREE) {
            if (splitStep == SplitStep.ZERO) {
                barView.setValueSegment(0);
                barView.setValueSegmentText("1 Paying...");
            } else if (splitStep == SplitStep.ONE) {
                barView.setValueSegment(1);
                barView.setValueSegmentText("2 Paying...");
            } else if (splitStep == SplitStep.TWO) {
                barView.setValueSegment(2);
                barView.setValueSegmentText("3 Paying...");
            }
        }

        barView.setBarHeight(60);
        barView.setSegmentTextSize(40);
        barView.setValueSignSize(150, 60);
        barView.setValueTextSize(30);
        barView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        barView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.vertical_padding), 0, 0);
        mUseCardLayOut.addView(barView);

        FinancialApplication.getPayDataDb().savePayData(PayData.getInstance());
    }

    @Override
    protected void bindEvent() {
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        mUseCardPresenter = new UseCardPresenter((UseCardContract.View) this);
        return mUseCardPresenter;
    }

    @Override
    public void setPresenter(UseCardContract.Presenter presenter) {

    }
}
