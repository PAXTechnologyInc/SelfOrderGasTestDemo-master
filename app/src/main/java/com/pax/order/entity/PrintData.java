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
 * 18-8-13 下午4:04           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * by tatal/by evenly:
 * by total和by evenly：订单全部按批次顺序打印
 * by total价格的打印：
 * 如只有一个订单，则totalOrderPrice = totalPrice,如有多个订单则，totalOrderPrice = totalPrice1+tatalPricen...
 * by evnely价格的打印：
 * totalprice（evenly）=1\n*totalprice
 * by item:
 * 一个或多订单，item需要对应批次打印
 */
@DatabaseTable(tableName = "printData")
public class PrintData implements Serializable {
    public static final String ID_FIELD_NAME = "id";
    private static final long serialVersionUID = 1L;
    /*需单独存储*/
    @ForeignCollectionField(eager = false)
    private ForeignCollection<Order> mList = null;
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;
    @DatabaseField
    private String mPrintMode;
    //桌号
    @DatabaseField(canBeNull = true)
    private String mTableId;  //order 以Table Num值代替Table Id
    @DatabaseField(canBeNull = true)
    private String mCurrentPayer;
    @DatabaseField(canBeNull = true)
    private String mTotalGuest;
    //所有订单的统计信息
    @DatabaseField(canBeNull = true)
    private String mTotalOrderPrice;
    @DatabaseField(canBeNull = true)
    private String mTotalOrderTipAmount;
    @DatabaseField(canBeNull = true)
    private String mTotalOrderTaxAmount;
    @DatabaseField(canBeNull = true)
    private String mCardNo;
    @DatabaseField(canBeNull = true)
    private String mCardOrg;
    /*不存储*/
    private List<Order> mOrders;

    public String getPrintMode() {
        return mPrintMode;
    }

    public void setPrintMode(String printMode) {
        this.mPrintMode = printMode;
    }

    public String getTableId() {
        return mTableId;
    }

    public void setTableId(String tableId) {
        this.mTableId = tableId;
    }

    public String getCurrentPayer() {
        return mCurrentPayer;
    }

    public void setCurrentPayer(String currentPayer) {
        this.mCurrentPayer = currentPayer;
    }

    public String getTotalGuest() {
        return mTotalGuest;
    }

    public void setTotalGuest(String totalGuest) {
        this.mTotalGuest = totalGuest;
    }

    public List<Order> getOrders() {
        return mOrders;
    }

    public void setOrders(List<Order> orders) {
        mOrders = orders;
    }

    public String getTotalOrderPrice() {
        return mTotalOrderPrice;
    }

    public void setTotalOrderPrice(String totalOrderPrice) {
        this.mTotalOrderPrice = totalOrderPrice;
    }

    public String getTotalOrderTipAmount() {
        return mTotalOrderTipAmount;
    }

    public void setTotalOrderTipAmount(String totalOrderTipAmount) {
        this.mTotalOrderTipAmount = totalOrderTipAmount;
    }

    public String getTotalOrderTaxAmount() {
        return mTotalOrderTaxAmount;
    }

    public void setTotalOrderTaxAmount(String totalOrderTaxAmount) {
        this.mTotalOrderTaxAmount = totalOrderTaxAmount;
    }

    public String getCardNo() {
        return mCardNo;
    }

    public void setCardNo(String cardNo) {
        this.mCardNo = cardNo;
    }

    public String getCardOrg() {
        return mCardOrg;
    }

    public void setCardOrg(String cardOrg) {
        this.mCardOrg = cardOrg;
    }

    public List<Order> getOrderformDb() {
        return new ArrayList<Order>(mList);
    }
}

