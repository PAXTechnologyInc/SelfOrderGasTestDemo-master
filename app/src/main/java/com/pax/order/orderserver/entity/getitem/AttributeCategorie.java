package com.pax.order.orderserver.entity.getitem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AttributeCategorie implements Serializable {
    private String Type;
    private List<AttributeValue> AttributeValue;



    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public List<com.pax.order.orderserver.entity.getitem.AttributeValue> getAttributeValue() {
        return AttributeValue;
    }

    public void setAttributeValue(List<com.pax.order.orderserver.entity.getitem.AttributeValue> attributeValue) {
        AttributeValue = attributeValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeCategorie that = (AttributeCategorie) o;
        return Objects.equals(Type, that.Type) &&
                Objects.equals(AttributeValue, that.AttributeValue);
    }

    @Override
    public int hashCode() {

        String hashSrc = Type;
        return hashSrc.hashCode();
    }

    public AttributeCategorie() {
        Type = "";
       // AttributeValue = new ArrayList<>();
    }
}
