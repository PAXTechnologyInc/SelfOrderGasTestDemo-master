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
 * 18-12-26 上午10:47           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.pay.paydata;

import java.io.Serializable;

public class BatchResponseVar implements IBatchResponse, Serializable {
    //base response
    public String result;
    public String messageText;
    public String transType;
    public String batchNum;
    public String authCode;
    public String transDate;
    public String transTime;
    //batch response
    public String cashAmt;
    public String cashCount;
    public String checkAmt;
    public String checkCount;
    public String creditAmount;
    public String creditCount;
    public String debitAmount;
    public String debitCount;
    public String sn;
    public String totalBatchAmt;
    public String totalBatchCount;

    public BatchResponseVar() {
        cashAmt = "0";
        cashCount = "0";
        checkAmt = "0";
        checkCount = "0";
        creditAmount = "0";
        creditCount = "0";
        debitAmount = "0";
        debitCount = "0";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(String cashAmt) {
        if ((cashAmt != null) && (cashAmt.length() != 0)) {
            this.cashAmt = cashAmt;
        }
    }

    public String getCashCount() {
        return cashCount;
    }

    public void setCashCount(String cashCount) {
        if ((cashCount != null) && (cashCount.length() != 0)) {
            this.cashCount = cashCount;
        }
    }

    public String getCheckAmt() {
        return checkAmt;
    }

    public void setCheckAmt(String checkAmt) {
        if ((checkAmt != null) && (checkAmt.length() != 0)) {
            this.checkAmt = checkAmt;
        }
    }

    public String getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(String checkCount) {
        if ((checkCount != null) && (checkCount.length() != 0)) {
            this.checkCount = checkCount;
        }
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        if ((creditAmount != null) && (creditAmount.length() != 0)) {
            this.creditAmount = creditAmount;
        }
    }

    public String getCreditCount() {
        return creditCount;
    }

    public void setCreditCount(String creditCount) {
        if ((creditCount != null) && (creditCount.length() != 0)) {
            this.creditCount = creditCount;
        }
    }

    public String getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(String debitAmount) {
        if ((debitAmount != null) && (debitAmount.length() != 0)) {
            this.debitAmount = debitAmount;
        }
    }

    public String getDebitCount() {
        return debitCount;
    }

    public void setDebitCount(String debitCount) {
        if ((debitCount != null) && (debitCount.length() != 0)) {
            this.debitCount = debitCount;
        }
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTotalBatchAmt() {
        return totalBatchAmt;
    }

    public void setTotalBatchAmt(String totalBatchAmt) {
        this.totalBatchAmt = totalBatchAmt;
    }

    public String getTotalBatchCount() {
        return totalBatchCount;
    }

    public void setTotalBatchCount(String totalBatchCount) {
        this.totalBatchCount = totalBatchCount;
    }
}
