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
 * 18-9-29 上午9:27           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.posdk;

import android.content.Context;
import android.os.AsyncTask;

import com.pax.order.pay.Pay;
import com.pax.posdk.PaxTransLink;
import com.pax.posdk.request.TransBatchRequest;
import com.pax.posdk.request.TransPaymentRequest;


public class PosdkAsncPayTask extends AsyncTask<Object, Void, Void> {
    private static PosdkAsncPayTask instance;
    private static Context mContext;
    private static Pay.PayListener mPayListener = null;

    public static PosdkAsncPayTask getInstance(Context context, Pay.PayListener listener) {

        instance = new PosdkAsncPayTask();
        mPayListener = listener;
        mContext = context;
        return instance;
    }

    @Override
    protected Void doInBackground(Object... params) {
        if (params[0] instanceof TransPaymentRequest) {
//            PosdkPayListener.getInstance().setListener(mPayListener);
            PaxTransLink.startPayment(mContext, (TransPaymentRequest) params[0], PosdkPayListener.getInstance());
        } else if (params[0] instanceof TransBatchRequest) {
//            PosdkBatchListener.getInstance().setListener(mPayListener);
            PaxTransLink.startBatch(mContext, (TransBatchRequest) params[0], PosdkBatchListener.getInstance());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
