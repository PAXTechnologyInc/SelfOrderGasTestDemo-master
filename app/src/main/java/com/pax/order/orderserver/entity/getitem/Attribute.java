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
 * attribute detail
 */
public class Attribute {
    private String Id;
    private String Name;
    private String Stock;
    private String PirceAdjustment;
    private String TaxAdjustment;
    private String NotifyStock;
    private List<AttributeValue> AttributeValue;



    public Attribute() {
        Id = "";
        Name = "";
        Stock = "";
        PirceAdjustment = "";
        TaxAdjustment = "";
        NotifyStock = "";
        AttributeValue = new ArrayList<>();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getPirceAdjustMent() {
        return PirceAdjustment;
    }

    public void setPirceAdjustMent(String pirceAdjustMent) {
        PirceAdjustment = pirceAdjustMent;
    }

    public String getTaxAdjustment() {
        return TaxAdjustment;
    }

    public void setTaxAdjustment(String taxAdjustment) {
        TaxAdjustment = taxAdjustment;
    }

    public String getNotifyStock() {
        return NotifyStock;
    }

    public void setNotifyStock(String notifyStock) {
        NotifyStock = notifyStock;
    }

    public List<com.pax.order.orderserver.entity.getitem.AttributeValue> getAttributeValue() {
        return AttributeValue;
    }

    public void setAttributeValue(List<com.pax.order.orderserver.entity.getitem.AttributeValue> attributeValue) {
        AttributeValue = attributeValue;
    }
}
