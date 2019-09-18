package com.pax.order.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pax.order.entity.PayData;
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
 * 2018/8/21 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public final class PayDataDb {
    private static final String TAG = "PayDataDb";

    private RuntimeExceptionDao<PayData, Integer> mPayDataDao = null;

    private final BaseDbHelper mDbHelper;

    private static PayDataDb sInstance;

    private PayDataDb() {
        mDbHelper = BaseDbHelper.getInstance();
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized PayDataDb getInstance() {
        if (sInstance == null) {
            sInstance = new PayDataDb();
        }

        return sInstance;
    }

    private RuntimeExceptionDao<PayData, Integer> getPayDataDao() {
        if (mPayDataDao == null) {
            mPayDataDao = mDbHelper.getRuntimeExceptionDao(PayData.class);
        }
        return mPayDataDao;
    }

    public boolean savePayData(PayData payData) {
        try {
            RuntimeExceptionDao<PayData, Integer> dao = getPayDataDao();
            if (dao.idExists(payData.getId())) {
                dao.update(payData);
            } else {
                dao.create(payData);
            }
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public PayData readPayData() {
        try {
            RuntimeExceptionDao<PayData, Integer> dao = getPayDataDao();
            QueryBuilder<PayData, Integer> queryBuilder = dao.queryBuilder();
            return queryBuilder.queryForFirst();
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearPayData() {
        try {
            RuntimeExceptionDao<PayData, Integer> dao = getPayDataDao();
            dao.delete(dao.queryForAll());
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
        }
        return;
    }
}
