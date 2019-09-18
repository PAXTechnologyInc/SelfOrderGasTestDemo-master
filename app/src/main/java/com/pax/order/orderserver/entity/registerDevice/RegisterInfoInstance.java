
package com.pax.order.orderserver.entity.registerDevice;

import android.util.Log;

import com.pax.order.FinancialApplication;
import com.pax.order.logger.AppLog;
import com.pax.order.util.SecurityUtils;

import java.util.Calendar;
import java.util.TimeZone;

public class RegisterInfoInstance {
    private final String TAG = RegisterInfoInstance.class.getSimpleName();
    String key = "7dd4bcb31694f554"; // DEV MODE
//    String key = "ef512f8508703e7c"; // PROD MODE
    // todo: jianping change sn to hard code sn
//    private String deviceSn = FinancialApplication.getTerminalSN();//"PAXDevice009";
    private String deviceSn = "19108522501876";//"PAXDevice009"; // Zebra
//    private String deviceSn = FinancialApplication.getTerminalSN();
    private String appKeyId = "TestForPAX"; // DEV MODE
//    private String appKeyId = "PaxRestaurantPOSAppKey"; // PROD MODE
    private String deviceId;
    private String timeStamp;
    private String signData;
    private static RegisterInfoInstance instance = null;
    private String SN = "19108522501876";

    public  String getSN() {
        return SN;
    }

    public void setSN(String sn) {
        SN = sn;
    }




    private RegisterInfoInstance() {

    }

    public static  RegisterInfoInstance getInstance(){
        if(instance == null){
            instance = new RegisterInfoInstance();
        }
        return instance;
    }

    public String getDeviceSn() {

        return deviceSn;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getTimeStamp() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        timeStamp = String.format("%d%02d%02d%02d%02d%02d",
                calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,
                calendar.get(Calendar.DATE),calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),calendar.get(Calendar.SECOND));
        Log.i(TAG,"UTC:"+timeStamp);
        return timeStamp;
    }

    public String getSignData() {
        if(getDeviceSn() != null && !getDeviceSn().isEmpty()){
            signData = SecurityUtils.genHMAC(getDeviceSn()+getTimeStamp(),key);
        }else if(getDeviceId() != null && !getDeviceId().isEmpty()){
            signData = SecurityUtils.genHMAC(getDeviceId()+getTimeStamp(),key);
        }else{
            AppLog.e("TAG","GET SIGN DATA NULL!!!!!");
        }
//        AppLog.i(TAG,signData);
        return signData;
    }

    public String getAppKeyId() {
        return appKeyId;
    }
}
