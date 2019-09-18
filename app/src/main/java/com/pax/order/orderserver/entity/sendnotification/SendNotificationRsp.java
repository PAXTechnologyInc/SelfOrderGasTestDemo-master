package com.pax.order.orderserver.entity.sendnotification;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

public class SendNotificationRsp extends BaseRspModel {

    private String ExtDataList;

    public SendNotificationRsp() {
        super();
        ExtDataList = "";
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
