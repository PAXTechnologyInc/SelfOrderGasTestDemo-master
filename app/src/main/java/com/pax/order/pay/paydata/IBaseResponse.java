/*
 *
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
 * 18-12-24 下午7:55           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.pay.paydata;

public interface IBaseResponse {
    String KEY = IBaseResponse.class.getSimpleName();

    String TRANS_SUCESS = "0";
    String TRANS_FAIL = "1";
    String TRANS_CANCEL = "2";
    String TRANS_ERROR_AMOUNT = "3";
    String TRANS_TIMEOUT = "4";
    String TRANS_NOLOG = "5";

    String getTransType();

    void setTransType(String transType);

    String getResult();

    void setResult(String result);

    String getMessageText();

    void setMessageText(String messageText);

    String getBatchNum();

    void setBatchNum(String batchNum);

    String getAuthCode();

    void setAuthCode(String authCode);

    String getTransDate();

    void setTransDate(String transDate);

    String getTransTime();

    void setTransTime(String transTime);
}
