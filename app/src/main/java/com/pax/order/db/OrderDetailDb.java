package com.pax.order.db;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.pax.order.entity.OrderDetail;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.entity.getorderdetail.AttrInItemInOrder;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;

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
 * 2018/10/10 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public final class OrderDetailDb {
    private static final String TAG = "OrderDetailDb";

    private RuntimeExceptionDao<OrderDetail, Integer> mOrderDetailDao = null;
    private RuntimeExceptionDao<ItemInOrder, Integer> mItemInOrderDao = null;
    private RuntimeExceptionDao<AttrInItemInOrder, Integer> mAttrInItemInOrderDao = null;

    private final BaseDbHelper mDbHelper;

    private static OrderDetailDb sInstance;

    private OrderDetailDb() {
        mDbHelper = BaseDbHelper.getInstance();
    }

    /**
     * get the Singleton of the DB Helper
     *
     * @return the Singleton of DB helper
     */
    public static synchronized OrderDetailDb getInstance() {
        if (sInstance == null) {
            sInstance = new OrderDetailDb();
        }

        return sInstance;
    }

    private RuntimeExceptionDao<OrderDetail, Integer> getOrderDetailDao() {
        if (mOrderDetailDao == null) {
            mOrderDetailDao = mDbHelper.getRuntimeExceptionDao(OrderDetail.class);
        }
        return mOrderDetailDao;
    }

    private RuntimeExceptionDao<ItemInOrder, Integer> getItemInOrderDao() {
        if (mItemInOrderDao == null) {
            mItemInOrderDao = mDbHelper.getRuntimeExceptionDao(ItemInOrder.class);
        }
        return mItemInOrderDao;
    }

    private RuntimeExceptionDao<AttrInItemInOrder, Integer> getAttrInItemInOrderDao() {
        if (mAttrInItemInOrderDao == null) {
            mAttrInItemInOrderDao = mDbHelper.getRuntimeExceptionDao(AttrInItemInOrder.class);
        }
        return mAttrInItemInOrderDao;
    }


    public boolean saveOrderDetailData(OrderDetail orderDetail) {
        try {
            RuntimeExceptionDao<OrderDetail, Integer> orderDetailDao = getOrderDetailDao();
            orderDetailDao.create(orderDetail);

            RuntimeExceptionDao<ItemInOrder, Integer> itemInOrderDao = getItemInOrderDao();
            RuntimeExceptionDao<AttrInItemInOrder, Integer> attrInItemInOrderDao = getAttrInItemInOrderDao();
            if (null != orderDetail.getmItemInOrdersList()) {
                for (ItemInOrder itemInOrder : orderDetail.getmItemInOrdersList()) {
                    itemInOrder.setOrderDetail(orderDetail);
                    itemInOrderDao.create(itemInOrder);
                    if (null != itemInOrder.getAttributeValue()) {
                        for (AttrInItemInOrder attrInItemInOrder : itemInOrder.getAttributeValue()) {
                            attrInItemInOrder.setItemInOrder(itemInOrder);
                            attrInItemInOrderDao.create(attrInItemInOrder);
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            AppLog.e(TAG, "", e);
            return false;
        }
        return true;
    }

    public OrderDetail readOrderDetailByTraceNum(String traceNum) {
        OrderDetail orderDetail = null;
        try {
            RuntimeExceptionDao<OrderDetail, Integer> dao = getOrderDetailDao();
            QueryBuilder<OrderDetail, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(OrderDetail.TRACE_NUM_NAME, traceNum);
            orderDetail = queryBuilder.queryForFirst();
            if (null != orderDetail) {
                orderDetail.setmItemInOrdersList(orderDetail.getItemInOrdersLisformDb());
                for (ItemInOrder itemInOrder : orderDetail.getmItemInOrdersList()) {
                    itemInOrder.setAttributeValue(itemInOrder.getAttrInOrdersLisformDb());
                }
            }
            return orderDetail;
        } catch (SQLException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public List<OrderDetail> findAllOrderDetailData() {
        List<OrderDetail> list = null;
        try {
            RuntimeExceptionDao<OrderDetail, Integer> dao = getOrderDetailDao();
            list = dao.queryForAll();
            if (null != list) {
                for (OrderDetail orderDetail : list) {
                    orderDetail.setmItemInOrdersList(orderDetail.getItemInOrdersLisformDb());
                    for (ItemInOrder itemInOrder : orderDetail.getmItemInOrdersList()) {
                        itemInOrder.setAttributeValue(itemInOrder.getAttrInOrdersLisformDb());
                    }
                }
            }
            return list;
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
        return null;
    }

    public void deleteOrderDetailByTraceNum(String traceNum) {
        try {
            RuntimeExceptionDao<OrderDetail, Integer> dao = getOrderDetailDao();
            dao.delete(readOrderDetailByTraceNum(traceNum));
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }

    public void deleteOrderDetail() {
        try {
            RuntimeExceptionDao<OrderDetail, Integer> dao = getOrderDetailDao();
            dao.delete(dao.queryForAll());

            RuntimeExceptionDao<ItemInOrder, Integer> dao1 = getItemInOrderDao();
            dao1.delete(dao1.queryForAll());

            RuntimeExceptionDao<AttrInItemInOrder, Integer> dao2 = getAttrInItemInOrderDao();
            dao2.delete(dao2.queryForAll());
        } catch (RuntimeException e) {
            AppLog.w(TAG, "", e);
        }
    }

}
