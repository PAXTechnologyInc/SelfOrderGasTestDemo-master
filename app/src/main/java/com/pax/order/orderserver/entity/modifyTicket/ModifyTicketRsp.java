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

import java.util.List;

import com.pax.order.orderserver.entity.openticket.ErrInTicket;
import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

/**
 * upload response
 */
public class ModifyTicketRsp extends BaseRspModel {
    private String Times;
    private List<ErrInTicket> ExtDataList;

    public ModifyTicketRsp() {
        super();
        Times = "";
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public List<ErrInTicket> getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(List<ErrInTicket> extDataList) {
        ExtDataList = extDataList;
    }
}
