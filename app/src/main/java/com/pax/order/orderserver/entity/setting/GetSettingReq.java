package com.pax.order.orderserver.entity.setting;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

public class GetSettingReq extends BaseReqModel {
    private  String SettingID ;

    public GetSettingReq() {
        SettingID = "";
    }

    public String getSettingID() {
        return SettingID;
    }

    public void setSettingID(String settingID) {
        SettingID = settingID;
    }
}
