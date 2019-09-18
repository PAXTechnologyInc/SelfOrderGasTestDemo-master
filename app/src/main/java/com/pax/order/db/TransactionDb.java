package com.pax.order.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.pax.order.entity.Transaction;
import com.pax.order.logger.AppLog;

import java.util.List;

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
 * 2018/9/10 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public final class TransactionDb {
    private static final String TAG = "TransactionDb";

    private RuntimeExceptionDao<Transaction, Integer> mTransactionDb = null;

    private final BaseDbHelper mDbHelper;

    private static TransactionDb sInstance;

    private TransactionDb() {
        mDbHelper = BaseDbHelper.getInstance();
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized TransactionDb getInstance() {
        if (sInstance == null) {
            sInstance = new TransactionDb();
        }

        return sInstance;
    }

    private RuntimeExceptionDao<Transaction, Integer> getTransactionDao() {
        if (mTransactionDb == null) {
            mTransactionDb = mDbHelper.getRuntimeExceptionDao(Transaction.class);
        }
        return mTransactionDb;
    }

    public boolean saveTransaction(Transaction payData) {
        try {
            RuntimeExceptionDao<Transaction, Integer> dao = getTransactionDao();
            dao.create(payData);
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public boolean saveTransactionList(List<Transaction> list) {
        try {
            RuntimeExceptionDao<Transaction, Integer> dao = getTransactionDao();
            for (Transaction item : list) {
                dao.create(item);
            }
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public List<Transaction> readTransactionList() {
        List<Transaction> list = null;
        try {
            RuntimeExceptionDao<Transaction, Integer> dao = getTransactionDao();
            list = dao.queryForAll();
            return list;
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public void clearTransaction() {
        try {
            RuntimeExceptionDao<Transaction, Integer> dao = getTransactionDao();
            dao.delete(dao.queryForAll());
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
        }
        return;
    }
}
