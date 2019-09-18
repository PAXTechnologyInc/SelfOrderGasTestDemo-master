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
package com.pax.order.orderserver.entity.getorderamount;

/**
 * order amount detail
 */
class OrderAmt {
    private String SubTotalAmt;
    private String TaxAmt;
    private String TipAmt;
    private String SurchargeAmt;
    private String DiscountType;
    private String DiscountValue;
    private String DueAmt;
    private String TraceNum;
    private String ExtDataList;

    public OrderAmt() {
        SubTotalAmt = "";
        TaxAmt = "";
        TipAmt = "";
        SurchargeAmt = "";
        DiscountType = "";
        DiscountValue = "";
        DueAmt = "";
        TraceNum = "";
        ExtDataList = "";
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

    public String getDueAmt() {
        return DueAmt;
    }

    public void setDueAmt(String dueAmt) {
        DueAmt = dueAmt;
    }

    public String getTraceNum() {
        return TraceNum;
    }

    public void setTraceNum(String traceNum) {
        TraceNum = traceNum;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
