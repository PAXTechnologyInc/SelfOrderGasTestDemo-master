package com.pax.order.orderserver.entity.sendnotification;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

public class SendNotificationReq extends BaseReqModel {

    private String SendType;
    private String TableID;

    public SendNotificationReq() {
        super();
        SendType = "1";
        TableID = "";
    }

    public String getSendType() {
        return SendType;
    }

    public void setSendType(String sendType) {
        SendType = sendType;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }
}
