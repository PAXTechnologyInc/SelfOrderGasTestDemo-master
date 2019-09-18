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
 * 18-9-29 上午9:29           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.paydata;

import java.io.Serializable;

public class RequestVar implements Serializable {

    public static final String KEY = "RequestVar";
    public static final String TRANS_SALE  = "1";
    public static final String TRANS_BATCH = "2";

    public String amount;
    public String tipAmount;
    public String taxAmount;
    public String transType;
    public String tendType;
    //    public String ecrTransId;
    public String ecrRefNum;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        if ((null != amount) && (0 != amount.length())) {
            this.amount = amount;
        }
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        if ((null != tipAmount) && (0 != tipAmount.length())) {
            this.tipAmount = tipAmount;
        }
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        if ((null != taxAmount) && (0 != taxAmount.length())) {
            this.taxAmount = taxAmount;
        }
    }

    public String getTendType() {
        return tendType;
    }

    public void setTendType(String tendType) {
        this.tendType = tendType;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        if ((null != transType) && (0 != transType.length())) {
            this.transType = transType;
        }
    }

    //    public String getEcrTransId() {
//        return ecrTransId;
//    }
//
//    public void setEcrTransId(String ecrTransId) {
//        this.ecrTransId = ecrTransId;
//    }
//
    public String getEcrRefNum() {
        return ecrRefNum;
    }

    public void setEcrRefNum(String ecrRefNum) {
        this.ecrRefNum = ecrRefNum;
    }
}
