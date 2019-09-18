/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 18-9-6 下午1:44           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.entity;

public class MessgeCode {
    public static final int DOWNADVERSUCC = 1000;
    public static final int DOWNADVERFAIL = 1001;
    public static final int DOWNADVERTIMEOUT = 1002;
    public static final int DOWNITEMSUCC = 2000;
    public static final int DOWNITEMFAIL = 2001;
    public static final int DOWNCATEGORYSUCC = 3000;
    public static final int DOWNCATEGORYFAIL = 3001;
    public static final int DOWNALLINFOSUCC = 4000;
    public static final int DOWNALLINFOERR = 4001;
    public static final int DOWNUPDATEPROGRESS = 4002;
    public static final int LOGIN_MSG = 4200;
    public static final int ORDER_DETAIL_MSG = 4300;
    public static final int GET_TABLE_NUMBER_MSG = 4400;
    public static final int GET_ALL_TABLE_ORDERS_NUMBER_MSG = 4500;
    public static final int MODIFY_TICKRT_MSG = 4600;

    public static final int OPENTICKETSUCC = 5000;
    public static final int OPENTICKETFAIL = 5001;
    public static final int OPENTICKETNOPRODUCT = 5002;
    public static final int OPENTICKETALREADYEXIST = 5003;
    public static final int UPLOADTRANSSUCC = 6000;
    public static final int UPLOADTRANSFAIL = 6001;
    public static final int MODIFYTICKETSUCC = 7000;
    public static final int MODIFYTICKETFAIL = 7001;
    public static final int MODIFYTICKETVOID = 7002;
    public static final int ORDER_DETAIL_SUCC = 8000;
    public static final int ORDER_DETAIL_FAIL = 8001;
    public static final int ORDER_NO_DETAIL = 8002;
    public static final int GET_TABLE_ORDER_SUCC = 9000;
    public static final int GET_TABLE_ORDER_FAIL = 9001;

    public static final int REGISTER_DEVICE_SUC = 9300;
    public static final int REGISTER_DEVICE_FAIL = 9301;
    public static final int REGISTER_DEVICE_RETRY = 9302;

    public static final int GET_TABLE_ID_SUC = 9400;
    public static final int GET_TABLE_ID_FAIL = 9401;

}
