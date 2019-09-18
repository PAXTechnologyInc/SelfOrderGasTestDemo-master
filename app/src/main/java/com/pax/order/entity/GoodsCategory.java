package com.pax.order.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:商品分类信息
 * author: zenglc
 * Date: 2018-8-4
 */
public class GoodsCategory {

    private int mTypeId;

    private String mTypeName;

    private int mPicUrl;

    private String mPicLocalFullpath;

    private int mCount;

    //不需要存储
    private List<GoodsItem> mItemList = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof GoodsCategory) {
            GoodsCategory goodsCategory = (GoodsCategory) obj;
            return (goodsCategory.hashCode() == this.hashCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        String hashSrc = String.valueOf(mTypeId) + mTypeName + mPicUrl;
        return hashSrc.hashCode();
    }

    public GoodsCategory() {

    }

    public GoodsCategory(int typeId, String typeName, int count, int picUrl, List<GoodsItem> list) {
        this.mTypeId = typeId;
        this.mTypeName = typeName;
        this.mCount = count;
        this.mPicUrl = picUrl;
        this.mItemList = list;
    }

    public int getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(int picUrl) {
        this.mPicUrl = picUrl;
    }

    public String getPicLocalFullpath() {
        return mPicLocalFullpath;
    }

    public void setPicLocalFullpath(String picLocalFullpath) {
        this.mPicLocalFullpath = picLocalFullpath;
    }

    public int getTypeId() {
        return mTypeId;
    }

    public void setTypeId(int typeId) {
        this.mTypeId = typeId;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        this.mTypeName = typeName;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    public List<GoodsItem> getGoodsItemList() {
        return mItemList;
    }

    public void setGoodsItemList(List<GoodsItem> list) {
        mItemList = list;
    }
}
