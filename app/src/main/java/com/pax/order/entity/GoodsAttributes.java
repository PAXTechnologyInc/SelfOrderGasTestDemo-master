package com.pax.order.entity;

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
 * 2018/8/9 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */

import com.pax.order.orderserver.entity.getitem.AttributeValue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:商品详细信息 可选属性
 * author: zenglc
 * Date: 2018-8-4
 */
public class GoodsAttributes implements Serializable {
    private String mAttributesId;

    private String mName;

    private double mPirceAdjustment;

    private double mTaxAdjustment;


    private int mStock;


    private int mGoodsItemId;
    private int mNotifyStock;



    private List<AttributeValue> attributeValue;
    private GoodsItem mGoodsItem;


    public GoodsAttributes() {

    }
    public List<String> getAttributesIdList(){
        List<String>  mAttrIdList = new ArrayList<>();
        mAttrIdList.clear();
        for(AttributeValue attValue:attributeValue){
            mAttrIdList.add(attValue.getID());
        }
        return mAttrIdList;
    }

    public List<AttributeValue> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<AttributeValue> attributeValue) {
        this.attributeValue = attributeValue;
    }
    @Override
    public int hashCode() {
        //track stock
//        String hashSrc = mAttributesId + mName
//                + String.valueOf(mStock) + String.valueOf(mGoodsItemId);

        //do not track stock
        String hashSrc = mAttributesId + mName + String.valueOf(mGoodsItemId) + String.valueOf(mNotifyStock);

        return hashSrc.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof GoodsAttributes) {
            GoodsAttributes goodsAttributes = (GoodsAttributes) obj;
            return (goodsAttributes.hashCode() == this.hashCode());
        }
        return false;
    }

    public String getId() {
        return mAttributesId;
    }

    public void setId(String id) {
        this.mAttributesId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public double getPirceAdjustMent() {
        return mPirceAdjustment;
    }

    public void setPirceAdjustMent(double pirceAdjustMent) {
        this.mPirceAdjustment = pirceAdjustMent;
    }

    public int getGoodsItemId() {
        return mGoodsItemId;
    }

    public void setGoodsItemId(int goodsItemId) {
        this.mGoodsItemId = goodsItemId;
    }

    public GoodsItem getGoodsItem() {
        return mGoodsItem;
    }

    public void setGoodsItem(GoodsItem goodsItem) {
        this.mGoodsItem = goodsItem;
    }

    public double getTaxAdjustment() {
        return mTaxAdjustment;
    }

    public void setTaxAdjustment(double taxAdjustment) {
        mTaxAdjustment = taxAdjustment;
    }

    public int getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        mStock = stock;
    }

    public int getmNotifyStock() {
        return mNotifyStock;
    }

    public void setmNotifyStock(int mNotifyStock) {
        this.mNotifyStock = mNotifyStock;
    }
}