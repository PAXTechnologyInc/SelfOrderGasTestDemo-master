package com.pax.order.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pax.order.FinancialApplication;
import com.pax.order.db.upgrade.DbUpgrader;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.Order;
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.PayData;
import com.pax.order.entity.PrintData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.entity.Transaction;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.entity.getorderdetail.AttrInItemInOrder;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;

import java.sql.SQLException;

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
 * 2018/8/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
final class BaseDbHelper extends OrmLiteSqliteOpenHelper {
    protected static final String TAG = "DB";
    // DB Name
    private static final String DATABASE_NAME = "data.db";
    // DB version
    private static final int DATABASE_VERSION = 1;
    // DB Upgrader packagePath
    private static final String UPGRADER_PATH = "com.pax.order.db.upgrade.db";
    private static BaseDbHelper sInstance = null;

    private BaseDbHelper() {
        super(FinancialApplication.getApp(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, OpenTicket.class);
            TableUtils.createTableIfNotExists(connectionSource, SelectGoods.class);
            TableUtils.createTableIfNotExists(connectionSource, StorageGoods.class);
            TableUtils.createTableIfNotExists(connectionSource, Order.class);
            TableUtils.createTableIfNotExists(connectionSource, PrintData.class);
            TableUtils.createTableIfNotExists(connectionSource, PayData.class);
            TableUtils.createTableIfNotExists(connectionSource, Transaction.class);
            TableUtils.createTableIfNotExists(connectionSource, OrderDetail.class);
            TableUtils.createTableIfNotExists(connectionSource, ItemInOrder.class);
            TableUtils.createTableIfNotExists(connectionSource, AttrInItemInOrder.class);
        } catch (SQLException e) {
            AppLog.e(TAG, "", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                DbUpgrader.upgrade(sqliteDatabase, connectionSource, i, i + 1, UPGRADER_PATH);
            }
        } catch (IllegalArgumentException e) {
            AppLog.e(TAG, "", e);
        }
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized BaseDbHelper getInstance() {
        if (sInstance == null) {
            sInstance = new BaseDbHelper();
        }

        return sInstance;
    }
}
