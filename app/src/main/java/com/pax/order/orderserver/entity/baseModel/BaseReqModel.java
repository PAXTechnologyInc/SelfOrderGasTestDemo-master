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
package com.pax.order.orderserver.entity.baseModel;

import com.pax.order.FinancialApplication;
import com.pax.order.util.SecurityUtils;

/**
 * Created by chenyr on 2018/9/7.
 */

public class BaseReqModel {

    protected String UserName;
    protected String Password;
    protected String Token;
    protected String DeviceID;
    protected String TimeStamp;
    protected String SignatureData;


    public BaseReqModel() {
        UserName = "";
        Password = "";
        Token = "";
        DeviceID = "";
        TimeStamp = "";
        SignatureData = "";
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getSignatureData() {

        return SignatureData;
    }

    public void setSignatureData(String signatureData) {
        SignatureData = signatureData;
    }
}
