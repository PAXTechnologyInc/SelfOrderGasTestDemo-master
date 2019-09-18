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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pax.order.R;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.posdk.PaxTransLink;
import com.pax.posdk.TransType;
import com.pax.posdk.request.TransBatchRequest;
import com.pax.posdk.request.TransPaymentRequest;

public class PosdkPaymentImpl implements Pay.Payment {

    private Pay.PayListener mPayListener = null;
    private FragmentManager mFragmentManager = null;

    private void startPosdkPayFragment(Context context) {
        PosdkPayMessageFragment newFragment = new PosdkPayMessageFragment(mPayListener);

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.commitAllowingStateLoss();

    }

    @Override
    public boolean doPayment(Context context, RequestVar requestVar) {

        TransPaymentRequest transPaymentRequest = PosdkPayListener.getInstance().getTransPaymentRequest();
        transPaymentRequest.setTenderType(requestVar.getTendType());
        transPaymentRequest.setTipAmt(requestVar.getTipAmount());
//        if ((Integer.parseInt(requestVar.getTaxAmount()) == 0) || (requestVar.getTaxAmount() == null)) {
//            transPaymentRequest.setTaxAmt("0");
//        } else {
//            transPaymentRequest.setTaxAmt(requestVar.getTaxAmount());
//        }
        //将税金和消费金额合并
        long amount = 0;
        amount = Long.parseLong(requestVar.getAmount()) - Long.parseLong(requestVar.getTipAmount());
        transPaymentRequest.setAmount(String.valueOf(amount));


//        transPaymentRequest.setTenderType("DEBIT");
        transPaymentRequest.setTransType(TransType.Payment.SALE);
        transPaymentRequest.setECRRefNum(requestVar.getEcrRefNum());
        startPosdkPayFragment(context);
        PosdkAsncPayTask posdkAsncPayTask = PosdkAsncPayTask.getInstance(context, mPayListener);
        posdkAsncPayTask.execute(transPaymentRequest);
        return true;
    }

    @Override
    public void doCommSetting(Context context, boolean isUSB) {
        PaxTransLink.init(context);
    }

    @Override
    public void abortTrans() {
        PaxTransLink.getInstance().abortTrans();
    }

    @Override
    public boolean doBatch(Context context) {
        TransBatchRequest transBatchRequest = PosdkBatchListener.getInstance()
                .getTransBatchRequest();

        transBatchRequest.setTransType(TransType.Batch.BATCHCLOSE);
        startPosdkPayFragment(context);
        PosdkAsncPayTask posdkAsncPayTask = PosdkAsncPayTask.getInstance(context, mPayListener);
        posdkAsncPayTask.execute(transBatchRequest);

        return true;
    }

    @Override
    public void setPayListener(Pay.PayListener listener) {
        mPayListener = listener;
    }

    @Override
    public void setFragmentMannager(FragmentManager fragmentMannager) {
        mFragmentManager = fragmentMannager;
    }
}
