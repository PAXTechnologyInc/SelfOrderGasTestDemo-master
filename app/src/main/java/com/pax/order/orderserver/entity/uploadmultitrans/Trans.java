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
package com.pax.order.orderserver.entity.uploadmultitrans;

/**
 * transaction request detail
 */
public class Trans {
    private String OrderNumber;
    private String TotalAmt;
    private String ApprovedAmt;
    private String TipAmt;
    private String TaxAmt;
    private String TransType;
    private String CardType;
    private String CardBin;
    private String CardHolderName;
    private String EmployeeID;
    private String MaskedPan;
    private String TenderType;
    private String EntryMode;
    private String BaseAmt;
    private String TipType;
    private String TerminalRefNum;
    private String ECRRefNum;
    private String BatchNum;
    private String AuthCode;


    public Trans() {
        OrderNumber = "";
        TotalAmt = "";
        ApprovedAmt = "";
        TipAmt = "";
        TaxAmt = "";
        TransType = "";
        CardType = "";
        CardBin = "";
        CardHolderName = "";
        EmployeeID = "";

    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getTraceNum() {
        return OrderNumber;
    }

    public void setTraceNum(String traceNum) {
        OrderNumber = traceNum;
    }

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getApprovedAmt() {
        return ApprovedAmt;
    }

    public void setApprovedAmt(String approvedAmt) {
        ApprovedAmt = approvedAmt;
    }

    public String getTipAmt() {
        return TipAmt;
    }

    public void setTipAmt(String tipAmt) {
        TipAmt = tipAmt;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public String getTransType() {
        return TransType;
    }

    public void setTransType(String transType) {
        TransType = transType;
    }

    public String getCardType() {
        return CardType;
    }

    public void setCardType(String cardType) {
        CardType = cardType;
    }

    public String getCardBin() {
        return CardBin;
    }

    public void setCardBin(String cardBin) {
        CardBin = cardBin;
    }

    public String getCardHolderName() {
        return CardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        CardHolderName = cardHolderName;
    }

    public String getMaskedPan() {
        return MaskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        MaskedPan = maskedPan;
    }

    public String getTenderType() {
        return TenderType;
    }

    public void setTenderType(String tenderType) {
        TenderType = tenderType;
    }

    public String getEntryMode() {
        return EntryMode;
    }

    public void setEntryMode(String entryMode) {
        EntryMode = entryMode;
    }

    public String getBaseAmt() {
        return BaseAmt;
    }

    public void setBaseAmt(String baseAmt) {
        BaseAmt = baseAmt;
    }

    public String getTipType() {
        return TipType;
    }

    public void setTipType(String tipType) {
        TipType = tipType;
    }

    public String getTerminalRefNum() {
        return TerminalRefNum;
    }

    public void setTerminalRefNum(String terminalRefNum) {
        TerminalRefNum = terminalRefNum;
    }

    public String getECRRefNum() {
        return ECRRefNum;
    }

    public void setECRRefNum(String ECRRefNum) {
        this.ECRRefNum = ECRRefNum;
    }

    public String getBatchNum() {
        return BatchNum;
    }

    public void setBatchNum(String batchNum) {
        BatchNum = batchNum;
    }

    public String getAuthCode() {
        return AuthCode;
    }

    public void setAuthCode(String authCode) {
        AuthCode = authCode;
    }
}
