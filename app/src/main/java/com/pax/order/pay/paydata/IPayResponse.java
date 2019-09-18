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
 * 18-12-24 下午7:57           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.pay.paydata;

public interface IPayResponse extends IBaseResponse {

    String getCardHolderName();

    void setCardHolderName(String cardHolderName);

    String getEntryMode();

    void setEntryMode(String entryMode);


    String getCardNo();

    void setCardNo(String cardNo);

    String getCardOrg();

    void setCardOrg(String cardOrg);

    String getApprovedAmt();

    void setApprovedAmt(String approvedAmt);

    String getRefNum();

    void setRefNum(String refNum);

    String getTipAmt();

    void setTipAmt(String tipAmt);

    String getExpireDate();

    void setExpireDate(String expireDate);

    String getTc();

    void setTc(String tc);

    String getTvr();

    void setTvr(String tvr);

    String getAid();

    void setAid(String aid);

    String getIad();

    void setIad(String iad);

    String getTsi();

    void setTsi(String tsi);

    String getArc();

    void setArc(String arc);

    String getAppn();

    void setAppn(String appn);

    String getAtc();

    void setAtc(String atc);

    String getApplab();

    void setApplab(String applab);

    String getTotalAmt();

    void setTotalAmt(String totalAmt);

    String getHostMessage();

    void setHostMessage(String hostMessage);
}
