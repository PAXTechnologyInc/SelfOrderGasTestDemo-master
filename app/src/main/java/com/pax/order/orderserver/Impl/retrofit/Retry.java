/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 18-9-13 下午8:25  	     JoeyTan           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.orderserver.Impl.retrofit;

import com.pax.order.logger.AppLog;
import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * intercept for retry when response is not successful
 * add intercept to retrofit
 */
public class Retry implements Interceptor {
    private int mMaxRetry; //最大重试次数
    private int mRetryNum = 0; //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

    public Retry(int maxRetry) {
        this.mMaxRetry = maxRetry;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        while (!response.isSuccessful() && mRetryNum < mMaxRetry) {
            mRetryNum++;
            AppLog.i("Request retry", "num:" + mRetryNum);
            response = chain.proceed(request);
        }

        mRetryNum = 0;
        return response;
    }
}
