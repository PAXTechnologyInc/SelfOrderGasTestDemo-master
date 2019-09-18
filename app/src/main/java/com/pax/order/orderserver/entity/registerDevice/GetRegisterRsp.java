package com.pax.order.orderserver.entity.registerDevice;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.util.HashMap;
import java.util.Map;

public class GetRegisterRsp extends BaseRspModel {

    private String DeviceID;
    private String Station;
    private String Token;
    private String IsTrial;
    private String TrialDate;
    private String RequestEncryptKey;
    private String ResponseEncryptKey;
    private Map ExtData;
  //  private List extDataList;

    public GetRegisterRsp() {
        super();
        ExtData = new HashMap();
    }

    public String getDeviceId() {
        return DeviceID;
    }

    public void setDeviceId(String DeviceID) {
        this.DeviceID = DeviceID;
    }

    public String getStation() {
        return Station;
    }

    public void setStation(String Station) {
        this.Station = Station;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getIsTrial() {
        return IsTrial;
    }

    public void setIsTrial(String IsTrial) {
        this.IsTrial = IsTrial;
    }

    public String getTrialDate() {
        return TrialDate;
    }

    public void setTrialDate(String TrialDate) {
        this.TrialDate = TrialDate;
    }

    public String getReqEncryKey() {
        return RequestEncryptKey;
    }

    public void setReqEncryKey(String RequestEncryptKey) {
        this.RequestEncryptKey = RequestEncryptKey;
    }

    public String getRspEncryKey() {
        return ResponseEncryptKey;
    }

    public void setRspEncryKey(String ResponseEncryptKey) {
        this.ResponseEncryptKey = ResponseEncryptKey;
    }

    public Map getExtData() {
        return ExtData;
    }

    public void setExtData(Map ExtData) {
        this.ExtData = ExtData;
    }
}
