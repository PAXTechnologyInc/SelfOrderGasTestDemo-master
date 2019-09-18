package com.pax.order.db.upgrade.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.support.ConnectionSource;
import com.pax.order.db.upgrade.DbUpgrader;

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
 * 2018/9/13 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================		
 */
public class Upgrade1To2 extends DbUpgrader {

//    private static final Map<String, Object> def = new TreeMap<>();
//    static {
//        def.put("test", "12");
//    };
    @Override
    protected void upgrade(SQLiteDatabase db, ConnectionSource cs) {
        //根据实际版本情况新增、修改或删除表、字段
//        try {
//            DbUpgrader.upgradeTable(db, cs, PayData.class, OperationType.ADD, def);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }
}
