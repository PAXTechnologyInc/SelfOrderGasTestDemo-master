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
package com.pax.order.entity;

/**
 * order amount response
 */
public class OrderAmount {
    private String subTotalAmt;
    private String taxAmt;
    private String tipAmt;
    private String surchargeAmt;
    private String discountType;
    private String discountValue;
    private String dueAmt;
    private String traceNum;

    public String getSubTotalAmt() {
        return subTotalAmt;
    }

    public void setSubTotalAmt(String subTotalAmt) {
        this.subTotalAmt = subTotalAmt;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getTipAmt() {
        return tipAmt;
    }

    public void setTipAmt(String tipAmt) {
        this.tipAmt = tipAmt;
    }

    public String getSurchargeAmt() {
        return surchargeAmt;
    }

    public void setSurchargeAmt(String surchargeAmt) {
        this.surchargeAmt = surchargeAmt;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getDueAmt() {
        return dueAmt;
    }

    public void setDueAmt(String dueAmt) {
        this.dueAmt = dueAmt;
    }

    public String getTraceNum() {
        return traceNum;
    }

    public void setTraceNum(String traceNum) {
        this.traceNum = traceNum;
    }
}
