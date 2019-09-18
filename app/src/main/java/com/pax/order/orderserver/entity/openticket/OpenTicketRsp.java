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

import java.util.List;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

/**
 * open ticket response
 */
public class OpenTicketRsp extends BaseRspModel {
    private String TransDate;
    private String TransTime;
    private String TraceNum;
    private String Name;
    private String DiscountType;
    private String DiscountValue;
    private String DueAmt;
    private String TotalAmt;
    private String TaxAmt;
    private String SubTotal;
    private List<ErrInTicket> ExtDataList;

    public OpenTicketRsp() {
        super();
        TransDate = "";
        TransTime = "";
        TraceNum = "";
        Name = "";
        DiscountType = "";
        DiscountValue = "";
        DueAmt = "";
        TotalAmt = "";
        TaxAmt = "";
        SubTotal = "";
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

    public String getTraceNum() {
        return TraceNum;
    }

    public void setTraceNum(String traceNum) {
        TraceNum = traceNum;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getDueAmt() {
        return DueAmt;
    }

    public void setDueAmt(String dueAmt) {
        DueAmt = dueAmt;
    }

    public String getTotalAmt() {
        return TotalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        TotalAmt = totalAmt;
    }

    public String getTaxAmt() {
        return TaxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        TaxAmt = taxAmt;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subTotal) {
        SubTotal = subTotal;
    }

    public List<ErrInTicket> getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(List<ErrInTicket> extDataList) {
        ExtDataList = extDataList;
    }
}
