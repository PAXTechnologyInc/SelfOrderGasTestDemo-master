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
 * 18-9-29 下午4:28           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.posdk;

import com.pax.order.pay.paydata.IBaseResponse;

public class PosdkPayMessage {
    private String mResultCode;
    private String mResultMessage;
    //0--wawrning 1---error 2---progress
    private int mMessageType;
    private boolean mFinishActivity;
    private boolean mFinishProgressDialog;
    private IBaseResponse mResponseVar;


    public String getResultCode() {
        return mResultCode;
    }

    public void setResultCode(String resultCode) {
        mResultCode = resultCode;
    }

    public String getResultMessage() {
        return mResultMessage;
    }

    public void setResultMessage(String resultMessage) {
        mResultMessage = resultMessage;
    }

    public int getMessageType() {
        return mMessageType;
    }

    public void setMessageType(int messageType) {
        mMessageType = messageType;
    }

    public boolean isFinishActivity() {
        return mFinishActivity;
    }

    public void setFinishActivity(boolean finishActivity) {
        mFinishActivity = finishActivity;
    }

    public boolean isFinishProgressDialog() {
        return mFinishProgressDialog;
    }

    public void setFinishProgressDialog(boolean finishProgressDialog) {
        mFinishProgressDialog = finishProgressDialog;
    }

    public IBaseResponse getResponseVar() {
        return mResponseVar;
    }

    public void setResponseVar(IBaseResponse responseVar) {
        mResponseVar = responseVar;
    }


}
