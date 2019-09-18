package com.pax.order.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

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
 * 2018/8/14 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
@DatabaseTable(tableName = "SelectGoods")
public class SelectGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "id";
    public static final String GOODSID_NAME = "goodsId";
    public static final String ATTRIBUTEID_NAME = "attributeId";
    public static final String UNPAIDNUM_NAME = "unPaidNum";
    public static final String PREPAYNUM_NAME = "prePaidNum";

    // ============= 需要存储 ==========================
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField(canBeNull = true, columnName = GOODSID_NAME)
    private int mGoodsId;

    @DatabaseField(canBeNull = true)
    private int mQuantity;

    @DatabaseField(canBeNull = true, columnName = ATTRIBUTEID_NAME)
    private String mAttributeId = "";

    @DatabaseField(canBeNull = true)
    private double mPrice;

    @DatabaseField(canBeNull = true)
    private double mTaxAmt;

    @DatabaseField(canBeNull = true)
    private String mAttributePrice;

    @DatabaseField(canBeNull = true)
    private double mAttributeTaxAmt;

    @DatabaseField(canBeNull = true)
    private String mName;

    @DatabaseField(canBeNull = true)
    private String mAtrributeidName;

    @DatabaseField(canBeNull = true, columnName = UNPAIDNUM_NAME)
    private int mUnPaidQuantity;

    @DatabaseField(canBeNull = true, columnName = PREPAYNUM_NAME)
    private int mPrePaidQuantity;

    private double DiscountAmt = 0.00;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private OpenTicket mOpenTicket;

    /* 合并后数据 */
    @DatabaseField(canBeNull = true)
    private int mMergeQuantity = 0;

    @DatabaseField(canBeNull = true)
    private double mMergePrice = 0.00;

    @DatabaseField(canBeNull = true)
    private double mTotalAmt = 0.00;

    @DatabaseField(canBeNull = true)
    private double mUnpaidlAmt = 0.00;

    @DatabaseField(canBeNull = true)
    private double mPrePaidAmt = 0.00;

    @DatabaseField(canBeNull = true)
    private double mUnpaidlTaxAmt = 0.00;

    @DatabaseField(canBeNull = true)
    private double mPrePaidTaxAmt = 0.00;

    @DatabaseField(canBeNull = true)
    private double mUnitTaxAmt;
    /* end */

    private int mCategoryId = 0;
    private String mCategoryName = "";
    private String mLastAttrId = "";
    //库存不足，则置位为true 购物车里修改数量后，则置位为true
    private boolean mIsUnderStock = false;
    //库存默认为最大，即库存充足。下单时如有库存不足的商品，则该值会被修改为真实值。
    private int mStockNum = Integer.MAX_VALUE;

    //能修改的最大商品数量
    private int originalQuantity;//20181203 add
    //在后台成功打折后的金额
    private double originalDiscountAmt;//20181203 add

    private double reviewTotalAmt = 0.00;
    private int reviewReduceQuantity = 0;

    public boolean isUnderStock() {
        return mIsUnderStock;
    }

    public void setUnderStock(boolean underStock) {
        mIsUnderStock = underStock;
    }

    public int getStockNum() {
        return mStockNum;
    }

    public void setStockNum(int stockNum) {
        mStockNum = stockNum;
    }

    public SelectGoods() {
        mAttributeId = "";
        mAtrributeidName = "";
    }

    public void setOpenTicket(OpenTicket openTicket) {
        this.mOpenTicket = openTicket;
    }

    public int getId() {
        return mGoodsId;
    }

    public void setId(int id) {
        this.mGoodsId = id;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
        this.mUnPaidQuantity = quantity;
    }

    public String getAttributeId() {
        return mAttributeId;
    }

    public void setAttributeId(String attributeId) {
        this.mAttributeId = attributeId;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getUnPaidQuantity() {
        return mUnPaidQuantity;
    }

    public void setUnPaidQuantity(int unPaidQuantity) {
        mUnPaidQuantity = unPaidQuantity;
    }

    public int getPrePaidQuantity() {
        return mPrePaidQuantity;
    }

    public void setPrePaidQuantity(int prePaidQuantity) {
        mPrePaidQuantity = prePaidQuantity;
    }

    public String getAtrributeidName() {
        return mAtrributeidName;
    }

    public void setAtrributeidName(String atrributeidName) {
        this.mAtrributeidName = atrributeidName;
    }

    public int getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(int categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public double getTaxAmt() {
        return mTaxAmt;
    }

    public void setTaxAmt(double taxAmt) {
        this.mTaxAmt = taxAmt;
    }

    public String getLastAttrId() {
        return mLastAttrId;
    }

    public void setLastAttrId(String lastAttrId) {
        this.mLastAttrId = lastAttrId;
    }

    public double getAttributeTaxAmt() {
        return mAttributeTaxAmt;
    }

    public void setAttributeTaxAmt(double attributeTaxAmt) {
        mAttributeTaxAmt = attributeTaxAmt;
    }

    public double getmTotalAmt() {
        return mTotalAmt;
    }

    public void setmTotalAmt(double mTotalAmt) {
        this.mTotalAmt = mTotalAmt;
    }

    public double getmUnpaidlAmt() {
        return mUnpaidlAmt;
    }

    public void setmUnpaidlAmt(double mUnpaidlAmt) {
        this.mUnpaidlAmt = mUnpaidlAmt;
    }

    public double getmPrePaidAmt() {
        return mPrePaidAmt;
    }

    public void setmPrePaidAmt(double mPrePaidAmt) {
        this.mPrePaidAmt = mPrePaidAmt;
    }

    public double getUnpaidlTaxAmt() {
        return mUnpaidlTaxAmt;
    }

    public void setUnpaidlTaxAmt(double unpaidlTaxAmt) {
        this.mUnpaidlTaxAmt = unpaidlTaxAmt;
    }

    public double getPrePaidTaxAmt() {
        return mPrePaidTaxAmt;
    }

    public void setPrePaidTaxAmt(double prePaidTaxAmt) {
        this.mPrePaidTaxAmt = prePaidTaxAmt;
    }

    public double getUnitTaxAmt() {
        return mUnitTaxAmt;
    }

    public void setUnitTaxAmt(double unitTaxAmt) {
        this.mUnitTaxAmt = unitTaxAmt;
    }

    public int getmMergeQuantity() {
        return mMergeQuantity;
    }

    public void setmMergeQuantity(int mMergeQuantity) {
        this.mMergeQuantity = mMergeQuantity;
        this.mUnPaidQuantity = mMergeQuantity;
    }

    public double getmMergePrice() {
        return mMergePrice;
    }

    public void setmMergePrice(double mMergePrice) {
        this.mMergePrice = mMergePrice;
    }

    public double getDiscountAmt() {
        return DiscountAmt;
    }

    public void setDiscountAmt(double discountAmt) {
        DiscountAmt = discountAmt;
    }

    public int getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(int originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public double getOriginalDiscountAmt() {
        return originalDiscountAmt;
    }

    public void setOriginalDiscountAmt(double originalDiscountAmt) {
        this.originalDiscountAmt = originalDiscountAmt;
    }

    public double getReviewTotalAmt() {
        return reviewTotalAmt;
    }

    public void setReviewTotalAmt(double reviewTotalAmt) {
        this.reviewTotalAmt = reviewTotalAmt;
    }

    public int getReviewReduceQuantity() {
        return reviewReduceQuantity;
    }

    public void setReviewReduceQuantity(int reviewReduceQuantity) {
        this.reviewReduceQuantity = reviewReduceQuantity;
    }

    public String getAttributePrice() {
        return mAttributePrice;
    }

    public void setAttributePrice(String attributePrice) {
        mAttributePrice = attributePrice;
    }
}
