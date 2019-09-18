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

package com.pax.order.pay.edc;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pax.ecrsdk.factory.ITransAPI;
import com.pax.ecrsdk.factory.TransAPIFactory;
import com.pax.ecrsdk.message.BaseResponse;
import com.pax.ecrsdk.message.CommConnectMsg;
import com.pax.ecrsdk.message.RequestProxy;
import com.pax.ecrsdk.message.SaleMsg;
import com.pax.ecrsdk.message.SettleMsg;
import com.pax.order.R;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.pay.paydata.RequestVar;

public class EdcPaymentImpl implements Pay.Payment {
    private final static String TAG = EdcPayFragment.class.getSimpleName();
    private ITransAPI sITransAPI;
    private Pay.PayListener mPayListener = null;
    private RequestProxy mRequestVar;
    private FragmentManager mFragmentManager = null;

    public EdcPaymentImpl() {
        sITransAPI = TransAPIFactory.createTransAPI();
    }

    private RequestProxy requestVar2RequestProxy(RequestVar requestVar) {
        SaleMsg.Request request = new SaleMsg.Request();
        long amout = 0, taxAmount = 0, tipAmount = 0;

        if (null != requestVar.getAmount()) {
            amout = Long.parseLong(requestVar.getAmount());
        }
        if (null != requestVar.getTaxAmount()) {
            taxAmount = Long.parseLong(requestVar.getTaxAmount());
        }
        if (null != requestVar.getTipAmount()) {
            tipAmount = Long.parseLong(requestVar.getTipAmount());
        }

        request.setAmount(amout);
        request.setTipAmount(tipAmount);

        return new RequestProxy(request);
    }

    private void startEdcPayFragment(Context context) {
        EdcPayFragment newFragment = new EdcPayFragment();
        newFragment.setEdcPayment(this);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment, "EDCFRAGMENT");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean doPayment(Context context, RequestVar requestVar) {
        mRequestVar = requestVar2RequestProxy(requestVar);

        startEdcPayFragment(context);

        return true;
    }

    @Override
    public void doCommSetting(Context context, boolean isUSB) {
        CommConnectMsg.Request request = new CommConnectMsg.Request();
        request.setDefault(isUSB);
        sITransAPI.doTrans(context, new RequestProxy(request));
        return;
    }

    @Override
    public void abortTrans() {

    }

    @Override
    public boolean doBatch(Context context) {
        SettleMsg.Request request = new SettleMsg.Request();
        mRequestVar = new RequestProxy(request);
        startEdcPayFragment(context);

        return true;
    }

    @Override
    public void setPayListener(Pay.PayListener listener) {
        mPayListener = listener;
    }

    public void doTrans(Context context) {
        AppLog.i(TAG, "doTrans: start");
        sITransAPI.doTrans(context, mRequestVar);
        AppLog.i(TAG, "doTrans: end");
    }

    private static PayResponseVar getResponseVar(SaleMsg.Response baseResponse) {
        PayResponseVar payResponseVar = new PayResponseVar();

        payResponseVar.setResult(String.valueOf(baseResponse.getRspCode()));
        payResponseVar.setAuthCode(baseResponse.getAuthCode());
        payResponseVar.setBatchNum(String.valueOf(baseResponse.getBatchNo()));
        payResponseVar.setCardOrg(String.valueOf(baseResponse.getCardType()));
        payResponseVar.setTransDate(baseResponse.getTransTime());
        payResponseVar.setTransTime(baseResponse.getTransTime());
        payResponseVar.setCardNo(baseResponse.getCardNo());
        payResponseVar.setCardOrg(baseResponse.getIssuerName());

        return payResponseVar;
    }

    private static PayResponseVar getResponseVar(SettleMsg.Response baseResponse) {
        PayResponseVar payResponseVar = new PayResponseVar();

        payResponseVar.setResult(String.valueOf(baseResponse.getRspCode()));
        payResponseVar.setMessageText(baseResponse.getRspMsg());
        AppLog.i(TAG, "getResponseVar: " + baseResponse.getRspCode() + "," + baseResponse.getRspMsg());

        return payResponseVar;
    }


    public void dealEDCPayResult(int requestCode, int resultCode, Intent data) {
        BaseResponse baseResponse = null;
        PayResponseVar payResponseVar = null;

        baseResponse = sITransAPI.onResult(requestCode, resultCode, data);
        if (baseResponse != null) {
            if (baseResponse.getRspCode() == 0) {
                if (baseResponse instanceof SaleMsg.Response)
                    payResponseVar = getResponseVar((SaleMsg.Response) baseResponse);
                else if (baseResponse instanceof SettleMsg.Response) {
                    payResponseVar = getResponseVar((SettleMsg.Response) baseResponse);
                }

            } else {
                payResponseVar = new PayResponseVar();
                payResponseVar.setResult(String.valueOf(baseResponse.getRspCode()));
            }
        } else {
            payResponseVar = new PayResponseVar();
            payResponseVar.setResult(PayResponseVar.TRANS_FAIL);
        }
        mPayListener.onResult(payResponseVar);
    }

    @Override
    public void setFragmentMannager(FragmentManager fragmentMannager) {
        mFragmentManager = fragmentMannager;
    }
}
