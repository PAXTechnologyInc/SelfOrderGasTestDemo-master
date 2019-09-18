package com.pax.order.orderserver.entity.getitem;

import java.io.Serializable;

public class AttributeValue implements Serializable {
    private String Id;
    private String Type;
    private String Name;
    private String Price;

    public AttributeValue() {
        Id = "";
        Type = "";
        Name = "";
        Price = "";
    }

    public String getID() {
        return Id;
    }

    public void setID(String Id) {
        this.Id = Id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
