package com.pax.order.menu;

import android.content.Intent;
import android.support.annotation.VisibleForTesting;

import com.pax.order.orderserver.entity.setting.SettingList;
import com.pax.posdk.TransType;

import java.util.List;
import java.util.Set;

public class GetFixedTip {
    final private String[] settingName =
            {"FixedTipPercentage", "FixedTipThreshold", "FixedTipSwitch", "Surcharge"};

    private int tipPercentage;
    private int tipThreshold;
    private boolean tipSwich;
    private int surPercentage;


    private boolean settingFlag;

    private static final GetFixedTip ourInstance = new GetFixedTip();

    public static GetFixedTip getInstance() {
        return ourInstance;
    }

    private GetFixedTip() {
        settingFlag = false;
    }

    public void parseSettingParam(List<SettingList> settingList){
        for (SettingList set:settingList) {
            for (String name:settingName) {
                if((null != set.getSettingName()) && (name.equals(set.getSettingName()))){
                    if (settingName[0].equals(name)) {
                        setTipPercentage(Integer.parseInt(set.getSettingValue()));
                    } else if (settingName[1].equals(name)) {
                        setTipThreshold(Integer.parseInt(set.getSettingValue()));
                    } else if (settingName[2].equals(name)) {
                        String mValue = set.getSettingValue();
                        if (mValue != null && mValue.equals("Enable")) {
                            setTipSwich(true);
                        } else {
                            setTipSwich(false);
                        }
                    }else if(settingName[3].equals(name)){
                        setSurPercentage(Integer.parseInt(set.getSettingValue()));
                    }
                }

            }
        }
        setSettingFlag(true);
    }

    public int orderGetFixTipValue(int guestCnt){
        int mTip = 0;
        if(!isTipSwich() || guestCnt < getTipThreshold()) return mTip;
        return getTipPercentage();
    }

    public int getTipPercentage() {
        return tipPercentage;
    }

    public void setTipPercentage(int tipPercentage) {
        if(tipPercentage>=0 && tipPercentage <= 100) {
            this.tipPercentage = tipPercentage;
        }
    }

    public int getTipThreshold() {
        return tipThreshold;
    }

    public void setTipThreshold(int tipThreshold) {
        if(tipThreshold >= 0) {
            this.tipThreshold = tipThreshold;
        }
    }

    public boolean isTipSwich() {
        return tipSwich;
    }

    public void setTipSwich(boolean tipSwich) {
        this.tipSwich = tipSwich;
    }

    public int getSurPercentage() {
        return surPercentage;
    }

    public void setSurPercentage(int surPercentage) {
        this.surPercentage = surPercentage;
    }

    public boolean isSettingFlag() {
        return settingFlag;
    }

    public void setSettingFlag(boolean settingFlag) {
        this.settingFlag = settingFlag;
    }
}
