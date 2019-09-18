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

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

/**
 * get order amount request
 */
public class GetOrderAmountReq extends BaseReqModel {

    private String TerminalID;
    private String TerminalSN;
    private String TraceNum;
    private String TableID;

    public GetOrderAmountReq() {
        super();
        TerminalID = "";
        TerminalSN = "";
        TraceNum = "";
        TableID = "";
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

    public String getTraceNum() {
        return TraceNum;
    }

    public void setTraceNum(String traceNum) {
        TraceNum = traceNum;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }
}
