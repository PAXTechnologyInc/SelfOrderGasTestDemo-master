package com.pax.order.orderserver.entity.getstoreinfo;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

public class GetStoreInfoRsp extends BaseRspModel {
    private String StoreID;
    private String Name;
    private String Phone;
    private String Country;
    private String State;
    private String City;
    private String Address1;
    private String Address2;
    private String ZipCode;
    private String LogoURL;
    private String Status;


    public GetStoreInfoRsp() {
        super();
        StoreID = "";
        Name = "";
        Phone = "";
        Country = "";
        State = "";
        City = "";
        Address1 = "";
        Address2 = "";
        ZipCode = "";
        LogoURL = "";
        Status = "";

    }

    public String getStoreID() {
        return StoreID;

    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getLogoURL() {
        return LogoURL;
    }

    public void setLogoURL(String logoURL) {
        LogoURL = logoURL;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
