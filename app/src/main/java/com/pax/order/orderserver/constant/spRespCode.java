package com.pax.order.orderserver.constant;

/**
 * Created by chenyr on 2018/12/25.
 */

public class spRespCode {
    //SP后台返回
    public static final String SP_RESPONSE_SUCCESS = "0000";
    public static final String SP_RESPONSE_VERIFICATION_ERROR = "0401";
    public static final String SP_RESPONSE_TRACENUMBER_NOTFOUND = "0404";
    public static final String SP_RESPONSE_ORDER_CANCELLED = "0414";
    public static final String SP_RESPONSE_FIELD_ERROR = "0403";
    public static final String SP_RESPONSE_DURATION_TOOLARGE = "1403";//Duration should be less than 90 days.
    public static final String SP_RESPONSE_SYSTEM_ERROR = "9999";
    public static final String SP_RESPONSE_CUSTOMER_NOTFOUND = "0400";
    public static final String SP_RESPONSE_TABLEID_NOTFOUND = "0434";
    public static final String SP_RESPONSE_ORDERDATA_ERROR = "1404";
    public static final String SP_RESPONSE_SERIALNO_EXISTS = "0409";
    public static final String SP_RESPONSE_UNPAID_ORDERS = "0454";
    public static final String SP_RESPONSE_NOTIFICATION_FAILED = "0206";


    //自定义
    public static final String TM_INTERNAL_ERR = "F999";
    public static final String TM_NULL_RESPONSE = "F998";
    public static final String TM_PIC_DOWNFAIL = "F997";
    public static final String TM_ON_FAIL_THROW = "F996";
    public static final String TM_ORDER_NO_DETAIL = "F995";
}
