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
package com.pax.order.orderserver.entity.getorderdetail;

import java.util.ArrayList;
import java.util.List;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

/**
 * get order detail response
 */
public class GetOrderDetailRsp extends BaseRspModel {
    private List<SPOrderDetail> Orders;
    private String ExtDataList;

    public GetOrderDetailRsp() {
        super();
        Orders = new ArrayList<>();
    }

    public List<SPOrderDetail> getOrders() {
        return Orders;
    }

    public void setOrders(List<SPOrderDetail> orders) {
        Orders = orders;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
