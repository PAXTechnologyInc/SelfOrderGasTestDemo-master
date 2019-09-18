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

import java.util.ArrayList;
import java.util.List;

/**
 * order detail
 */
public class SPOrderDetail {
    private String TransDate;
    private String TransTime;
    private String DueAmt;
    private String SubTotalAmt;
    private String TaxAmt;
    private String TipAmt;
    private String SurchargeAmt;
    private String DiscountType;
    private String DiscountValue;
    private String TotalAmt;
    private String OrderNumber;
    private String EmployeeID;
    private String GuestCount;
    private String SeatNum;
    private String TableID;
    private String CustomerName;
    private String OrderStatus;
    private List<ItemInOrder> Items;

    public SPOrderDetail() {
        TransDate = "";
        TransTime = "";
        DueAmt = "";
        SubTotalAmt = "";
        TaxAmt = "";
        TipAmt = "";
        SurchargeAmt = "";
        DiscountType = "";
        DiscountValue = "";
        TotalAmt = "";
        OrderNumber = "";
        EmployeeID = "";
        GuestCount = "";
        SeatNum = "";
        TableID = "";
        CustomerName = "";
        OrderStatus = "";
        Items = new ArrayList<>();
    }

    public String getTransDate() {
        return TransDate;
    }

    public void setTransDate(String transDate) {
        TransDate = transDate;
    }

    public String getTransTime() {
        return TransTime;
    }

    public void setTransTime(String transTime) {
        TransTime = transTime;
    }

    public String getDueAmt() {
        return DueAmt;
    }

    public void setDueAmt(String dueAmt) {
        DueAmt = dueAmt;
    }

    public String getSubTotalAmt() {
        return SubTotalAmt;
    }

    public void setSubTotalAmt(String subTotalAmt) {
        SubTotalAmt = subTotalAmt;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public String getTipAmt() {
        return TipAmt;
    }

    public void setTipAmt(String tipAmt) {
        TipAmt = tipAmt;
    }

    public String getSurchargeAmt() {
        return SurchargeAmt;
    }

    public void setSurchargeAmt(String surchargeAmt) {
        SurchargeAmt = surchargeAmt;
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

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getTraceNum() {
        return OrderNumber;
    }

    public void setTraceNum(String traceNum) {
        OrderNumber = traceNum;
    }

    public String getServer() {
        return EmployeeID;
    }

    public void setServer(String server) {
        EmployeeID = server;
    }

    public String getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(String guestCount) {
        GuestCount = guestCount;
    }

    public String getSeatNum() {
        return SeatNum;
    }

    public void setSeatNum(String seatNum) {
        SeatNum = seatNum;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getStatus() {
        return OrderStatus;
    }

    public void setStatus(String status) {
        OrderStatus = status;
    }

    public List<ItemInOrder> getItems() {
        return Items;
    }

    public void setItems(List<ItemInOrder> items) {
        Items = items;
    }

}
