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
 * 2018/9/27 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public class ErrDataInOpenTicket {
    private String mType;
    private String mItemID;
    private String mAttributeId;
    private String mStock;
    private String mQuantity;
    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getItemID() {
        return mItemID;
    }

    public void setItemID(String itemID) {
        mItemID = itemID;
    }

    public String getAttributeId() {
        return mAttributeId;
    }

    public void setAttributeId(String attributeId) {
        mAttributeId = attributeId;
    }

    public String getStock() {
        return mStock;
    }

    public void setStock(String stock) {
        mStock = stock;
    }

    public String getQuantity() {
        return mQuantity;
    }

    public void setQuantity(String quantity) {
        mQuantity = quantity;
    }
}
