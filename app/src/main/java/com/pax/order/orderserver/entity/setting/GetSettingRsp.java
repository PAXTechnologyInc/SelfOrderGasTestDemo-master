package com.pax.order.orderserver.entity.setting;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.util.ArrayList;
import java.util.List;

public class GetSettingRsp extends BaseRspModel {
    private String TotalCount;
    private String CurrentCount;
    private List<SettingList>  Settings ;

    public GetSettingRsp() {
       // Settings = new ArrayList<>();
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(String currentCount) {
        CurrentCount = currentCount;
    }

    public List<SettingList> getSettings() {
        return Settings;
    }

    public void setSettings(List<SettingList> settings) {
        Settings = settings;
    }
}
