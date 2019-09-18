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
package com.pax.order.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * order detail response
 */
@DatabaseTable(tableName = "Order_Detail")
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "id";
    public static final String TRACE_NUM_NAME = "traceNum";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField(unique = true, canBeNull = false, columnName = TRACE_NUM_NAME)
    private String mTraceNum;

    @DatabaseField(canBeNull = true)
    private String mTableID;

    @DatabaseField(canBeNull = true)
    private String mTransDate;

    @DatabaseField(canBeNull = true)
    private String mTransTime;

    @DatabaseField(canBeNull = true)
    private String mDueAmt;

    @DatabaseField(canBeNull = true)
    private String mSubTotalAmt;

    @DatabaseField(canBeNull = true)
    private String mTaxAmt;

    @DatabaseField(canBeNull = true)
    private String mTipAmt;

    @DatabaseField(canBeNull = true)
    private String mSurchargeAmt;

    @DatabaseField(canBeNull = true)
    private String mDiscountType;

    @DatabaseField(canBeNull = true)
    private String mDiscountValue;

    @DatabaseField(canBeNull = true)
    private String mTotalAmt;

    @DatabaseField(canBeNull = true)
    private String mServer;

    @DatabaseField(canBeNull = true)
    private String mGuestCount;

    @DatabaseField(canBeNull = true)
    private String mSeatNum;

    @DatabaseField(canBeNull = true)
    private String mCustomerName;

    @DatabaseField(canBeNull = true)
    private String mStatus;



    private List<ItemInOrder> mItemInOrdersList;

    /*需单独存储*/
    @ForeignCollectionField(eager = false)
    private ForeignCollection<ItemInOrder> mlist = null;

    @DatabaseField(canBeNull = true)
    private String mEmployeeId;


    public OrderDetail() {
    }

    public String getmEmployeeId() {
        return mEmployeeId;
    }

    public void setmEmployeeId(String mEmployeeId) {
        this.mEmployeeId = mEmployeeId;
    }

    public String getDueAmt() {
        return mDueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.mDueAmt = dueAmt;
    }

    public String getSubTotalAmt() {
        return mSubTotalAmt;
    }

    public void setSubTotalAmt(String subTotalAmt) {
        this.mSubTotalAmt = subTotalAmt;
    }

    public String getTaxAmt() {
        return mTaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.mTaxAmt = taxAmt;
    }

    public String getTipAmt() {
        return mTipAmt;
    }

    public void setTipAmt(String tipAmt) {
        this.mTipAmt = tipAmt;
    }

    public String getSurchargeAmt() {
        return mSurchargeAmt;
    }

    public void setSurchargeAmt(String surchargeAmt) {
        this.mSurchargeAmt = surchargeAmt;
    }

    public String getDiscountType() {
        return mDiscountType;
    }

    public void setDiscountType(String discountType) {
        this.mDiscountType = discountType;
    }

    public String getDiscountValue() {
        return mDiscountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.mDiscountValue = discountValue;
    }

    public String getTotalAmt() {
        return mTotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.mTotalAmt = totalAmt;
    }

    public String getTraceNum() {
        return mTraceNum;
    }

    public void setTraceNum(String traceNum) {
        this.mTraceNum = traceNum;
    }

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        this.mCustomerName = customerName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTableID() {
        return mTableID;
    }

    public void setTableID(String tableID) {
        mTableID = tableID;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String server) {
        mServer = server;
    }

    public String getGuestCount() {
        return mGuestCount;
    }

    public void setGuestCount(String guestCount) {
        mGuestCount = guestCount;
    }

    public String getSeatNum() {
        return mSeatNum;
    }

    public void setSeatNum(String seatNum) {
        mSeatNum = seatNum;
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

    public List<ItemInOrder> getmItemInOrdersList() {
        return mItemInOrdersList;
    }

    public List<ItemInOrder> getItemInOrdersLisformDb() {
        return new ArrayList<ItemInOrder>(mlist);
    }

    public void setmItemInOrdersList(List<ItemInOrder> mItemInOrdersList) {
        this.mItemInOrdersList = mItemInOrdersList;
    }
}
