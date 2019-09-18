/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * yyyymmdd  	         xiazp           	Create/Add/Modify/Delete
 * ===========================================================================================
 */

package com.pax.order.pay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.payInterfaceFactory.PayFactory;
import com.pax.order.pay.paydata.BatchResponseVar;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.IBatchResponse;
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.order.pay.poslink.PaymentReceiptActivity;
import com.pax.order.print.PrintBitmapFactory;
import com.pax.order.print.PrintPreviewActivity;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;
import com.pax.poslink.constant.EDCType;


public class PayActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PayActivity.class.getSimpleName();
    private static final String SALE_AMOUNT = "saleAmout";
    private static final String SALE_TAXAMOUNT = "saleTaxAmout";
    private static final String SALE_TIPAMOUNT = "saleTipAmout";
    private static final String TRANSTYPE = "transType";
    private static final int PRN_CODE = 300;
    private RequestVar mRequestVar;
    private PayResponseVar mPayResponseVar;
    private BatchResponseVar mBatchResponseVar;
    private ImageView mHeaderBack;
    private TextView tvHeaderTitle;
    private Pay.Payment sale;
    private FragmentManager mFragmentManager;


    private void setTransResult(IBaseResponse responseVar) {
        if (responseVar instanceof IPayResponse) {
            mPayResponseVar = (PayResponseVar) responseVar;
            setResult(0, new Intent().putExtra(IBaseResponse.KEY, mPayResponseVar));
        } else if (responseVar instanceof IBatchResponse) {
            mBatchResponseVar = (BatchResponseVar) responseVar;
            setResult(0, new Intent().putExtra(IBaseResponse.KEY, mBatchResponseVar));
        }

    }

    private void selectTendType() {
        final String[] tenderTypes = {EDCType.CREDIT, EDCType.DEBIT};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Select CARD Type");
        builder.setCancelable(false);
        builder.setItems(tenderTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mRequestVar.tendType = tenderTypes[i];
                sale.doPayment(PayActivity.this, mRequestVar);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IBaseResponse payResponse = new PayResponseVar();
                payResponse.setResult(IBaseResponse.TRANS_CANCEL);
                setTransResult(payResponse);
                finish();
            }
        });
        final AlertDialog alertDialog = builder.show();
    }

    private void saleTrans(String saleAmount, String saleTaxAmount, String saleTipAmount) {
        mRequestVar = new RequestVar();
        sale = PayFactory.getPaymentInstance();
        mRequestVar.setTaxAmount(saleTaxAmount);
        mRequestVar.setTransType(RequestVar.TRANS_SALE);
        mRequestVar.setTipAmount(saleTipAmount);
        mRequestVar.setAmount(saleAmount);
        mRequestVar.setEcrRefNum(String.valueOf(FinancialApplication.getEcrRefNum()));
        FinancialApplication.ecrRefNumAdd();

        sale.setPayListener(new Pay.PayListener() {
            @Override
            public void onResult(IBaseResponse responseVar) {
                setTransResult(responseVar);
                finish();
            }
        });
        sale.setFragmentMannager(mFragmentManager);
        //selectTendType();
    }

    private void battleTrans() {
        mRequestVar = new RequestVar();
        sale = PayFactory.getPaymentInstance();
        mRequestVar.setTransType(RequestVar.TRANS_BATCH);

        mRequestVar.setEcrRefNum(String.valueOf(FinancialApplication.getEcrRefNum()));
        FinancialApplication.ecrRefNumAdd();

        sale.setPayListener(new Pay.PayListener() {
            @Override
            public void onResult(IBaseResponse responseVar) {
                setTransResult(responseVar);
                if (responseVar.getResult().equals(IBaseResponse.TRANS_SUCESS)) {
                    Bitmap prnBitmap = PrintBitmapFactory.genPrnBitmap(responseVar);
                    PrintBitmapFactory.savePrnBitmap(PrintBitmapFactory.BATCHPRN_NAME, prnBitmap);
                    Intent intent = new Intent(PayActivity.this, PrintPreviewActivity.class);
                    intent.putExtra(PrintBitmapFactory.RPRN_TAG, PrintBitmapFactory.BATCHPRN_NAME);
                    startActivityForResult(intent, PRN_CODE);
                } else {
                    finish();
                }
            }
        });
        sale.setFragmentMannager(mFragmentManager);
        sale.doBatch(PayActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        mHeaderBack = (ImageView) findViewById(R.id.header_back);
        mHeaderBack.setOnClickListener(this);
        tvHeaderTitle = (TextView) findViewById(R.id.header_title);
        mFragmentManager = getSupportFragmentManager();

        Intent intent = getIntent();
        String transType = intent.getStringExtra(TRANSTYPE);
        if (transType.equals(RequestVar.TRANS_SALE)) {
            tvHeaderTitle.setText(getString(R.string.use_card));
            String saleAmount = intent.getStringExtra(SALE_AMOUNT);
            String saleTaxAmount = intent.getStringExtra(SALE_TAXAMOUNT);
            String saleTipAmount = intent.getStringExtra(SALE_TIPAMOUNT);
            if (Integer.parseInt(saleAmount) < 0) {
                new MStatusDialog(this, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        final String ERROR_AMOUNT = "3";
                        IBaseResponse responseVar = new PayResponseVar();
                        responseVar.setResult(ERROR_AMOUNT);
                        setTransResult(responseVar);
                        finish();
                    }
                }).build()).show(getString(R.string.error_amout), getResources().getDrawable(R.drawable.mn_icon_dialog_error));
            } else if (Integer.parseInt(saleAmount) == 0) {
                new MStatusDialog(this, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        IPayResponse responseVar = new PayResponseVar();
                        responseVar.setResult("0");
                        responseVar.setCardOrg("Free Dish");
                        responseVar.setCardNo("0000");
                        setTransResult((IBaseResponse) responseVar);
                        finish();
                    }
                }).build()).show(getString(R.string.zero_amout), getResources().getDrawable(R.drawable.mn_icon_dialog_ok));
            } else {
                saleTrans(saleAmount, saleTaxAmount, saleTipAmount);
            }
        } else if (transType.equals(RequestVar.TRANS_BATCH)) {
            tvHeaderTitle.setText("Batch");
            battleTrans();
        }
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                if (sale != null)
                    sale.abortTrans();
                break;
            default:
                break;
        }
    }

    private void printReceipt() {
        //String receipt = generateReceipt(poslink,mContext);
        Intent intent = new Intent(this, PaymentReceiptActivity.class);
        Bundle bundle = new Bundle();
        //bundle.putString("Payment_Receipt", receipt);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.i(TAG, "onActivityResult: " + requestCode);
        Fragment fragment = mFragmentManager.findFragmentByTag("EDCFRAGMENT");
        if (fragment != null)
            fragment.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PRN_CODE:
                finish();
                break;
            default:
                break;
        }
    }
}
