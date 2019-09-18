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
package com.pax.order.orderserver.entity.getorderdetail;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * order detail's attribute
 */
@DatabaseTable(tableName = "AttrInItemInOrder")
public class AttrInItemInOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "file_id";
    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField(canBeNull = true)
    private String ID;

    @DatabaseField(canBeNull = true)
    private String Type;

    @DatabaseField(canBeNull = true)
    private String Name;

    @DatabaseField(canBeNull = true)
    private String Quantity;

    @DatabaseField(canBeNull = true)
    private String Price;

    @DatabaseField(canBeNull = true)
    private String TaxAdjustment;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private ItemInOrder mItemInOrder;

    public AttrInItemInOrder() {
        ID = "";
        Type = "";
        Name = "";
        Quantity = "";
        Price = "";
        TaxAdjustment = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTaxAdjustment() {
        return TaxAdjustment;
    }

    public void setTaxAdjustment(String taxAdjustment) {
        TaxAdjustment = taxAdjustment;
    }

    public ItemInOrder getItemInOrder() {
        return mItemInOrder;
    }

    public void setItemInOrder(ItemInOrder itemInOrder) {
        mItemInOrder = itemInOrder;
    }
}
