package com.pax.order.entity;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/*
 * ============================================================================		
 * = COPYRIGHT		
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION		
 *   This software is supplied under the terms of a license agreement or		
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied		
 *   or disclosed except in accordance with the terms in that agreement.		
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.		
 * Description: // Detail description about the function of this module,		
 *             // interfaces with the other modules, and dependencies. 		
 * Revision History:		
 * Date	                 Author	                Action
 * 2018/8/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
@DatabaseTable(tableName = "Trans_Result")
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String ID_FIELD_NAME = "id";
    // ============= 需要存储 ==========================
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField(canBeNull = true)
    private String mTerminalId;

    @DatabaseField(canBeNull = true)
    private String mTerminalSn;

    @DatabaseField(canBeNull = true)
    private String mTraceNum;

    @DatabaseField(canBeNull = true)
    private String mTableID;

    @DatabaseField(canBeNull = true)
    private String mSeatNumber;

    @DatabaseField(canBeNull = true)
    private String mBaseAmt; //菜品金额(不含税)

    @DatabaseField(canBeNull = true)
    private String mTipAmt; //本次支付的税

    @DatabaseField(canBeNull = true)
    private String mTaxAmt;

    @DatabaseField(canBeNull = true)
    private String mTotalAmt;

    @DatabaseField(canBeNull = true)
    private String mApprovedAmt;

    @DatabaseField(canBeNull = true)
    private String mTransDate;

    @DatabaseField(canBeNull = true)
    private String mTransTime;

    @DatabaseField(canBeNull = true)
    private String mTransType;

    @DatabaseField(canBeNull = true)
    private String mMaskedPan;

    @DatabaseField(canBeNull = true)
    private String mEntryMode;

    @DatabaseField(canBeNull = true)
    private String mAuthCode;

    @DatabaseField(canBeNull = true)
    private String mHostResponse;

    @DatabaseField(canBeNull = true)
    private String mCardType;

    @DatabaseField(canBeNull = true)
    private String mCardBin;

    @DatabaseField(canBeNull = true)
    private String mCardHolderName;

    @DatabaseField(canBeNull = true)
    private String mEmployeeId;

    @DatabaseField(canBeNull = true)
    private String mTenderType;

    @DatabaseField(canBeNull = true)
    private String mTipType;

    @DatabaseField(canBeNull = true)
    private String mBatchNum;

    @DatabaseField(canBeNull = true)
    private String mEcrRefNum;

    @DatabaseField(canBeNull = true)
    private String mTerminalRefNum;




    public String getmEmployeeId() {
        return mEmployeeId;
    }

    public void setmEmployeeId(String mEmployeeId) {
        this.mEmployeeId = mEmployeeId;
    }

    public String getTerminalId() {
        return mTerminalId;
    }

    public void setTerminalId(String terminalId) {
        this.mTerminalId = terminalId;
    }

    public String getTerminalSn() {
        return mTerminalSn;
    }

    public void setTerminalSn(String terminalSn) {
        this.mTerminalSn = terminalSn;
    }

    public String getTraceNum() {
        return mTraceNum;
    }

    public void setTraceNum(String traceNum) {
        this.mTraceNum = traceNum;
    }

    public String getTableID() {
        return mTableID;
    }

    public void setTableID(String tableID) {
        this.mTableID = tableID;
    }

    public String getSeatNumber() {
        return mSeatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        mSeatNumber = seatNumber;
    }

    public String getBaseAmt() {
        return mBaseAmt;
    }

    public void setBaseAmt(String baseAmt) {
        this.mBaseAmt = baseAmt;
    }

    public String getTipAmt() {
        return mTipAmt;
    }

    public void setTipAmt(String tipAmt) {
        this.mTipAmt = tipAmt;
    }

    public String getTaxAmt() {
        return mTaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.mTaxAmt = taxAmt;
    }

    public String getTotalAmt() {
        return mTotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        mTotalAmt = totalAmt;
    }

    public String getApprovedAmt() {
        return mApprovedAmt;
    }

    public void setApprovedAmt(String approvedAmt) {
        mApprovedAmt = approvedAmt;
    }

    public String getTransDate() {
        return mTransDate;
    }

    public void setTransDate(String transDate) {
        mTransDate = transDate;
    }

    public String getTransTime() {
        return mTransTime;
    }

    public void setTransTime(String transTime) {
        mTransTime = transTime;
    }

    public String getTransType() {
        return mTransType;
    }

    public void setTransType(String transType) {
        mTransType = transType;
    }

    public String getMaskedPan() {
        return mMaskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.mMaskedPan = maskedPan;
    }

    public String getEntryMode() {
        return mEntryMode;
    }

    public void setEntryMode(String entryMode) {
        this.mEntryMode = entryMode;
    }

    public String getAuthCode() {
        return mAuthCode;
    }

    public void setAuthCode(String authCode) {
        this.mAuthCode = authCode;
    }

    public String getHostResponse() {
        return mHostResponse;
    }

    public void setHostResponse(String hostResponse) {
        this.mHostResponse = hostResponse;
    }

    public String getmCardType() {
        return mCardType;
    }

    public void setmCardType(String mCardType) {
        this.mCardType = mCardType;
    }

    public String getmCardBin() {
        return mCardBin;
    }

    public void setmCardBin(String mCardBin) {
        this.mCardBin = mCardBin;
    }

    public String getmCardHolderName() {
        return mCardHolderName;
    }

    public void setmCardHolderName(String mCardHolderName) {
        this.mCardHolderName = mCardHolderName;
    }

    public String getmTenderType() {
        return mTenderType;
    }

    public void setmTenderType(String mTenderType) {
        this.mTenderType = mTenderType;
    }

    public String getmTipType() {
        return mTipType;
    }

    public void setmTipType(String mTipType) {
        this.mTipType = mTipType;
    }

    public String getmBatchNum() {
        return mBatchNum;
    }

    public void setmBatchNum(String mBatchNum) {
        this.mBatchNum = mBatchNum;
    }

    public String getmEcrRefNum() {
        return mEcrRefNum;
    }

    public void setmEcrRefNum(String mEcrRefNum) {
        this.mEcrRefNum = mEcrRefNum;
    }

    public String getmTerminalRefNum() {
        return mTerminalRefNum;
    }

    public void setmTerminalRefNum(String mTerminalRefNum) {
        this.mTerminalRefNum = mTerminalRefNum;
    }
}
