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

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

import java.util.ArrayList;
import java.util.List;

/**
 * open ticket request
 */
public class OpenTicketReq extends BaseReqModel {
    private String EmployeeId;
    private String GuestCount;
    private String OrderType;
    private String Name;
    private String TableId;
    private String IfCustomerView;
    private String DiscountAmt;
    private String SerialNumber;
    private String SendFCMAfterPay;
    private String AllowMultiOrders;
    private List<ItemInTicket> Items;

    public OpenTicketReq() {
        super();
        EmployeeId = "";
        GuestCount = "";
        OrderType = "";
        Name = "";
        TableId = "";
        IfCustomerView = "";
        DiscountAmt = "";
        SerialNumber = "";
        Items = new ArrayList<>();
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(String guestCount) {
        GuestCount = guestCount;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTableId() {
        return TableId;
    }

    public void setTableId(String tableId) {
        TableId = tableId;
    }

    public String getIfCustomerView() {
        return IfCustomerView;
    }

    public void setIfCustomerView(String ifCustomerView) {
        IfCustomerView = ifCustomerView;
    }

    public List<ItemInTicket> getItems() {
        return Items;
    }

    public void setItems(List<ItemInTicket> items) {
        Items = items;
    }

    public String getDiscountAmt() {
        return DiscountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        DiscountAmt = discountAmt;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getSendFCMAfterPay() {
        return SendFCMAfterPay;
    }

    public void setSendFCMAfterPay(String sendFCMAfterPay) {
        SendFCMAfterPay = sendFCMAfterPay;
    }

    public String getAllowMultiOrders() {
        return AllowMultiOrders;
    }

    public void setAllowMultiOrders(String allowMultiOrders) {
        AllowMultiOrders = allowMultiOrders;
    }
}
