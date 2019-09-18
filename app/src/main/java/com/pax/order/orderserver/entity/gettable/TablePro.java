package com.pax.order.orderserver.entity.gettable;

public class TablePro {
    private String ID;
    private String Name;
    private String Num;
    private String Status;
    private String Seats;
    private String Area;
    private String Floor;
    private String DefaultEmployeeID;
    private String DefaultEmployeeName;
    private String Node;
    private boolean IsHaveUnpaidOrder;

    public TablePro() {
        ID = "";
        Name = "";
        Num = "";
        Status = "";
        Seats = "";
        Area = "";
        Floor = "";
        DefaultEmployeeID = "";
        DefaultEmployeeName = "";
        Node = "";
        IsHaveUnpaidOrder = false;
    }

    public TablePro(String id, String name, String num, String status, String seats, String area, String floor ) {
        ID = id;
        Name = name;
        Num = num;
        Status = status;
        Seats = seats;
        Area = area;
        Floor = floor;
        DefaultEmployeeID = "";
        DefaultEmployeeName = "";
        Node = "";
        IsHaveUnpaidOrder = false;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNum() {
        return Num;
    }

    public void setNum(String num) {
        Num = num;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getSeats() {
        return Seats;
    }

    public void setSeats(String seats) {
        Seats = seats;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public String getDefaultEmployeeID() {
        return DefaultEmployeeID;
    }

    public void setDefaultEmployeeID(String defaultEmployeeID) {
        DefaultEmployeeID = defaultEmployeeID;
    }

    public String getNode() {
        return Node;
    }

    public void setNode(String node) {
        Node = node;
    }

    public String getDefaultEmployeeName() {
        return DefaultEmployeeName;
    }

    public void setDefaultEmployeeName(String defaultEmployeeName) {
        DefaultEmployeeName = defaultEmployeeName;
    }

    public boolean isHaveUnpaidOrder() {
        return IsHaveUnpaidOrder;
    }

    public void setHaveUnpaidOrder(boolean haveUnpaidOrder) {
        IsHaveUnpaidOrder = haveUnpaidOrder;
    }
}
