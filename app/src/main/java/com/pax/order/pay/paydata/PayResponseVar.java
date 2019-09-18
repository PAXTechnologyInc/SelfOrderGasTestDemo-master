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

public class PayResponseVar implements IPayResponse, Serializable {

    //0 :succ 1:timeout other:cancel-pay 3:amout error(0金额或者负金额)
    //base response
    public String result;
    public String messageText;
    public String transType;
    public String batchNum;
    public String authCode;
    public String transDate;
    public String transTime;

    //sale response
    public String cardHolderName;
    public String entryMode;
    public String cardNo;
    public String cardOrg;
    public String approvedAmt;
    public String refNum;
    public String tipAmt;
    public String expireDate;
    public String tc;
    public String tvr;
    public String aid;
    public String iad;
    public String tsi;
    public String arc;
    public String appn;
    public String atc;
    public String applab;
    public String totalAmt;
    public String hostMessage;
    public String cardBin;

    public String sigFileName;
    public String signData ;


    public PayResponseVar() {

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

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardOrg() {
        return cardOrg;
    }

    public void setCardOrg(String cardOrg) {
        this.cardOrg = cardOrg;
    }

    public String getApprovedAmt() {
        return approvedAmt;
    }

    public void setApprovedAmt(String approvedAmt) {
        this.approvedAmt = approvedAmt;
    }

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getTipAmt() {
        return tipAmt;
    }

    public void setTipAmt(String tipAmt) {
        this.tipAmt = tipAmt;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getIad() {
        return iad;
    }

    public void setIad(String iad) {
        this.iad = iad;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getArc() {
        return arc;
    }

    public void setArc(String arc) {
        this.arc = arc;
    }

    public String getAppn() {
        return appn;
    }

    public void setAppn(String appn) {
        this.appn = appn;
    }

    public String getAtc() {
        return atc;
    }

    public void setAtc(String atc) {
        this.atc = atc;
    }

    public String getApplab() {
        return applab;
    }

    public void setApplab(String applab) {
        this.applab = applab;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getHostMessage() {
        return hostMessage;
    }

    public void setHostMessage(String hostMessage) {
        this.hostMessage = hostMessage;
    }

    public String getCardBin() {
        return cardBin;
    }

    public void setCardBin(String cardBin) {
        this.cardBin = cardBin;
    }

    public String getSigFileName() {
        return sigFileName;
    }

    public void setSigFileName(String sigFileName) {
        this.sigFileName = sigFileName;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData;
    }
}
