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
package com.pax.order.orderserver.entity.uploadmultitrans;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

import java.util.ArrayList;
import java.util.List;

/**
 * multi upload request
 */
public class UploadMultiTransReq extends BaseReqModel {
    private String TerminalID;
    private String TerminalSN;
    private String SerialNumber;
    private List<Trans> MultiTrans;

    public UploadMultiTransReq() {
        super();
        TerminalID = "";
        TerminalSN = "";
        SerialNumber = "";
        MultiTrans = new ArrayList<>();
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

    public List<Trans> getMultiTrans() {
        return MultiTrans;
    }

    public void setMultiTrans(List<Trans> multiTrans) {
        MultiTrans = multiTrans;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

}
