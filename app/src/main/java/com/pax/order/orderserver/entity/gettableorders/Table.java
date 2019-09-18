/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 18-9-13 下午8:25  	     JoeyTan           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.orderserver.entity.gettableorders;

import java.util.ArrayList;
import java.util.List;

/**
 * table detail
 */
public class Table {
    private String TableID;
    private String Area;
    private String Status;
    private boolean IsHaveUnpaidOrder;
    private List<OrderInTable> Orders;

    public Table() {
        TableID = "";
        Area = "";
        Status = "";
        IsHaveUnpaidOrder = false;
        Orders = new ArrayList<>();
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getIsOpened() {
        return Status;
    }

    public void setIsOpened(String isOpened) {
        Status = isOpened;
    }

    public List<OrderInTable> getOrders() {
        return Orders;
    }

    public void setOrders(List<OrderInTable> orders) {
        Orders = orders;
    }

    public boolean isHaveUnpaidOrde() {
        return IsHaveUnpaidOrder;
    }

    public void setHaveUnpaidOrde(boolean haveUnpaidOrde) {
        IsHaveUnpaidOrder = haveUnpaidOrde;
    }
}
