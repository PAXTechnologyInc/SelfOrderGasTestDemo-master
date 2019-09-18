package com.pax.order.orderserver.entity.modifyTicket;

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
 * 2018/11/21 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public class OpenItemReq {
    private String Id;
    private String AttributeId;
    private String Quantity;
    private String DiscountAmt;
    private String TicketItemNum;

    public OpenItemReq() {
        Id = "";
        AttributeId = "";
        Quantity = "";
        DiscountAmt = "";
        TicketItemNum = "";
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

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTicketItemNum() {
        return TicketItemNum;
    }

    public void setTicketItemNum(String ticketItemNum) {
        TicketItemNum = ticketItemNum;
    }
}
