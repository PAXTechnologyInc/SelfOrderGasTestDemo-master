package com.pax.order.settings;

import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;

public class GetStoreInfoInstance {

    private GetStoreInfoRsp  storeInfo ;
    private static final GetStoreInfoInstance ourInstance = new GetStoreInfoInstance();

    public static GetStoreInfoInstance getInstance() {
        return ourInstance;
    }

    private GetStoreInfoInstance() {

    }
    public void setStoreInfo(GetStoreInfoRsp storeInfo){
        this.storeInfo = storeInfo;
    }

    public GetStoreInfoRsp getStoreInfo(){
        return storeInfo;
    }

}
