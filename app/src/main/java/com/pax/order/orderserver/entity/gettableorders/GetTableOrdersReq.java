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

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

/**
 * get table orders request
 */
public class GetTableOrdersReq extends BaseReqModel {
    private String TerminalID;
    private String TerminalSN;
    private String TableID;
    private String IsOnlyShowOpened;
    private String Area;

    public GetTableOrdersReq() {
        super();
        TerminalID = "";
        TerminalSN = "";
        TableID = "";
        IsOnlyShowOpened = "";
        Area = "";
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

    public String getTerminalSN() {
        return TerminalSN;
    }

    public void setTerminalSN(String terminalSN) {
        TerminalSN = terminalSN;
    }

    public String getIsOnlyShowOpened() {
        return IsOnlyShowOpened;
    }

    public void setIsOnlyShowOpened(String isOnlyShowOpened) {
        IsOnlyShowOpened = isOnlyShowOpened;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }
}
