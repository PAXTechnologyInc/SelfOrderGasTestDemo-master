package com.pax.order.settings;

import com.pax.order.orderserver.entity.gettable.TablePro;

import java.util.ArrayList;
import java.util.List;

public class GetTableInstance {
    private static final GetTableInstance ourInstance = new GetTableInstance();
    private List<TablePro> tableProList = new ArrayList<>();
    public static GetTableInstance getInstance() {
        return ourInstance;
    }

    private GetTableInstance() {
    }

    public List<TablePro> getTableProList() {
        return tableProList;
    }

    public void setTableProList(List<TablePro> tableProList) {
        //清除 num号为空的数据
        this.tableProList.clear();
        for(TablePro tp:tableProList){
            if(tp.getNum() != null && !tp.getNum().isEmpty()){
                this.tableProList.add(tp);
            }
        }
    }

    public String[] getTableName(){
        if(tableProList==null || tableProList.size() == 0) return null;
        String[] tableName = new String[tableProList.size()];

        for(int i = 0; i<tableProList.size();i++){
//            tableName[i]= String.format("%s(%s)",tableProList.get(i).getName(),tableProList.get(i).getNum());
            tableName[i]= String.format("%s (%s)",tableProList.get(i).getNum(),tableProList.get(i).getName());
        }
        return tableName;
    }

    public String[] getTableNum(){
        if(tableProList==null || tableProList.size() == 0) return null;
        String[] num = new String[tableProList.size()];

        for(int i = 0; i<tableProList.size();i++){
            num[i] = tableProList.get(i).getNum();
        }
        return num;
    }

    public String GetTableIdByNum(String num){
        if(num == null) return null;
        for(TablePro table : tableProList){
            if(num.equals(table.getNum())){
                return table.getID();
            }
        }
        return null;
    }
}
