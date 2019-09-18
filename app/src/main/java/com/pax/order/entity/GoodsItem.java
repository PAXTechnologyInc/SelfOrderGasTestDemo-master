package com.pax.order.entity;

import android.graphics.Bitmap;

import com.pax.order.orderserver.entity.getitem.AttributeCategorie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * Description:商品详细信息
 * author: zenglc
 * Date: 2018-8-4
 */

public class GoodsItem implements Serializable {
    private int mTemid;
    private int mStock;
    private String mName;
    private boolean mOpen;
    private double mPrice;
    private int mCategoryId;
    private String mCategoryName;
    private String mPicUrl;
    private String mPicLocalFullpath;
    private int mCount;
    private double mTaxAmount;
    private String InventoryMethod = "";
    private int imageIcon;

    private String mDescription;
    //不需要存储
    private List<GoodsAttributes> mItemList;
    private List<AttributeCategorie> mAttCate;

    private int mNotifyStock;
    private Bitmap mBitmap;

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof GoodsItem) {
            GoodsItem goodsItem = (GoodsItem) obj;
            return (goodsItem.hashCode() == this.hashCode());
        }
        return false;

    }

    @Override
    public int hashCode() {
        StringBuilder hashSrc = new StringBuilder("");
        int attributeHash = 0;
        int  attCateHash = 0;

        if ((mItemList != null) && (mItemList.size() != 0)) {
            for (GoodsAttributes goodsAttributes : mItemList) {
                hashSrc.append(String.valueOf(goodsAttributes.hashCode()));
            }
            attributeHash = hashSrc.toString().hashCode();
            hashSrc = new StringBuilder("");
        }
        if ((mAttCate != null) && (mAttCate.size() != 0)){
            for(AttributeCategorie attCate: mAttCate){
                hashSrc.append(String.valueOf(attCate.hashCode()));
            }
            attCateHash = hashSrc.toString().hashCode();

        }

        //track stock
//        hashSrc += String.valueOf(mTemid) + String.valueOf(mStock)
//                + mName + String.valueOf(mPrice) + String.valueOf(mCategoryId) + mCategoryName
//                + mPicUrl + mDescription + InventoryMethod;

        //do not track stock
        hashSrc.append(String.valueOf(attributeHash)).append(String.valueOf(mStock)).append(String.valueOf(mTemid)).append(mName).append(String.valueOf(mPrice)).append(String.valueOf(mCategoryId)).append(mCategoryName).append(mPicUrl).append(mDescription).append(InventoryMethod).append(String.valueOf(mTaxAmount)).append(String.valueOf(attCateHash)).append(String.valueOf(mNotifyStock));

        return hashSrc.toString().hashCode();

    }

    public GoodsItem() {
    }

    public GoodsItem(int id, int stock, String name, boolean open, double price, int categoryId,
                     String categoryName, String picUrl, int imageIcon, int count, List<GoodsAttributes> goodsAttributesList) {
        this.mTemid = id;
        this.mStock = stock;
        this.mName = name;
        this.mOpen = open;
        this.mPrice = price;
        this.mCategoryId = categoryId;
        this.mCategoryName = categoryName;
        this.mPicUrl = picUrl;
        this.mPicLocalFullpath = picUrl;
        this.mCount = count;
        this.mItemList = goodsAttributesList;
        this.imageIcon = imageIcon;

        if (null != goodsAttributesList) {
            for (GoodsAttributes attributes : mItemList) {
                attributes.setGoodsItemId(id);
            }
        }
    }

    public GoodsItem deepClone() {
        GoodsItem p2 = null;
        GoodsItem p1 = this;
        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream();
        try {
            in.connect(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (ObjectOutputStream bo = new ObjectOutputStream(out);
             ObjectInputStream bi = new ObjectInputStream(in);) {
            bo.writeObject(p1);
            p2 = (GoodsItem) bi.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return p2;
    }

    public int getImageIcon() {
        return this.imageIcon;
    }

    public int getId() {
        return mTemid;
    }

    public void setId(int id) {
        this.mTemid = id;
    }

    public int getStock() {
        return mStock;
    }

    public void setStock(int stock) {
        this.mStock = stock;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public boolean isOpen() {
        return mOpen;
    }

    public void setOpen(boolean open) {
        this.mOpen = open;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        this.mPrice = price;
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

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String picUrl) {
        this.mPicUrl = picUrl;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    public List<GoodsAttributes> getGoodsAttributesList() {
        Iterator<GoodsAttributes> it = mItemList.iterator();
        while (it.hasNext()) {
            GoodsAttributes goodsAttributes = it.next();
            if (goodsAttributes.getName() == null) {
                it.remove();
            }
        }
        return mItemList;
    }

    public void setGoodsAttributesList(List<GoodsAttributes> goodsAttributesList) {
        this.mItemList = goodsAttributesList;
    }

    public String getPicLocalFullpath() {
        return mPicLocalFullpath;
    }

    public void setPicLocalFullpath(String picLocalFullpath) {
        this.mPicLocalFullpath = picLocalFullpath;
    }

    public double getTaxAmount() {
        return mTaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.mTaxAmount = taxAmount;
    }

    public String getInventoryMethod() {
        return InventoryMethod;
    }

    public void setInventoryMethod(String inventoryMethod) {
        InventoryMethod = inventoryMethod;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }
    public List<AttributeCategorie> getmAttCate() {
        return mAttCate;
    }

    public void setmAttCate(List<AttributeCategorie> mAttCate) {
        this.mAttCate = mAttCate;
    }

    public int getNotifyStock() {
        return mNotifyStock;
    }

    public void setNotifyStock(int mNotifyStock) {
        this.mNotifyStock = mNotifyStock;
    }

}
