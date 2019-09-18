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
package com.pax.order.orderserver.entity.getAllTableInfo;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.util.ArrayList;
import java.util.List;

/**
 * get table orders
 */
public class GetAllTableInfoRsp extends BaseRspModel {
    private List<TableInfo> Tables;
    private String ExtDataList;

    public GetAllTableInfoRsp() {
        super();
        Tables = new ArrayList<>();
        ExtDataList = "";
    }

    public List<TableInfo> getTables() {
        return Tables;
    }

    public void setTables(List<TableInfo> tables) {
        Tables = tables;
    }

    public String getExtDataList() {
        return ExtDataList;
    }

    public void setExtDataList(String extDataList) {
        ExtDataList = extDataList;
    }
}
