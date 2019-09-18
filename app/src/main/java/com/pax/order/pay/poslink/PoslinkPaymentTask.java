package com.pax.order.pay.poslink;

import android.content.Context;
import android.os.AsyncTask;

import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.poslink.PaymentRequest;
import com.pax.poslink.PaymentResponse;
import com.pax.poslink.PosLink;
import com.pax.poslink.ProcessTransResult;
import com.pax.poslink.constant.TransType;

/**
 * Created by Leon.F on 2018/1/19.
 */

public class PoslinkPaymentTask extends AsyncTask<RequestVar, Void, Void> {
    private static final String TAG = "PoslinkPaymentTask"; // log label
    private ProcessTransResult ptr;
    private PosLink poslink;
    private Pay.PayListener listener;
    private ViewUtil mViewUtil;

    public PoslinkPaymentTask(Context context, PosLink posLink, Pay.PayListener listener) {
        this.poslink = posLink;
        this.listener = listener;
    }

    public void setViewUtil(ViewUtil viewUtil) {
        mViewUtil = viewUtil;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mViewUtil.showTransProcsess();
    }

    @Override
    protected Void doInBackground(RequestVar... requestVar) {
        AppLog.i(TAG, "doInBackground: ");

        poslink.PaymentRequest = requestVar2PaymentRequest(requestVar[0]);
        AppLog.i(TAG, "paymentRequest.TenderType=" + poslink.PaymentRequest.TenderType + ",paymentRequest.TransType="
                + poslink.PaymentRequest.TransType);

        try {
            Thread.sleep(500);
            // ProcessTrans is Blocking call, will return when the transaction is complete.
            AppLog.i(TAG, "ProcessTrans: start");
            ptr = poslink.ProcessTrans();
            AppLog.i(TAG, "ProcessTrans: end" + ptr.Code + " " + ptr.Msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        final PayResponseVar payResponseVar = getResponseVar(poslink.PaymentResponse);
        mViewUtil.dismissTransProgress();
        mViewUtil.showTransResult(payResponseVar, new OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                listener.onResult(payResponseVar);
            }
        });
    }

    private PayResponseVar getResponseVar(PaymentResponse paymentResponse) {
        PayResponseVar payResponseVar = new PayResponseVar();

        if (paymentResponse == null) {
            payResponseVar.setResult(PayResponseVar.TRANS_FAIL);
            return payResponseVar;
        }

        AppLog.i(TAG, "getResponseVar: " + paymentResponse.ResultCode);
        String resultCode = paymentResponse.ResultCode;
        if (resultCode.equals("000000")) {
            payResponseVar.setResult(PayResponseVar.TRANS_SUCESS);
            payResponseVar.setAuthCode(paymentResponse.AuthCode);
//        payResponseVar.setBatchNum(transPaymentResponse);
//        payResponseVar.setTransDate(transPaymentResponse.);
            payResponseVar.setTransTime(paymentResponse.Timestamp);
            payResponseVar.setCardNo(paymentResponse.BogusAccountNum);
            payResponseVar.setCardOrg(paymentResponse.CardType);
        } else if (resultCode.equals("100002")) {
            payResponseVar.setResult(PayResponseVar.TRANS_CANCEL);
        } else if (resultCode.equals("100001")) {
            payResponseVar.setResult(PayResponseVar.TRANS_TIMEOUT);
        } else {
            payResponseVar.setResult(PayResponseVar.TRANS_FAIL);
        }

        return payResponseVar;
    }

    private String getAmount(String amount, String tipAmount) {

        long tmpAmount = Long.parseLong(amount) - Long.parseLong(tipAmount);

        return String.valueOf(tmpAmount);
    }

    private PaymentRequest requestVar2PaymentRequest(RequestVar requestVar) {

        AppLog.i(TAG, "requestVar2PaymentRequest: ");

        PaymentRequest request = new PaymentRequest();
        request.TransType = request.ParseTransType(TransType.SALE);
        request.TenderType = request.ParseTenderType(requestVar.tendType);
        request.Amount = getAmount(requestVar.amount, requestVar.tipAmount);
//        request.TaxAmt=requestVar.taxAmount;
        request.TipAmt = requestVar.tipAmount;
        AppLog.i(TAG, "requestVar2PaymentRequest: " + request.Amount + "," + request.TipAmt);

        request.ECRRefNum = requestVar.ecrRefNum;

        return request;
    }

}
