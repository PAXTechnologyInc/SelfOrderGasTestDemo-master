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
 * 18-10-9 下午2:55           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.posdk;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.paydata.BatchResponseVar;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.Tools;
import com.pax.order.util.XmlParse;
import com.pax.posdk.BatchListener;
import com.pax.posdk.PaxTransLink;
import com.pax.posdk.ResultConstant;
import com.pax.posdk.request.TransBatchRequest;
import com.pax.posdk.response.TransBatchResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class PosdkBatchListener implements BatchListener {
    private static final String TAG = PosdkBatchListener.class.getSimpleName();
    volatile private static PosdkBatchListener instance = null;
    private TransBatchRequest mTransBatchRequest;
    private final int WARN_MSG = 1;
    private final int ERROR_MSG = 2;
    private final int PROGRESS_MSG = 3;
    private final int OK_MSG = 0;

    private PosdkBatchListener() {
    }

    public static PosdkBatchListener getInstance() {
        if (null == instance) {
            synchronized (PosdkBatchListener.class) {
                if (null == instance) {
                    instance = new PosdkBatchListener();
                    instance.mTransBatchRequest = null;
                }
            }
        }
        return instance;
    }

    public TransBatchRequest getTransBatchRequest() {
        if (mTransBatchRequest == null) {
            mTransBatchRequest = new TransBatchRequest();
        }
        return mTransBatchRequest;
    }

    @Override
    public void onSelectAID(List<String> list) {
        AppLog.d(TAG, "onSelectAID: ");
    }

    @Override
    public void onIsContinueBatchClose() {
        AppLog.d(TAG, "onIsContinueBatchClose: ");
        PaxTransLink.getInstance().handleIsContinueBatchClose(true);
    }

    @Override
    public void onWarnUntipped() {
        AppLog.d(TAG, "onWarnUntipped: ");
        PaxTransLink.getInstance().handleWarnUntipped(true);
    }

    @Override
    public void onBatchCloseStart(String code, String message) {
        AppLog.d(TAG, "onBatchCloseStart: ");
        final PosdkPayMessage posdkPayMessage = new PosdkPayMessage();

        posdkPayMessage.setResultCode(code);
        posdkPayMessage.setResultMessage(message);
        posdkPayMessage.setMessageType(PROGRESS_MSG);
        EventBus.getDefault().post(posdkPayMessage);
        PaxTransLink.getInstance().handleBatchClose(new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                posdkPayMessage.setFinishProgressDialog(true);
                EventBus.getDefault().post(posdkPayMessage);
            }

            @Override
            public void onFail(String code, String msg) {
                posdkPayMessage.setFinishProgressDialog(true);
                posdkPayMessage.setMessageType(ERROR_MSG);
                posdkPayMessage.setResultCode(code);
                posdkPayMessage.setResultMessage(msg);
                EventBus.getDefault().post(posdkPayMessage);
            }
        });
    }

    @Override
    public void onWarnUploadTrans() {
        AppLog.d(TAG, "onWarnUploadTrans: ");
        PaxTransLink.getInstance().handleBatchUploadTrans(true);
    }

    @Override
    public void onWarnUploadFailTrans() {
        AppLog.d(TAG, "onWarnUploadFailTrans: ");
        PaxTransLink.getInstance().handleBatchUploadFailTrans(true);
    }

    @Override
    public void onUploadingTrans(String code, String message) {
        AppLog.d(TAG, "onUploadingTrans: ");
        final PosdkPayMessage posdkPayMessage = new PosdkPayMessage();
        posdkPayMessage.setMessageType(PROGRESS_MSG);
        posdkPayMessage.setResultMessage(message);
        EventBus.getDefault().post(posdkPayMessage);
        PaxTransLink.getInstance().handleBatchUploadingTrans(new PaxTransLink.UpdateMessageCallback() {
            @Override
            public void onUpdateMessage(String code, String message) {
                posdkPayMessage.setMessageType(PROGRESS_MSG);
                posdkPayMessage.setResultMessage(message);
                EventBus.getDefault().post(posdkPayMessage);
            }
        }, new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                posdkPayMessage.setFinishProgressDialog(true);
                EventBus.getDefault().post(posdkPayMessage);
            }

            @Override
            public void onFail(String code, String message) {
                posdkPayMessage.setFinishProgressDialog(true);
                posdkPayMessage.setMessageType(ERROR_MSG);
                posdkPayMessage.setResultMessage(message);
                EventBus.getDefault().post(posdkPayMessage);
            }
        });
    }

    @Override
    public void onStartConnect() {
        AppLog.d(TAG, "onStartConnect: ");
        PosdkPayMessage posdkPayMessage = new PosdkPayMessage();
        posdkPayMessage.setResultMessage("Start Batch");
        posdkPayMessage.setMessageType(PROGRESS_MSG);

        EventBus.getDefault().post(posdkPayMessage);
    }

    @Override
    public void onConnectService() {
        AppLog.d(TAG, "onConnectService: ");
    }

    @Override
    public void onTransEnd(TransBatchResponse transBatchResponse) {
//        AppLog.d(TAG, "onTransEnd: " + transBatchResponse.getResultCode() + transBatchResponse.getResultTxt());
        PosdkPayMessage posdkPayMessage = new PosdkPayMessage();
        posdkPayMessage.setFinishProgressDialog(true);
        posdkPayMessage.setFinishActivity(true);
        if (transBatchResponse == null) {
            EventBus.getDefault().post(posdkPayMessage);
            return;
        }
        BatchResponseVar responseVar = new BatchResponseVar();
        responseVar.setResult(transBatchResponse.getResultCode());
        responseVar.setTransType(FinancialApplication.getApp().getString(R.string.prn_batchTransName));
        responseVar.setMessageText(transBatchResponse.getResultTxt());
        if (ResultConstant.CODE_TRANSACTION_NOT_FOUND.equals(transBatchResponse.getResultCode())) {
            posdkPayMessage.setResultMessage("No trans record!");
            posdkPayMessage.setMessageType(ERROR_MSG);
        } else if (ResultConstant.CODE_OK.equals(transBatchResponse.getResultCode())) {
            long totalAmt = 0;

            responseVar.setResult(IBaseResponse.TRANS_SUCESS);
            responseVar.setBatchNum(transBatchResponse.getBatchNum());
            responseVar.setAuthCode(transBatchResponse.getAuthCode());
            responseVar.setTransDate(Tools.dateTimeFormat(transBatchResponse.getTimestamp().substring(0, 8), true));
            responseVar.setTransTime(Tools.dateTimeFormat(transBatchResponse.getTimestamp().substring(8, 14), false));
            if ((transBatchResponse.getCashAmount() != null) && (transBatchResponse.getCashAmount().length() != 0)) {
                totalAmt += Long.parseLong(transBatchResponse.getCashAmount());
                responseVar.setCashAmt(AmountUtils.amountFormat(DoubleUtils.round(
                        Double.parseDouble(transBatchResponse.getCashAmount()) / 100)).toString());
            } else {
                responseVar.setCashAmt(AmountUtils.amountFormat(DoubleUtils.round(Double.MIN_VALUE)).toString());
            }
            responseVar.setCashCount(transBatchResponse.getCashCount());
            if ((transBatchResponse.getCHECKAmount() != null) && (transBatchResponse.getCHECKAmount().length() != 0)) {
                totalAmt += Long.parseLong(transBatchResponse.getCHECKAmount());
                responseVar.setCheckAmt(AmountUtils.amountFormat(DoubleUtils.round(
                        Double.parseDouble(transBatchResponse.getCHECKAmount()) / 100)).toString());
            } else {
                responseVar.setCheckAmt(AmountUtils.amountFormat(DoubleUtils.round(Double.MIN_VALUE)).toString());
            }
            responseVar.setCheckCount(transBatchResponse.getCHECKCount());
            if ((transBatchResponse.getCreditAmount() != null) && (transBatchResponse.getCreditAmount().length() != 0)) {
                totalAmt += Long.parseLong(transBatchResponse.getCreditAmount());
                responseVar.setCreditAmount(AmountUtils.amountFormat(DoubleUtils.round(
                        Double.parseDouble(transBatchResponse.getCreditAmount()) / 100)).toString());
            } else {
                responseVar.setCreditAmount(AmountUtils.amountFormat(DoubleUtils.round(Double.MIN_VALUE)).toString());
            }
            responseVar.setCreditCount(transBatchResponse.getCreditCount());
            if ((transBatchResponse.getDebitAmount() != null) && (transBatchResponse.getDebitAmount().length() != 0)) {
                totalAmt += Long.parseLong(transBatchResponse.getDebitAmount());
                responseVar.setDebitAmount(AmountUtils.amountFormat(DoubleUtils.round(
                        Double.parseDouble(transBatchResponse.getDebitAmount()) / 100)).toString());
            } else {
                responseVar.setDebitAmount(AmountUtils.amountFormat(DoubleUtils.round(Double.MIN_VALUE)).toString());
            }

            responseVar.setDebitCount(transBatchResponse.getDebitCount());
            responseVar.setTotalBatchAmt(AmountUtils.amountFormat(DoubleUtils.round(
                    Double.parseDouble(String.valueOf(totalAmt)) / 100)).toString());
            responseVar.setTotalBatchCount(String.valueOf(Integer.parseInt(responseVar.getCreditCount())
                    + Integer.parseInt(responseVar.getDebitCount()) + Integer.parseInt(responseVar.getCashCount())
                    + Integer.parseInt(responseVar.getCheckCount())));
            if ((transBatchResponse.getExtData() != null && (transBatchResponse.getExtData().length() != 0))) {
                HashMap<String, String> hashMap = XmlParse.parseXml(transBatchResponse.getExtData());
                responseVar.setSn(hashMap.get("SN"));
            }
        } else {
            posdkPayMessage.setResultMessage(transBatchResponse.getResultTxt());
            posdkPayMessage.setMessageType(ERROR_MSG);
        }

        posdkPayMessage.setResponseVar((IBaseResponse) responseVar);
        EventBus.getDefault().post(posdkPayMessage);
    }
}
