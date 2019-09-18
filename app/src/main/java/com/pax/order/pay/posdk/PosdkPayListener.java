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

import com.pax.order.FinancialApplication;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.util.ActivityStack;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.Tools;
import com.pax.order.util.XmlParse;
import com.pax.posdk.PaxTransLink;
import com.pax.posdk.PaymentListener;
import com.pax.posdk.ResultConstant;
import com.pax.posdk.card.CardEvent;
import com.pax.posdk.request.TransPaymentRequest;
import com.pax.posdk.response.TransPaymentResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

public class PosdkPayListener implements PaymentListener, PaxTransLink.CurrentStepCallback {
    volatile private static PosdkPayListener instance = null;
    private TransPaymentRequest mTransPaymentRequest;
    private static final String TAG = PosdkPayListener.class.getSimpleName();
    private final int WARN_MSG = 1;
    private final int ERROR_MSG = 2;
    private final int PROGRESS_MSG = 3;
    private final int OK_MSG = 0;


    private PosdkPayListener() {
    }

    public static PosdkPayListener getInstance() {
        if (null == instance) {
            synchronized (PosdkPayListener.class) {
                if (null == instance) {
                    instance = new PosdkPayListener();
                    instance.mTransPaymentRequest = null;

                }
            }
        }
        return instance;
    }

    public TransPaymentRequest getTransPaymentRequest() {
        if (mTransPaymentRequest == null) {
            mTransPaymentRequest = new TransPaymentRequest();
        }

        return mTransPaymentRequest;
    }


    @Override
    public void onSelectAID(List<String> list) {

    }

    @Override
    public void onEnterAmount() {
        AppLog.d(TAG, "onEnterAmount: ");
    }

    @Override
    public void onEnterTip() {
        AppLog.d(TAG, "onEnterTip: ");
        PaxTransLink.getInstance().handleInputTip(0, this);
    }

    @Override
    public void onInputAccountStart(byte b) {
        AppLog.d(TAG, "onInputAccountStart: ");
        PaxTransLink.getInstance().handleInputAccount(new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                AppLog.d(TAG, "input acount onSuccess!!! ");
            }

            @Override
            public void onFail(String code, String msg) {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishActivity(false);
                mPosdkPayMessage.setMessageType(WARN_MSG);
                mPosdkPayMessage.setResultCode(code);
                mPosdkPayMessage.setResultMessage(msg);
                AppLog.d(TAG, "onFail: onInputAccout Start post" + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);
            }
        }, new PaxTransLink.CardEventListener() {
            @Override
            public void onEvent(String event) {
                if ((CardEvent.FALLBACK_SWIPE.equals(event)) || (CardEvent.FALLBACK_INSERT).equals(event)) {
                    PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                    mPosdkPayMessage.setFinishActivity(false);
                    mPosdkPayMessage.setMessageType(WARN_MSG);
                    mPosdkPayMessage.setResultCode("1");
                    mPosdkPayMessage.setResultMessage(event);
                    AppLog.d(TAG, "onEvent: onInputAccout EventBus post!!" + event);
                    EventBus.getDefault().post(mPosdkPayMessage);
                }
            }
        });
    }

    @Override
    public void onTransOnlineStart(String s, String message) {

        PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
        mPosdkPayMessage.setFinishActivity(false);
        mPosdkPayMessage.setMessageType(PROGRESS_MSG);
        mPosdkPayMessage.setResultCode(s);
        mPosdkPayMessage.setResultMessage(message);
        AppLog.d(TAG, "onTransOnlineStart: post " + mPosdkPayMessage.getResultMessage());
        EventBus.getDefault().post(mPosdkPayMessage);

        PaxTransLink.getInstance().handleTransOnline(new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishProgressDialog(true);
                AppLog.d(TAG, "onSuccess: onTransOnlineStart post " + mPosdkPayMessage.isFinishProgressDialog());
                EventBus.getDefault().post(mPosdkPayMessage);
            }

            @Override
            public void onFail(String code, String msg) {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishProgressDialog(true);
                mPosdkPayMessage.setFinishActivity(true);
                mPosdkPayMessage.setMessageType(ERROR_MSG);
                mPosdkPayMessage.setResultCode(code);
                mPosdkPayMessage.setResultMessage(msg);
                IBaseResponse responseVar = new PayResponseVar();
                responseVar.setResult("2");
                mPosdkPayMessage.setResponseVar(responseVar);
                AppLog.d(TAG, "onFail: onTransOnlineStart post" + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);

            }
        });
    }

    @Override
    public void onEnterTransNum() {
        AppLog.d(TAG, "onEnterTransNum: ");
    }

    @Override
    public void onCheckCardPresent() {
        AppLog.d(TAG, "onCheckCardPresent: ");
    }

    @Override
    public void onEnterAuthCode() {
        AppLog.d(TAG, "onEnterAuthCode: ");
    }

    @Override
    public void onSelectEBTType(List<String> list) {
        AppLog.d(TAG, "onSelectEBTType: ");
    }

    @Override
    public void onSelectTaxReason(List<String> list) {
        AppLog.d(TAG, "onSelectTaxReason: ");
    }

    @Override
    public void onEnterCashBack() {
        AppLog.d(TAG, "onEnterCashBack: ");
    }

    @Override
    public void onEnterInvoice() {
        AppLog.d(TAG, "onEnterInvoice: ");
    }

    @Override
    public void onEnterVCode() {
        AppLog.d(TAG, "onEnterVCode: ");
    }

    @Override
    public void onEnterClerkID() {
        AppLog.d(TAG, "onEnterClerkID: ");
    }

    @Override
    public void onEnterTableNum() {
        AppLog.d(TAG, "onEnterTableNum: ");
    }

    @Override
    public void onEnterGuestNumber() {
        AppLog.d(TAG, "onEnterGuestNumber: ");
    }

    @Override
    public void onEnterExpiryDate() {
        AppLog.d(TAG, "onEnterExpiryDate: ");
    }

    @Override
    public void onEnterAddress() {
        AppLog.d(TAG, "onEnterAddress: ");
    }

    @Override
    public void onEnterZipCode() {
        AppLog.d(TAG, "onEnterZipCode: ");
    }

    @Override
    public void onEnterAddressAndZipCode() {
        AppLog.d(TAG, "onEnterAddressAndZipCode: ");
    }

    @Override
    public void onEnterCardNumLast4digits() {
        AppLog.d(TAG, "onEnterCardNumLast4digits: ");
    }

    @Override
    public void onEnterCardNumAllDigits() {
        AppLog.d(TAG, "onEnterCardNumAllDigits: ");
    }

    @Override
    public void onEnterPinStart() {
        AppLog.d(TAG, "onEnterPinStart: ");
        PinInputActivity.startActivity(ActivityStack.getInstance().top());
    }

    @Override
    public void onDisplayBalances(String s, String s1) {
        AppLog.d(TAG, "onDisplayBalances: ");
    }

    @Override
    public void onDisplayTransInfo(TransPaymentResponse transPaymentResponse) {
        AppLog.d(TAG, "onDisplayTransInfo: ");
    }

    @Override
    public void onConfirmSurchargeFee(String s, String s1) {
        AppLog.d(TAG, "onConfirmSurchargeFee: ");
    }

    @Override
    public void onEnterCustomerCodeAndTaxAmount() {
        AppLog.d(TAG, "onEnterCustomerCodeAndTaxAmount: ");
    }

    @Override
    public void onEnterTaxAmount() {
        AppLog.d(TAG, "onEnterTaxAmount: ");
    }

    @Override
    public void onEnterVoucherNum() {
        AppLog.d(TAG, "onEnterVoucherNum: ");
    }

    @Override
    public void onCheckLocalDUP() {
        AppLog.d(TAG, "onCheckLocalDUP: ");
    }

    @Override
    public void onSelectBypassReason(List<String> list) {
        AppLog.d(TAG, "onSelectBypassReason: ");
    }

    @Override
    public void onEMVChooseApp(List<String> list) {
        AppLog.d(TAG, "onEMVChooseApp: ");
    }

    @Override
    public void onEMVStart(String code, String message) {
        PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
        mPosdkPayMessage.setFinishActivity(false);
        mPosdkPayMessage.setMessageType(PROGRESS_MSG);
        mPosdkPayMessage.setResultCode(code);
        mPosdkPayMessage.setResultMessage(message);
        AppLog.d(TAG, "onEMVStart: post message:  " + message);
        EventBus.getDefault().post(mPosdkPayMessage);

        PaxTransLink.getInstance().handleEMVStart(new PaxTransLink.UpdateMessageCallback() {
            @Override
            public void onUpdateMessage(String code, String message) {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishActivity(false);
                mPosdkPayMessage.setMessageType(PROGRESS_MSG);
                mPosdkPayMessage.setResultCode(code);
                mPosdkPayMessage.setResultMessage(message);
                AppLog.d(TAG, "onEMVStart onUpdateMessage post : " + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);
            }
        }, new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishProgressDialog(true);
                AppLog.d(TAG, "onSuccess: onEMVStart post");
                EventBus.getDefault().post(mPosdkPayMessage);
            }

            @Override
            public void onFail(String code, String message) {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishActivity(false);
                mPosdkPayMessage.setMessageType(WARN_MSG);
                mPosdkPayMessage.setResultCode(code);
                mPosdkPayMessage.setResultMessage(message);
                AppLog.d(TAG, "onFail: onEMVStart post" + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);
            }
        });
    }

    @Override
    public void onWarnRemoveCard(String code, final String message) {
        PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
        mPosdkPayMessage.setFinishActivity(false);
        mPosdkPayMessage.setMessageType(PROGRESS_MSG);
        mPosdkPayMessage.setResultCode(code);
        mPosdkPayMessage.setResultMessage(message);
        AppLog.d(TAG, "onWarnRemoveCard post: " + mPosdkPayMessage.getResultMessage());
        EventBus.getDefault().post(mPosdkPayMessage);

        PaxTransLink.getInstance().handleWarnRemoveCard(new PaxTransLink.CurrentStepCallback() {
            @Override
            public void onSuccess() {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();

                mPosdkPayMessage.setFinishProgressDialog(true);
                mPosdkPayMessage.setResultCode(null);
                mPosdkPayMessage.setResultMessage(null);
                mPosdkPayMessage.setFinishActivity(false);
                mPosdkPayMessage.setResponseVar(null);
                mPosdkPayMessage.setMessageType(-1);
                AppLog.d(TAG, "onSuccess: onWarnRemoveCard success post" + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);
            }

            @Override
            public void onFail(String code, String message) {
                PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
                mPosdkPayMessage.setFinishProgressDialog(true);
                mPosdkPayMessage.setFinishActivity(true);
                mPosdkPayMessage.setMessageType(ERROR_MSG);
                mPosdkPayMessage.setResultCode(code);
                mPosdkPayMessage.setResultMessage(message);
                IBaseResponse responseVar = new PayResponseVar();
                responseVar.setResult("2");
                mPosdkPayMessage.setResponseVar(responseVar);
                AppLog.d(TAG, "onFail: onWarnRemovedCard : " + mPosdkPayMessage.getResultMessage());
                EventBus.getDefault().post(mPosdkPayMessage);

            }
        });
    }

    @Override
    public void onStartConnect() {
        AppLog.d(TAG, "onStartConnect: ");
    }

    @Override
    public void onConnectService() {
        AppLog.d(TAG, "onConnectService: ");
    }

    private String getCardOrg(String cardOrgCode) {
        String cardOrg;
        switch (cardOrgCode) {
            case "01":
                cardOrg = "VISA";
                break;
            case "02":
                cardOrg = "MASTER";
                break;
            case "03":
                cardOrg = "AMEX";
                break;
            case "04":
                cardOrg = "DISCOVER";
                break;
            case "05":
                cardOrg = "DINERCLUB";
                break;
            case "06":
                cardOrg = "ENROUTE";
                break;
            case "07":
                cardOrg = "JCB";
                break;
            case "08":
                cardOrg = "REVOLUTIONCARD";
                break;
            case "09":
                cardOrg = "VISAFLEET";
                break;
            case "10":
                cardOrg = "MASTERCARDFLEET";
                break;
            case "11":
                cardOrg = "FLEETONE";
                break;
            case "12":
                cardOrg = "FLEETWIDE";
                break;
            case "13":
                cardOrg = "FUELMAN";
                break;
            case "14":
                cardOrg = "GASCARD";
                break;
            case "15":
                cardOrg = "VOYAGER";
                break;
            case "16":
                cardOrg = "WRIGHTEXPRESS";
                break;
            case "99":
            default:
                cardOrg = "UnKnown";
                break;
        }
        return cardOrg;
    }

    private IBaseResponse getResponseVar(TransPaymentResponse transPaymentResponse) {
        PayResponseVar responseVar = new PayResponseVar();
        final String manual = "0";
        final String swipe = "1";
        final String contactless = "2";
        final String scan = "3";
        final String chip = "4";
        final String fallback = "5";
        final String MANUAL_PRN = "Manual";
        final String SWIPE_PRN = "Swipe";
        final String CONTACT_PRN = "Contactless";
        final String SCAN_PRN = "Scanner";
        final String CHIP_PRN = "Insert";
        final String FALLBACK_PRN = "Fallback";

        if (ResultConstant.CODE_OK.equals(transPaymentResponse.getResultCode())) {
            responseVar.setResult(IBaseResponse.TRANS_SUCESS);
            responseVar.setTransType(mTransPaymentRequest.getTenderType() + " " + mTransPaymentRequest.getTransType());
            if ((transPaymentResponse.getExtData() != null) && (transPaymentResponse.getExtData().length() != 0)) {
                HashMap<String, String> extData = XmlParse.parseXml(transPaymentResponse.getExtData());
                String etryMode = extData.get("PLEntryMode");
                if (etryMode != null) {
                    if (etryMode.equals(manual)) {
                        responseVar.setEntryMode(MANUAL_PRN);
                    } else if (etryMode.equals(swipe)) {
                        responseVar.setEntryMode(SWIPE_PRN);
                    } else if (etryMode.equals(contactless)) {
                        responseVar.setEntryMode(CONTACT_PRN);
                    } else if (etryMode.equals(scan)) {
                        responseVar.setEntryMode(SCAN_PRN);
                    } else if (etryMode.equals(chip)) {
                        responseVar.setEntryMode(CHIP_PRN);
                    } else if (etryMode.equals(fallback)) {
                        responseVar.setEntryMode(FALLBACK_PRN);
                    }
                }

                responseVar.setAid(extData.get("AID"));
                responseVar.setTvr(extData.get("TVR"));
                responseVar.setAtc(extData.get("ATC"));
                responseVar.setIad(extData.get("IAD"));
                responseVar.setRefNum(extData.get("ECRRefNum"));
                responseVar.setCardBin(extData.get("CARDBIN"));
                responseVar.setApplab(extData.get("APPLAB"));
                responseVar.setBatchNum(extData.get("BatchNum"));
                responseVar.setTsi(extData.get("TSI"));
                responseVar.setTc(extData.get("TC"));
                String expireDate = extData.get("ExpDate");
                StringBuilder stringBuilder = new StringBuilder(expireDate);
                responseVar.setExpireDate(stringBuilder.insert(2, "/").toString());
                responseVar.setArc(extData.get("ARC"));
                responseVar.setAppn(extData.get("APPN"));
            }
            responseVar.setTotalAmt(AmountUtils.amountFormat(DoubleUtils.round(
                    Double.parseDouble(transPaymentResponse.getApprovedAmount()) / 100)).toString());
            responseVar.setTipAmt(AmountUtils.amountFormat(DoubleUtils.round(
                    Double.parseDouble(mTransPaymentRequest.getTipAmt()) / 100)).toString());
            responseVar.setHostMessage(transPaymentResponse.getHostResponseMessage());
            responseVar.setApprovedAmt(AmountUtils.amountFormat(DoubleUtils.round(
                    Double.parseDouble(mTransPaymentRequest.getAmount()) / 100)).toString());
            responseVar.setMessageText(transPaymentResponse.getResultTxt());
            responseVar.setAuthCode(transPaymentResponse.getAuthCode());

            responseVar.setTransDate(Tools.dateTimeFormat(transPaymentResponse.getTimestamp().substring(0, 8), true));
            responseVar.setTransTime(Tools.dateTimeFormat(transPaymentResponse.getTimestamp().substring(8, 14), false));
            responseVar.setCardNo(responseVar.getCardBin().substring(0, 4) + "********" + transPaymentResponse.getBogusAccountNum());
            responseVar.setCardOrg(getCardOrg(transPaymentResponse.getCardType()));
        } else if (ResultConstant.CODE_TIME_OUT.equals(transPaymentResponse.getResultCode())) {
            responseVar.setResult(IBaseResponse.TRANS_TIMEOUT);
        } else {
            responseVar.setResult(IBaseResponse.TRANS_CANCEL);
        }

        return (IBaseResponse) responseVar;
    }

    @Override
    public void onTransEnd(TransPaymentResponse transPaymentResponse) {

        if ((transPaymentResponse.getResultCode().equals(ResultConstant.CODE_OK) == false)) {
            PinInputActivity.PinInputMessage pinInputMessage = new PinInputActivity.PinInputMessage();
            pinInputMessage.setFinishAcitivity(true);
            EventBus.getDefault().post(pinInputMessage);
        }
        AppLog.d(TAG, "onTransEnd: " + transPaymentResponse.getResultCode() + "+" + transPaymentResponse.getResultTxt());
        IBaseResponse responseVar;
        responseVar = getResponseVar(transPaymentResponse);
        PosdkPayMessage mPosdkPayMessage = new PosdkPayMessage();
        if (ResultConstant.CODE_TIME_OUT.equals(transPaymentResponse.getResultCode())) {
            mPosdkPayMessage.setFinishProgressDialog(true);
            mPosdkPayMessage.setFinishActivity(true);
            mPosdkPayMessage.setMessageType(ERROR_MSG);
            mPosdkPayMessage.setResultMessage(transPaymentResponse.getResultTxt());
            mPosdkPayMessage.setResultCode(transPaymentResponse.getResultTxt());
        } else if (Integer.parseInt(transPaymentResponse.getResultCode()) != 0) {
            mPosdkPayMessage.setFinishActivity(true);
            mPosdkPayMessage.setMessageType(ERROR_MSG);
            mPosdkPayMessage.setResultCode(transPaymentResponse.getResultCode());
            mPosdkPayMessage.setResultMessage(transPaymentResponse.getResultTxt());
        } else {
            mPosdkPayMessage = new PosdkPayMessage();
            mPosdkPayMessage.setFinishActivity(true);
            mPosdkPayMessage.setFinishProgressDialog(true);
        }
        mPosdkPayMessage.setResponseVar(responseVar);
        AppLog.d(TAG, "onTransEnd post: " + mPosdkPayMessage.getResultMessage());
        EventBus.getDefault().post(mPosdkPayMessage);

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFail(String s, String s1) {

    }
}
