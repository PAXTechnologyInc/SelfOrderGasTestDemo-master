package com.pax.order.orderserver.entity.registerDevice;


import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

public class GetRegisterReq  {
    private String DeviceSN;
    private String DeviceID;
    private String TimeStamp;
    private String SignatureData;
    private String AppkeyIdentity;

    public GetRegisterReq() {
        super();
        this.DeviceSN = "";
        this.DeviceID = "";
        this.TimeStamp = "";
        this.SignatureData = "";
        this.AppkeyIdentity = "";
    }

    public String getDeviceSn() {
        return DeviceSN;
    }

    public void setDeviceSn(String DeviceSN) {
        this.DeviceSN = DeviceSN;
    }

    public String getDeviceId() {
        return DeviceID;
    }

    public void setDeviceId(String DeviceID) {
        this.DeviceID = DeviceID;
    }

    public String getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(String TimeStamp) {
        this.TimeStamp = TimeStamp;
    }

    public String getSigntureData() {
        return SignatureData;
    }

    public void setSigntureData(String SignatureData) {
        this.SignatureData = SignatureData;
    }

    public String getAppkeyId() {
        return AppkeyIdentity;
    }

    public void setAppkeyId(String AppkeyIdentity) {
        this.AppkeyIdentity = AppkeyIdentity;
    }
}
