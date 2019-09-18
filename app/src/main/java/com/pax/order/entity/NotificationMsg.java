/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/10/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.entity;

import com.pax.order.orderserver.entity.getitem.Item;

import java.io.Serializable;
import java.util.List;

public class NotificationMsg implements Serializable {

    private String MessageNum;
    private String MessageType;
    private String TableID;
    private String TraceNum;
    private String TerminalID;
    //private String IsOpen;
    private String OrderNumber;
    private String DeviceID;
    private String TableStauts;
    private boolean IsHaveUnpaidOrder;
    private String TableNum;
    private List<Item> Items ;

    class Items{
        private String ItemName;
        private String ItemId;
        private String AttributeId;
        private String Quantity;
        private String OrderLineID;
        private String DiscountAmt;

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public String getItemId() {
            return ItemId;
        }

        public void setItemId(String itemId) {
            ItemId = itemId;
        }

        public String getAttributeId() {
            return AttributeId;
        }

        public void setAttributeId(String attributeId) {
            AttributeId = attributeId;
        }

        public String getQuantity() {
            return Quantity;
        }

        public void setQuantity(String quantity) {
            Quantity = quantity;
        }

        public String getOrderLineID() {
            return OrderLineID;
        }

        public void setOrderLineID(String orderLineID) {
            OrderLineID = orderLineID;
        }

        public String getDiscountAmt() {
            return DiscountAmt;
        }

        public void setDiscountAmt(String discountAmt) {
            DiscountAmt = discountAmt;
        }
    }




    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public NotificationMsg() {

    }

    public String getMessageNum() {
        return MessageNum;
    }

    public void setMessageNum(String messageNum) {
        MessageNum = messageNum;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getTraceNum() {
        return TraceNum;
    }

    public void setTraceNum(String traceNum) {
        TraceNum = traceNum;
    }

    public String getTerminalID() {
        return TerminalID;
    }

    public void setTerminalID(String terminalID) {
        TerminalID = terminalID;
    }

//    public String getIsOpen() {
//        return IsOpen;
//    }
//
//    public void setIsOpen(String isOpen) {
//        IsOpen = isOpen;
//    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getTableStauts() {
        return TableStauts;
    }

    public void setTableStauts(String tableStatus) {
        TableStauts = tableStatus;
    }

    public boolean isHaveUnpaidOrder() {
        return IsHaveUnpaidOrder;
    }

    public void setHaveUnpaidOrder(boolean haveUnpaidOrder) {
        IsHaveUnpaidOrder = haveUnpaidOrder;
    }

    public List<Item> getItems() {
        return Items;
    }

    public void setItems(List<Item> items) {
        Items = items;
    }

    public String getTableNum() {
        return TableNum;
    }

    public void setTableNum(String tableNum) {
        TableNum = tableNum;
    }
}