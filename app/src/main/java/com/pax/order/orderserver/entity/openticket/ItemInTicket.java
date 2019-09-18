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
package com.pax.order.orderserver.entity.openticket;

/**
 * item detail in ticket request
 */
public class ItemInTicket {
    private String Id;
    private String Quantity;
    private String AttributeId;
    private String DiscountAmt;

    public ItemInTicket() {
        Id = "";
        Quantity = "";
        AttributeId = "";
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getAttributeId() {
        return AttributeId;
    }

    public void setAttributeId(String attributeId) {
        AttributeId = attributeId;
    }

    public String getDiscountAmt() {
        return DiscountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        DiscountAmt = discountAmt;
    }
}
