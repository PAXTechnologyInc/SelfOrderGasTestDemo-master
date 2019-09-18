package com.pax.order.constant;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by Xi Chen on 10/23/2017.
 */

public class SystemVariable extends Application{

    // parameters for settings

    public static HashMap<String,String> map = new HashMap<String, String>(){
        {
            //enable card type
            put(APPConstants.VISA, APPConstants.enable);
            put(APPConstants.MASTERCARD, APPConstants.enable);
            put(APPConstants.AMEX, APPConstants.enable);
            put(APPConstants.DINERS, APPConstants.enable);
            put(APPConstants.DISCOVER, APPConstants.enable);
            put(APPConstants.JCB, APPConstants.enable);
            put(APPConstants.ENROUTE, APPConstants.disable);
            //trans type
            put(APPConstants.CREDIT, APPConstants.enable);
            put(APPConstants.DEBIT, APPConstants.enable);
            put(APPConstants.EBT, APPConstants.enable);
            put(APPConstants.CASH, APPConstants.disable);
            //edit receipt header & trailer
            put(APPConstants.header1,"");
            put(APPConstants.header2,"");
            put(APPConstants.header3,"");
            put(APPConstants.header4,"");
            put(APPConstants.header5,"");
            put(APPConstants.trailer1,"");
            put(APPConstants.trailer2,"");
            put(APPConstants.trailer3,"");
            put(APPConstants.trailer4,"");
            put(APPConstants.trailer5,"");
            //edit user password
            put(APPConstants.USERPASSWORD, "0");
            //host params
            put(APPConstants.USERNAME, "");
            put(APPConstants.PASSWORD, "");
            put(APPConstants.PosID, "");
            put(APPConstants.TimeOut, "");
            //host url
            put(APPConstants.HOSTURL1, "");
            put(APPConstants.HOSTPORT1, "");
            put(APPConstants.HOSTURL2, "");
            put(APPConstants.HOSTPORT2, "");
            //Fraud Control
            put(APPConstants.avsOnSwipe, APPConstants.disable);
            put(APPConstants.avsOnCardPrsnt, APPConstants.disable);
            put(APPConstants.avsOnCardNotPrsnt, APPConstants.disable);
            put(APPConstants.cardPresnet, APPConstants.disable);
            put(APPConstants.needCvv, APPConstants.disable);
            //working mode
            put(APPConstants.localDupCheck, APPConstants.disable);
            //operation setting
            put(APPConstants.TIPRATEOPTION1, "10%");
            put(APPConstants.TIPRATEOPTION2, "15%");
            put(APPConstants.TIPRATEOPTION3, "20%");
            put(APPConstants.DEMO,APPConstants.disable);
            put(APPConstants.PARTIALAPPROVAL, APPConstants.disable);
            put(APPConstants.PTPE, APPConstants.disable);
        }

    };

//
//    //edit receipt header & trailer
//    public static HashMap<String,String> customedReceipt = new HashMap<String,String>() {
//        {
//
//        }
//    };
//
//    public static HashMap<String, String> hostParams = new HashMap<String, String>(){
//        {
//            put(APPConstants.USERNAME, "");
//            put(APPConstants.PASSWORD, "");
//            put(APPConstants.PosID, "");
//            put(APPConstants.TimeOut, "");
//        }
//    };
//
//    public static HashMap<String, String> authURL = new HashMap<String, String>(){
//        {
//            put(APPConstants.HOSTURL, "");
//            put(APPConstants.HOSTPORT, "");
//        }
//    };
//
//
//
//    public static HashMap<String,String> fraudControl = new HashMap<String, String>(){
//        {
//
//
//        }
//    };
//    public static String dupCheck = APPConstants.enable;


}
