package com.pax.order.orderserver.entity.setting;

public class SettingList {
    private String ID ;
    private String SettingType;
    private String SettingName;
    private String SettingValue;
    private String Note;

    public SettingList() {
        ID = "";
        SettingType = "";
        SettingName = "";
        SettingValue = "";
        Note = "";
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSettingType() {
        return SettingType;
    }

    public void setSettingType(String settingType) {
        SettingType = settingType;
    }

    public String getSettingName() {
        return SettingName;
    }

    public void setSettingName(String settingName) {
        SettingName = settingName;
    }

    public String getSettingValue() {
        return SettingValue;
    }

    public void setSettingValue(String settingValue) {
        SettingValue = settingValue;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
