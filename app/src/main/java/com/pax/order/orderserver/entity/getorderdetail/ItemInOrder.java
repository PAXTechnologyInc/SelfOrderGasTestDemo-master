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
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.pax.order.entity.OrderDetail;

/**
 * order detail's item information
 */
@DatabaseTable(tableName = "ItemInOrder")
public class ItemInOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ID_FIELD_NAME = "file_id";
    public static final String ID_TICKETITEMNUM = "OrderLineID";

    @DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
    private int mId;

    @DatabaseField(unique = true, canBeNull = false, columnName = ID_TICKETITEMNUM)
    private String OrderLineID ;

    @DatabaseField(canBeNull = true)
    private String ID;

    @DatabaseField(canBeNull = true)
    private String Name;

    @DatabaseField(canBeNull = true)
    private String Quantity;

    @DatabaseField(canBeNull = true)
    private String UnitPrice;

    @DatabaseField(canBeNull = true)
    private String Price;

    @DatabaseField(canBeNull = true)
    private String TaxAmt;

    @DatabaseField(canBeNull = true)
    private String DiscountType;

    @DatabaseField(canBeNull = true)
    private String DiscountValue;

    @DatabaseField(canBeNull = true)
    private String DiscountTax;

    @DatabaseField(canBeNull = true)
    private String AttributeId;

    @DatabaseField(canBeNull = true)
    private String AttributeSku;

    @DatabaseField(canBeNull = true)
    private String AttributePriceAdj;

    @DatabaseField(canBeNull = true)
    private String AttributeTaxAdj;

    private List<AttrInItemInOrder> AttributeValue;

    /*需单独存储*/
    @ForeignCollectionField(eager = false)
    private ForeignCollection<AttrInItemInOrder> mlist = null;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private OrderDetail mOrderDetail;

    public ItemInOrder() {
        OrderLineID = "";
        ID = "";
        Name = "";
        Quantity = "";
        UnitPrice = "";
        Price = "";
        TaxAmt = "";
        DiscountType = "";
        DiscountValue = "";
        DiscountTax = "";
        AttributeId = "";
        AttributeSku = "";
        AttributePriceAdj = "";
        AttributeTaxAdj = "";
        AttributeValue = new ArrayList<>();
    }

    public String getTicketItemNum() {
        return OrderLineID;
    }

    public void setTicketItemNum(String ticketItemNum) {
        OrderLineID = ticketItemNum;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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

    public String getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public String getDiscountValue() {
        return DiscountValue;
    }

    public void setDiscountValue(String discountValue) {
        DiscountValue = discountValue;
    }

    public String getAttributeId() {
        return AttributeId;
    }

    public void setAttributeId(String attributeId) {
        AttributeId = attributeId;
    }

    public String getAttributeSku() {
        return AttributeSku;
    }

    public void setAttributeSku(String attributeSku) {
        AttributeSku = attributeSku;
    }

    public String getAttributePriceAdj() {
        return AttributePriceAdj;
    }

    public void setAttributePriceAdj(String attributePriceAdj) {
        AttributePriceAdj = attributePriceAdj;
    }

    public String getAttributeTaxAdj() {
        return AttributeTaxAdj;
    }

    public void setAttributeTaxAdj(String attributeTaxAdj) {
        AttributeTaxAdj = attributeTaxAdj;
    }

    public List<AttrInItemInOrder> getAttributeValue() {
        return AttributeValue;
    }

    public List<AttrInItemInOrder> getAttrInOrdersLisformDb() {
        return new ArrayList<AttrInItemInOrder>(mlist);
    }

    public void setAttributeValue(List<AttrInItemInOrder> attributeValue) {
        AttributeValue = attributeValue;
    }

    public String getDiscountTax() {
        return DiscountTax;
    }

    public void setDiscountTax(String discountTax) {
        DiscountTax = discountTax;
    }

    public OrderDetail getOrderDetail() {
        return mOrderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        mOrderDetail = orderDetail;
    }
}
