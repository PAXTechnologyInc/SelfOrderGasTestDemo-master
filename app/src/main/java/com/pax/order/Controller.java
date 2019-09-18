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
 * 2018/11/15 	        chenyr           	Create/Add/Modify/Delete
 * ============================================================================
 */

package com.pax.order;

import com.pax.order.entity.OrderDetail;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static Controller controller;
    private List<OrderDetail> mOrderDetailList = new ArrayList<>();
    private String TraceNum;
    private boolean IsOpen;

    private Controller() {

    }

    public static synchronized Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public List<OrderDetail> getOrderDetailList() {
        return mOrderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.mOrderDetailList = orderDetailList;
    }

    public void clearOrderDetailList() {
        this.mOrderDetailList.clear();
    }

    public void addOrderDetailList(OrderDetail orderDetail) {
        this.mOrderDetailList.add(orderDetail);
    }

    public String getTraceNum() {
        return TraceNum;
    }

    public void setTraceNum(String traceNum) {
        TraceNum = traceNum;
    }

    public boolean getIsOpen() {
        return IsOpen;
    }

    public void setIsOpen(boolean isOpen) {
        IsOpen = isOpen;
    }


}
