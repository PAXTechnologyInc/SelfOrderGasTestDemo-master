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
 * 18-8-14 下午2:37           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 单个订单数据
 */
@DatabaseTable(tableName = "printorder")
public class Order implements Serializable {
    public static final String ID_FIELD_NAME = "id";
    //下单时产生的批次号，后台返回
    public static final String TRACE_NO_NAME = "traceNo";
    private static final long serialVersionUID = 1L;
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mTermId;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mAttributeId;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mAtrributeidName;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mAtrributeidPrice;

    @DatabaseField(unique = true, canBeNull = true, columnName = TRACE_NO_NAME)
    private String mTraceNo;
    //下单时间
    @DatabaseField(canBeNull = true)
    private String mOrderTime;
    //菜名
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mDishName;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mSingleDishPrice;

    //对应价格,N*单价
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mSingleDishTotalPrice;

    //单个菜的数量
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mSingleDishTotal;
    //单个订单的总价
    @DatabaseField(canBeNull = true)
    private String mTotalPrice;
    //单个订单的总菜数量
    @DatabaseField(canBeNull = true)
    private String mDishTotal;
    //单个订单菜的种类总数量
    @DatabaseField(canBeNull = true)
    private String mDishSortTotal;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private PrintData mPrintData;

    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mQuantityAdjustment;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] mPriceAdjustment;


    public String[] getTermId() {
        return mTermId;
    }

    public void setTermId(String[] id) {
        mTermId = id;
    }

    public String[] getAttributeId() {
        return mAttributeId;
    }

    public void setAttributeId(String[] attributeId) {
        mAttributeId = attributeId;
    }

    public String[] getAtrributeidName() {
        return mAtrributeidName;
    }

    public void setAtrributeidName(String[] atrributeidName) {
        mAtrributeidName = atrributeidName;
    }

    public String getTraceNo() {
        return mTraceNo;
    }

    public void setTraceNo(String traceNo) {
        this.mTraceNo = traceNo;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.mOrderTime = orderTime;
    }

    public String getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.mTotalPrice = totalPrice;
    }

    public String getDishTotal() {
        return mDishTotal;
    }

    public void setDishTotal(String dishTotal) {
        this.mDishTotal = dishTotal;
    }

    public String getDishSortTotal() {
        return mDishSortTotal;
    }

    public void setDishSortTotal(String dishSortTotal) {
        this.mDishSortTotal = dishSortTotal;
    }

    public PrintData getPrintData() {
        return mPrintData;
    }

    public void setPrintData(PrintData printData) {
        this.mPrintData = printData;
    }

    public String[] getDishName() {
        return mDishName;
    }

    public void setDishName(String[] dishName) {
        this.mDishName = dishName;
    }

    public String[] getSingleDishTotalPrice() {
        return mSingleDishTotalPrice;
    }

    public void setSingleDishTotalPrice(String[] singleDishTotalPrice) {
        this.mSingleDishTotalPrice = singleDishTotalPrice;
    }

    public String[] getSingleDishTotal() {
        return mSingleDishTotal;
    }

    public void setSingleDishTotal(String[] singleDishTotal) {
        this.mSingleDishTotal = singleDishTotal;
    }

    public String[] getQuantityAdjustment() {
        return mQuantityAdjustment;
    }

    public void setQuantityAdjustment(String[] quantityAdjustment) {
        mQuantityAdjustment = quantityAdjustment;
    }

    public String[] getPriceAdjustment() {
        return mPriceAdjustment;
    }

    public void setPriceAdjustment(String[] priceAdjustment) {
        mPriceAdjustment = priceAdjustment;
    }

    public String[] getAtrributeidPrice() {
        return mAtrributeidPrice;
    }

    public void setAtrributeidPrice(String[] atrributeidPrice) {
        mAtrributeidPrice = atrributeidPrice;
    }

    public String[] getSingleDishPrice() {
        return mSingleDishPrice;
    }

    public void setSingleDishPrice(String[] singleDishPrice) {
        mSingleDishPrice = singleDishPrice;
    }
}

