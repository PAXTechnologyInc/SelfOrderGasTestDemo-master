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
package com.pax.order.orderserver.entity.gettableorders;

/**
 * order information in table response
 */
public class OrderInTable {
    private String CustomerName;
    private String OrderNumber;
    private String SeatNum;
    private String OrderType;
    private String OrderStatus;

    public OrderInTable() {
        CustomerName = "";
        OrderNumber = "";
        SeatNum = "";
        OrderType = "";
        OrderStatus = "";
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTraceNum() {
        return OrderNumber;
    }

    public void setTraceNum(String traceNum) {
        OrderNumber = traceNum;
    }

    public String getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(String seatNum) {
        SeatNum = seatNum;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
