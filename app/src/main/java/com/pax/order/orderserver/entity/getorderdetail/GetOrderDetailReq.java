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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

/**
 * get order detail request
 */
public class GetOrderDetailReq extends BaseReqModel {
    private String TerminalID;
    private String TerminalSN;
    private String OrderNumber;
    private String TableID;
    private String StartUTC;
    private String EndUTC;
    private String OrderStatus;
    private String PageIndex;
    private String PageSize;

    public GetOrderDetailReq() {
        super();
        TerminalID = "";
        TerminalSN = "";
        OrderNumber = "";
        TableID = "";
        OrderStatus = "Processing";
        PageIndex = "1";
        PageSize = "100";

        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        //calendar.add(Calendar.MONTH, -3);  //设置为前3月
        calendar.add(Calendar.DATE, -90);  //设置为90天前
        dBefore = calendar.getTime();   //得到前3月的时间
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); //设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); //设置时间格式

        sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
//        StartUTC = sdf.format(dBefore);    //格式化前3月的时间
//        EndUTC = sdf.format(dNow); //格式化当前时间
        StartUTC = "";   
        EndUTC = ""; 
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
        return OrderNumber;
    }

    public void setTraceNum(String traceNum) {
        OrderNumber = traceNum;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }
}
