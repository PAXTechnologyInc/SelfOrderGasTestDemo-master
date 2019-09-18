package com.pax.order.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pax.order.entity.Order;
import com.pax.order.entity.PrintData;
import com.pax.order.logger.AppLog;

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
 * 2018/8/20 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public final class PrintDataDb {
    private static final String TAG = "PrintDataDb";

    private RuntimeExceptionDao<PrintData, Integer> mDetailOrderDao = null;
    private RuntimeExceptionDao<Order, Integer> mOrderDao = null;

    private final BaseDbHelper mDbHelper;

    private static PrintDataDb sInstance;

    private PrintDataDb() {
        mDbHelper = BaseDbHelper.getInstance();
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized PrintDataDb getInstance() {
        if (sInstance == null) {
            sInstance = new PrintDataDb();
        }

        return sInstance;
    }

    private RuntimeExceptionDao<PrintData, Integer> getDetailOrderDao() {
        if (mDetailOrderDao == null) {
            mDetailOrderDao = mDbHelper.getRuntimeExceptionDao(PrintData.class);
        }
        return mDetailOrderDao;
    }

    private RuntimeExceptionDao<Order, Integer> getOrderDao() {
        if (mOrderDao == null) {
            mOrderDao = mDbHelper.getRuntimeExceptionDao(Order.class);
        }
        return mOrderDao;
    }

    public boolean savePrintData(PrintData printData) {
        try {
            RuntimeExceptionDao<PrintData, Integer> dao = getDetailOrderDao();
            dao.create(printData);

            RuntimeExceptionDao<Order, Integer> dao1 = getOrderDao();
            for (Order order : printData.getOrders()) {
                order.setPrintData(printData);
                dao1.create(order);
            }

        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public PrintData readPrintData() {
        PrintData printData = null;
        try {
            RuntimeExceptionDao<PrintData, Integer> dao = getDetailOrderDao();
            QueryBuilder<PrintData, Integer> queryBuilder = dao.queryBuilder();
            printData = queryBuilder.queryForFirst();
            if (null != printData) {
                printData.setOrders(printData.getOrderformDb());
            }
            return printData;
        } catch (SQLException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public void deletePrintData() {
        try {
            RuntimeExceptionDao<PrintData, Integer> dao = getDetailOrderDao();
            dao.delete(dao.queryForAll());

            RuntimeExceptionDao<Order, Integer> dao1 = getOrderDao();
            dao1.delete(dao1.queryForAll());
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }

}
