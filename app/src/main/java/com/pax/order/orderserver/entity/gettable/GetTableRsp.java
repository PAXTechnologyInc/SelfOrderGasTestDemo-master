package com.pax.order.orderserver.entity.gettable;

import com.pax.order.orderserver.entity.baseModel.BaseRspModel;

import java.util.List;

public class GetTableRsp extends BaseRspModel {
    List<TablePro>Tables ;


    public List<TablePro> getTablePros() {
        return Tables;
    }

    public void setTablePros(List<TablePro> tablePros) {
        this.Tables = tablePros;
    }
}
