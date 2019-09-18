package com.pax.order.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.pax.order.enums.DiscountType;

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
@DatabaseTable(tableName = "open_ticket")
public class OpenTicket implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "id";
    public static final String TRACE_NUM_NAME = "traceNum";

    // ============= 需要存储 ==========================
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    /*Request*/
    /** 必须* */
    @DatabaseField(canBeNull = true)
    private String mTableId;

    /*需单独存储*/
    @ForeignCollectionField(eager = false)
    private ForeignCollection<SelectGoods> mlist = null;

    /*不存储*/
    private List<SelectGoods> mSelectList;
    /** 可选* */
    @DatabaseField(canBeNull = true)
    private String mEmployeeId;

    @DatabaseField(canBeNull = true)
    private int mGuestCount;

    @DatabaseField(canBeNull = true)
    private String mOrederType;

    @DatabaseField(canBeNull = true)
    private String mName;

    @DatabaseField(canBeNull = true)
    private boolean mIfCustomerView;

    /* Response*/
    @DatabaseField(unique = true, canBeNull = false, columnName = TRACE_NUM_NAME)
    private String mTraceNum;

    @DatabaseField(canBeNull = true)
    private DiscountType mDiscountType;

    @DatabaseField(canBeNull = true)
    private  String mDiscountValue;

    private  String mDiscountAmt;

    private  String mSerialNumber;

    @DatabaseField(canBeNull = true)
    private  String mDueAmt;

    @DatabaseField(canBeNull = true)
    private  String mTotalAmt;

    @DatabaseField(canBeNull = true)
    private  String mTaxAmt;

    @DatabaseField(canBeNull = true)
    private  String mSubTotalAmt;

    @DatabaseField(canBeNull = true)
    private String mOrderTime;

    private boolean mSendFCMAfterPay;

    private boolean mAllowMultiOrders;

    private List<ErrDataInOpenTicket> mErrDataInOpenTickets;

    public String getTraceNum() {
        return mTraceNum;
    }

    public void setTraceNum(String traceNum) {
        this.mTraceNum = traceNum;
    }

    public DiscountType getDiscountType() {
        return mDiscountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.mDiscountType = discountType;
    }

    public String getDiscountValue() {
        return mDiscountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.mDiscountValue = discountValue;
    }

    public String getDueAmt() {
        return mDueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.mDueAmt = dueAmt;
    }

    public String getTotalAmt() {
        return mTotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.mTotalAmt = totalAmt;
    }

    public String getTaxAmt() {
        return mTaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.mTaxAmt = taxAmt;
    }

    public String getSubTotalAmt() {
        return mSubTotalAmt;
    }

    public void setSubTotalAmt(String subTotalAmt) {
        this.mSubTotalAmt = subTotalAmt;
    }

    public String getTableId() {
        return mTableId;
    }

    public void setTableId(String tableId) {
        this.mTableId = tableId;
    }

    public List<SelectGoods> getSelectList() {
        return mSelectList;
    }

    public void setSelectList(List<SelectGoods> selectList) {
        this.mSelectList = selectList;
    }

    public List<SelectGoods> getSelectListformDb() {
        return new ArrayList<SelectGoods>(mlist);
    }

    public String getEmployeeId() {
        return mEmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.mEmployeeId = employeeId;
    }

    public int getGuestCount() {
        return mGuestCount;
    }

    public void setGuestCount(int guestCount) {
        this.mGuestCount = guestCount;
    }

    public String getOrederType() {
        return mOrederType;
    }

    public void setOrederType(String orederType) {
        mOrederType = orederType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isIfCustomerView(boolean b) {
        return mIfCustomerView;
    }

    public void setIfCustomerView(boolean ifCustomerView) {
        this.mIfCustomerView = ifCustomerView;
    }

    public int getGoodsKind() {
        if (null != mSelectList) {
            return mSelectList.size();
        } else {
            return 0;
        }
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.mOrderTime = orderTime;
    }

    public List<ErrDataInOpenTicket> getErrDataInOpenTickets() {
        return mErrDataInOpenTickets;
    }

    public void setErrDataInOpenTickets(List<ErrDataInOpenTicket> errDataInOpenTickets) {
        mErrDataInOpenTickets = errDataInOpenTickets;
    }

    public String getDiscountAmt() {
        return mDiscountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        mDiscountAmt = discountAmt;
    }

    public String getSerialNumber() {
        return mSerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        mSerialNumber = serialNumber;
    }

    public boolean isSendFCMAfterPay() {
        return mSendFCMAfterPay;
    }

    public void setSendFCMAfterPay(boolean sendFCMAfterPay) {
        mSendFCMAfterPay = sendFCMAfterPay;
    }

    public boolean isAllowMultiOrders() {
        return mAllowMultiOrders;
    }

    public void setAllowMultiOrders(boolean allowMultiOrders) {
        mAllowMultiOrders = allowMultiOrders;
    }
}

