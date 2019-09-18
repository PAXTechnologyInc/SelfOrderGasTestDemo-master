package com.pax.order.orderserver.entity.openticket;

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
 * 2018/9/27 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public class ErrInTicket {
    private String Type;
    private String ItemID;
    private String AttributeId;
    private String Stock;
    private String Quantity;
    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getAttributeId() {
        return AttributeId;
    }

    public void setAttributeId(String attributeId) {
        AttributeId = attributeId;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
