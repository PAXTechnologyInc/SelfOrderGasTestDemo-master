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

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.util.ArrayList;
import java.util.List;

/**
 * get table orders
 */
public class GetTableOrdersRsp extends BaseRspModel {
    private List<Table> Tables;
    private String ExtDataList;

    public GetTableOrdersRsp() {
        super();
        Tables = new ArrayList<>();
        ExtDataList = "";
    }

    public List<Table> getTables() {
        return Tables;
    }

    public void setTables(List<Table> tables) {
        Tables = tables;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
