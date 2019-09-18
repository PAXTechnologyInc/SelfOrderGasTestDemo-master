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
package com.pax.order.orderserver.entity.modifyTicket;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

import java.util.ArrayList;
import java.util.List;

/**
 * upload request
 */
public class ModifyTicketReq extends BaseReqModel {
    private String OrderNumber;
    private String GuestCount;
    private String EmployeeId;
    private String DiscountAmt;
    private String ModifyType;
    private List<ModifyItemReq> ModifyItemList;
    private List<OpenItemReq> Items;
    private String SerialNumber;

    public ModifyTicketReq() {
        super();
        OrderNumber = "";
        ModifyType = "";
        ModifyItemList = new ArrayList<>();
    }

    public String getTraceNum() {
        return OrderNumber;
    }

    public void setTraceNum(String traceNum) {
        OrderNumber = traceNum;
    }

    public String getModifyType() {
        return ModifyType;
    }

    public void setModifyType(String modifyType) {
        ModifyType = modifyType;
    }

    public List<ModifyItemReq> getModifyItemList() {
        return ModifyItemList;
    }

    public void setModifyItemList(List<ModifyItemReq> modifyItemList) {
        ModifyItemList = modifyItemList;
    }

    public String getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(String guestCount) {
        GuestCount = guestCount;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getDiscountAmt() {
        return DiscountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        DiscountAmt = discountAmt;
    }

    public List<OpenItemReq> getOpenItemList() {
        return Items;
    }

    public void setOpenItemList(List<OpenItemReq> openItemList) {
        this.Items = openItemList;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }
}
