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
package com.pax.order.orderserver.entity.getitem;

import java.util.ArrayList;
import java.util.List;

/**
 * item detail
 */
public class Item {
    private String Id;
    private String InventoryMethod;
    private String Stock;
    private String Name;
    private String Open;
    private String Price;
    private String TaxAmt;
    private String CategoryID;
    private String CategoryName;
    private String ImageURL;
    private String Description;
    private List<Attribute> Attributes;
    private List<AttributeCategorie> AttributeCategories;
    private String ExtDataList;
    private String TaxCategoryID;
    private String NotifyStock;
    public Item() {
        Id = "";
        InventoryMethod = "";
        Stock = "";
        Name = "";
        Open = "";
        Price = "";
        CategoryID = "";
        CategoryName = "";
        ImageURL = "";
        Description = "";
        TaxCategoryID = "";
        Attributes = new ArrayList<>();
        AttributeCategories = new ArrayList<>();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getInventoryMethod() {
        return InventoryMethod;
    }

    public void setInventoryMethod(String inventoryMethod) {
        InventoryMethod = inventoryMethod;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTaxCategoryID() {
        return TaxCategoryID;
    }

    public void setTaxCategoryID(String taxCategoryID) {
        TaxCategoryID = taxCategoryID;
    }

    public String getCategoryId() {
        return CategoryID;

    }

    public void setCategoryId(String categoryId) {
        CategoryID = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getPicUrl() {
        return ImageURL;
    }

    public void setPicUrl(String picUrl) {
        ImageURL = picUrl;
    }

    public List<Attribute> getAttributes() {
        return Attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        Attributes = attributes;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.TaxAmt = taxAmt;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getNotifyStock() {
        return NotifyStock;
    }

    public void setNotifyStock(String notifyStock) {
        NotifyStock = notifyStock;
    }

    public List<AttributeCategorie> getAttributeCategories() {
        return AttributeCategories;
    }

    public void setAttributeCategories(List<AttributeCategorie> attributeCategories) {
        AttributeCategories = attributeCategories;
    }
}
