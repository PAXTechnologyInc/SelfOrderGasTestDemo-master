package com.pax.order.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.logger.AppLog;
import com.pax.order.util.DoubleUtils;

import java.sql.SQLException;
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
 * 2018/8/15 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public final class OpenTicketDb {
    private static final String TAG = "OpenTicketDb";

    private RuntimeExceptionDao<OpenTicket, Integer> mOpenTicketsDao = null;
    private RuntimeExceptionDao<SelectGoods, Integer> mSelectGoodsDao = null;
    private RuntimeExceptionDao<StorageGoods, Integer> mStorageGoodsDao = null;
    private final BaseDbHelper mDbHelper;

    private static OpenTicketDb sInstance;

    private OpenTicketDb() {
        mDbHelper = BaseDbHelper.getInstance();
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized OpenTicketDb getInstance() {
        if (sInstance == null) {
            sInstance = new OpenTicketDb();
        }

        return sInstance;
    }

    private RuntimeExceptionDao<OpenTicket, Integer> getOpenTicketsDao() {
        if (mOpenTicketsDao == null) {
            mOpenTicketsDao = mDbHelper.getRuntimeExceptionDao(OpenTicket.class);
        }
        return mOpenTicketsDao;
    }

    private RuntimeExceptionDao<SelectGoods, Integer> getSelectGoodsDao() {
        if (mSelectGoodsDao == null) {
            mSelectGoodsDao = mDbHelper.getRuntimeExceptionDao(SelectGoods.class);
        }
        return mSelectGoodsDao;
    }

    private RuntimeExceptionDao<StorageGoods, Integer> getStorageGoodsDao() {
        if (mStorageGoodsDao == null) {
            mStorageGoodsDao = mDbHelper.getRuntimeExceptionDao(StorageGoods.class);
        }
        return mStorageGoodsDao;
    }

    public boolean insertTicketData(OpenTicket openTicket) {
        try {
            RuntimeExceptionDao<OpenTicket, Integer> dao = getOpenTicketsDao();
            dao.create(openTicket);

            RuntimeExceptionDao<SelectGoods, Integer> dao1 = getSelectGoodsDao();
            for (SelectGoods selectGoods : openTicket.getSelectList()) {
                selectGoods.setOpenTicket(openTicket);
                dao1.create(selectGoods);
            }

            RuntimeExceptionDao<StorageGoods, Integer> dao2 = getStorageGoodsDao();
            QueryBuilder<StorageGoods, Integer> queryBuilder = dao2.queryBuilder();
            for (SelectGoods selectGoods : openTicket.getSelectList()) {
                StorageGoods storageGoods = new StorageGoods();
                storageGoods.setId(selectGoods.getId());
                storageGoods.setAttributeId(selectGoods.getAttributeId());
                storageGoods.setAtrributeidName(selectGoods.getAtrributeidName());
                storageGoods.setAttributePrice(selectGoods.getAttributePrice());
                storageGoods.setAttributeTaxAmt(selectGoods.getAttributeTaxAmt());
                storageGoods.setName(selectGoods.getName());
                storageGoods.setPrice(selectGoods.getPrice());
                //storageGoods.setmMergePrice(selectGoods.getPrice());
                storageGoods.setmMergePrice(selectGoods.getmTotalAmt() / selectGoods.getQuantity());
                storageGoods.setTaxAmt(selectGoods.getTaxAmt());
                storageGoods.setUnitTaxAmt(selectGoods.getUnitTaxAmt());
                storageGoods.setQuantity(selectGoods.getQuantity());
                storageGoods.setmMergeQuantity(selectGoods.getQuantity());
                storageGoods.setUnPaidQuantity(selectGoods.getUnPaidQuantity());
                storageGoods.setPrePaidQuantity(0);
                storageGoods.setmTotalAmt(selectGoods.getmTotalAmt());
                //storageGoods.setmTotalAmt(selectGoods.getQuantity() * selectGoods.getPrice());
                //       storageGoods.setmUnpaidlAmt(selectGoods.getUnPaidQuantity() * selectGoods.getPrice());
                storageGoods.setmUnpaidlAmt(selectGoods.getmTotalAmt());
                storageGoods.setmPrePaidAmt(selectGoods.getPrePaidQuantity() * selectGoods.getPrice());
                storageGoods.setUnpaidlTaxAmt(selectGoods.getTaxAmt());
                storageGoods.setPrePaidTaxAmt(0.00);

                if (null != storageGoods.getAttributeId() && !storageGoods.getAttributeId().isEmpty()) {
                    queryBuilder.where().eq(storageGoods.GOODSID_NAME, storageGoods.getId())
                            .and().eq(storageGoods.ATTRIBUTEID_NAME, storageGoods.getAttributeId());
                } else {
                    queryBuilder.where().eq(storageGoods.GOODSID_NAME, storageGoods.getId());
                }

                StorageGoods item = queryBuilder.queryForFirst();
                if (null != item) {
                    item.setQuantity(item.getQuantity() + storageGoods.getQuantity());
                    item.setPrePaidQuantity(0);
                    item.setmMergeQuantity(item.getmMergeQuantity() + storageGoods.getmMergeQuantity());
                    item.setmTotalAmt(item.getmTotalAmt() + storageGoods.getmTotalAmt());
                    item.setmUnpaidlAmt(item.getmUnpaidlAmt() + storageGoods.getmUnpaidlAmt());
                    item.setmPrePaidAmt(item.getmPrePaidAmt() + storageGoods.getmPrePaidAmt());
                    item.setmMergePrice(item.getmTotalAmt() / item.getmMergeQuantity());
                    item.setTaxAmt(item.getTaxAmt() + storageGoods.getTaxAmt());
                    item.setUnpaidlTaxAmt(item.getUnpaidlTaxAmt() + storageGoods.getUnpaidlTaxAmt());
                    item.setPrePaidTaxAmt(item.getPrePaidTaxAmt() + storageGoods.getPrePaidTaxAmt());
                    dao2.update(item);
                } else {
                    dao2.create(storageGoods);
                }
            }

        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public OpenTicket findOpenTicketByTraceNum(String traceNum) {
        OpenTicket openTicket = null;
        try {
            RuntimeExceptionDao<OpenTicket, Integer> dao = getOpenTicketsDao();
            QueryBuilder<OpenTicket, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(OpenTicket.TRACE_NUM_NAME, traceNum);
            openTicket = queryBuilder.queryForFirst();
            if (null != openTicket) {
                openTicket.setSelectList(openTicket.getSelectListformDb());
            }
            return openTicket;
        } catch (SQLException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public void deleteOpenTicketByTraceNum(String traceNum) {
        try {
            RuntimeExceptionDao<OpenTicket, Integer> dao = getOpenTicketsDao();
            dao.delete(findOpenTicketByTraceNum(traceNum));
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }

    public List<OpenTicket> findAllOpenTicketData() {
        List<OpenTicket> list = null;
        try {
            RuntimeExceptionDao<OpenTicket, Integer> dao = getOpenTicketsDao();
            list = dao.queryForAll();
            if (null != list) {
                for (OpenTicket openTicket : list) {
                    openTicket.setSelectList(openTicket.getSelectListformDb());
                }
            }
            return list;
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public void deleteAllOpenTicket() {
        try {
            RuntimeExceptionDao<OpenTicket, Integer> dao = getOpenTicketsDao();
            dao.delete(findAllOpenTicketData());

            RuntimeExceptionDao<SelectGoods, Integer> dao1 = getSelectGoodsDao();
            dao1.delete(dao1.queryForAll());
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }

    public boolean updateSelectGoods(List<StorageGoods> list) {
        if (null == list) {
            return false;
        }
        try {
            RuntimeExceptionDao<StorageGoods, Integer> dao = getStorageGoodsDao();
            for (StorageGoods item : list) {
                dao.update(item);
            }
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public List<StorageGoods> findAllUnPaidGoods() {
        try {
            RuntimeExceptionDao<StorageGoods, Integer> dao = getStorageGoodsDao();
            QueryBuilder<StorageGoods, Integer> queryBuilder = dao.queryBuilder();
            return queryBuilder.where().gt(StorageGoods.UNPAIDNUM_NAME, 0).query();
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<StorageGoods> findAllPrePaidGoods() {
        try {
            RuntimeExceptionDao<StorageGoods, Integer> dao = getStorageGoodsDao();
            QueryBuilder<StorageGoods, Integer> queryBuilder = dao.queryBuilder();
            return queryBuilder.where().gt(StorageGoods.PREPAYNUM_NAME, 0).query();
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<StorageGoods> findAllStorageGoods() {
        try {
            RuntimeExceptionDao<StorageGoods, Integer> dao = getStorageGoodsDao();
            return dao.queryForAll();
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return null;
        }
    }

    public void clearPrePaidGoods() {
        List<StorageGoods> list = findAllPrePaidGoods();
        if (null == list) {
            return;
        }
        for (StorageGoods item : list) {
            item.setmTotalAmt(DoubleUtils.round(item.getmTotalAmt() - item.getmPrePaidAmt()));
            item.setTaxAmt(DoubleUtils.round(item.getTaxAmt() - item.getPrePaidTaxAmt()));
            item.setPrePaidQuantity(0);
            item.setmPrePaidAmt(0);
            item.setPrePaidTaxAmt(0);
        }
        updateSelectGoods(list);
    }

    public void deleteAllStorageGoods() {
        try {
            RuntimeExceptionDao<StorageGoods, Integer> dao = getStorageGoodsDao();
            dao.delete(findAllStorageGoods());
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }
}
