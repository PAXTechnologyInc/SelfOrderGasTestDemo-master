package com.pax.order.pay.poslink;

import android.content.Context;
import android.os.AsyncTask;

import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.poslink.BatchRequest;
import com.pax.poslink.BatchResponse;
import com.pax.poslink.PosLink;
import com.pax.poslink.ProcessTransResult;

/**
 * Created by Leon.F on 2018/1/19.
 */

public class PoslinkBatchTask extends AsyncTask<RequestVar, Void, Void> {
    private static final String TAG = "PoslinkPaymentTask"; // log label
    private ProcessTransResult ptr;
    private PosLink poslink;
    private Pay.PayListener listener;
    private ViewUtil mViewUtil;

    public PoslinkBatchTask(Context context, PosLink posLink, Pay.PayListener listener) {
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

        poslink.BatchRequest = requestVar2BatchRequest(null);
        try {
            Thread.sleep(500);
            // ProcessTrans is Blocking call, will return when the transaction is complete.
            AppLog.i(TAG, "ProcessTrans: start");
            ptr = poslink.ProcessTrans();
            AppLog.i(TAG, "ProcessTrans: end" + ptr.Code + " " + ptr.Msg);

            if (ptr.Code == ProcessTransResult.ProcessTransResultCode.OK) {
                AppLog.e(TAG, "Transaction finish! " + ptr.Code + ":" + ptr.Msg);
            } else if (ptr.Code == ProcessTransResult.ProcessTransResultCode.TimeOut) {
                AppLog.e(TAG, "Transaction TimeOut! " + ptr.Code + ":" + ptr.Msg);
            } else {
                AppLog.e(TAG, "Transaction Error! " + ptr.Code + "" + ptr.Msg);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // There will be 2 separate results that you must handle. First is the
        // ProcessTransResult, this will give you the result of the
        // request to call poslink. PaymentResponse should only be checked if
        // ProcessTransResultCode.Code == OK.


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mViewUtil.dismissTransProgress();
        final PayResponseVar payResponseVar = getResponseVar(poslink.BatchResponse);
        mViewUtil.showTransResult(payResponseVar, new OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                listener.onResult(payResponseVar);
            }
        });

    }

    private PayResponseVar getResponseVar(BatchResponse response) {
        PayResponseVar payResponseVar = new PayResponseVar();

        if (response == null) {
            payResponseVar.setResult(PayResponseVar.TRANS_FAIL);
            return payResponseVar;
        }

        AppLog.i(TAG, "getResponseVar: " + response.ResultCode);
        String resultCode = response.ResultCode;
        if (resultCode.equals("000000")) {
            payResponseVar.setResult(PayResponseVar.TRANS_SUCESS);
            payResponseVar.setAuthCode(response.AuthCode);
            payResponseVar.setTransTime(response.Timestamp);
        } else if (resultCode.equals("100002")) {
            payResponseVar.setResult(PayResponseVar.TRANS_CANCEL);
        } else if (resultCode.equals("100023")) {
            payResponseVar.setResult(PayResponseVar.TRANS_NOLOG);
        } else {
            payResponseVar.setResult(PayResponseVar.TRANS_FAIL);
        }

        return payResponseVar;
    }

    private BatchRequest requestVar2BatchRequest(RequestVar requestVar) {

        AppLog.i(TAG, "requestVar2PaymentRequest: ");

        BatchRequest request = new BatchRequest();
        request.TransType = request.ParseTransType("BATCHCLOSE");
        request.EDCType = request.ParseEDCType("ALL");

        return request;
    }

}
