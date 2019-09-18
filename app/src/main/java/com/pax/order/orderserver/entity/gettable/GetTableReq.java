package com.pax.order.orderserver.entity.gettable;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;

public class GetTableReq extends BaseReqModel {
    String TableID ;

    public GetTableReq() {
        TableID = "";
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }
}
