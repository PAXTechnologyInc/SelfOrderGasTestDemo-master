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
 * 2018/8/15 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.payment;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.pax.order.FinancialApplication;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.IView;

import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class CheckMenuPresenter extends BasePresenter<IView> implements CheckMenuContract.Presenter {
    private static final String TAG = "CheckMenuPresenter";

    private final CheckMenuContract.View mCheckMenuView;
    private List<StorageGoods> mStorageGoodsList;

    private int mCount = 0;
    private double mCost = 0.0;


    public CheckMenuPresenter(@NonNull CheckMenuContract.View checkMenuView) {
        mCheckMenuView = checkMenuView;
        mCheckMenuView.setPresenter(this);

    }

    @Override
    public void start() {

        mStorageGoodsList = FinancialApplication.getOpenTicketDbHelper().findAllStorageGoods();
        List<SelectGoods> selectGoods = FinancialApplication.getOrderTicket().getSelectList();

        for(SelectGoods selectGood : selectGoods) {
            StorageGoods goods = new StorageGoods();
            goods.setId(selectGood.getId());
            goods.setQuantity(selectGood.getQuantity());
            goods.setPrice(selectGood.getPrice());
            goods.setTaxAmt(selectGood.getTaxAmt());
            goods.setCategoryId(selectGood.getCategoryId());
            goods.setCategoryName(selectGood.getCategoryName());
            goods.setName(selectGood.getName());
            mStorageGoodsList.add(goods);
        }
        getModifyTicket();

        for (StorageGoods storageGoods : mStorageGoodsList) {
            if (storageGoods.getQuantity() < 0)
                continue;
            System.out.println("ITEM: " + storageGoods.getName());
            mCount += storageGoods.getQuantity();
            if (storageGoods.getQuantity() == 0 && storageGoods.getReviewReduceQuantity() == 0) {//由折扣金额生成的商品
                mCost += storageGoods.getPrice();
            } else {
                mCost += DoubleUtils.mul(storageGoods.getQuantity(), storageGoods.getPrice());
            }
        }

        //显示view页

        System.out.println("CheckMenuPresenter storagegoodslist length: " + mStorageGoodsList.size());
        System.out.println("Count: " + mCount);
        System.out.println("Cost: " + mCost);
        mCheckMenuView.initView(mStorageGoodsList, mCount, mCost);
    }


    public void getModifyTicket() {
        List<OrderDetail> orderDetailList = FinancialApplication.getController().getOrderDetailList();
        if (orderDetailList == null) {
            return;
        }

        for (OrderDetail orderDetail : orderDetailList) {
            List<ItemInOrder> itemInOrderList = orderDetail.getmItemInOrdersList();
            if (itemInOrderList == null || itemInOrderList.isEmpty()) {
                continue; //return;
            }

            for (ItemInOrder itemInOrder : itemInOrderList) {
                if ("Amount".equals(itemInOrder.getDiscountType()) && Double.parseDouble(itemInOrder.getDiscountValue()) > 0) {
                    StorageGoods haveStorageGoods = getStorageGoodsById(itemInOrder, 2);
                    if (haveStorageGoods != null) {//合并负金额
                        haveStorageGoods.setPrice(DoubleUtils.sum(haveStorageGoods.getPrice(), DoubleUtils.sub(itemInOrder.getDiscountTax(), itemInOrder.getDiscountValue())));//取负数
                    } else {
                        StorageGoods storageGoods = new StorageGoods();
                        storageGoods.setId(Integer.parseInt(itemInOrder.getID()));

                        if (itemInOrder.getAttributeId() != null) {
                            storageGoods.setAttributeId(itemInOrder.getAttributeId());
                            storageGoods.setAtrributeidName(itemInOrder.getAttributeSku());
                        } else {
                            storageGoods.setAttributeId("");
                            storageGoods.setAtrributeidName("");
                        }

                        storageGoods.setPrice(DoubleUtils.sub(itemInOrder.getDiscountTax(), itemInOrder.getDiscountValue()));//取负数
                        storageGoods.setQuantity(0);

                        storageGoods.setName(itemInOrder.getName());
                        mStorageGoodsList.add(storageGoods);
                    }

                } else if (Integer.parseInt(itemInOrder.getQuantity()) < 0 && Double.parseDouble(itemInOrder.getPrice()) < 0) {
                    StorageGoods haveStorageGoods = getStorageGoodsById(itemInOrder, 0);
                    if (haveStorageGoods != null) {//合并负数量
//                        haveStorageGoods.setQuantity(haveStorageGoods.getQuantity() + Integer.parseInt(itemInOrder.getQuantity()));
                        haveStorageGoods.setReviewTotalAmt(DoubleUtils.sum(haveStorageGoods.getReviewTotalAmt(), DoubleUtils.sub(itemInOrder.getPrice(), itemInOrder.getTaxAmt())));//取负数
                        haveStorageGoods.setReviewReduceQuantity(haveStorageGoods.getReviewReduceQuantity() + Integer.parseInt(itemInOrder.getQuantity()));//取负数


                        StorageGoods positiveStorageGoods = getStorageGoodsById(itemInOrder, 1);
                        if (positiveStorageGoods != null) {
                            positiveStorageGoods.setReviewTotalAmt(DoubleUtils.sum(positiveStorageGoods.getReviewTotalAmt(), DoubleUtils.sub(itemInOrder.getTaxAmt(), itemInOrder.getPrice())));//取正数
                            positiveStorageGoods.setReviewReduceQuantity(positiveStorageGoods.getReviewReduceQuantity() + Math.abs(Integer.parseInt(itemInOrder.getQuantity())));//取正数
                        }

                    } else { //新增

                        StorageGoods storageGoods = new StorageGoods();
                        storageGoods.setId(Integer.parseInt(itemInOrder.getID()));

                        if (itemInOrder.getAttributeId() != null) {
                            storageGoods.setAttributeId(itemInOrder.getAttributeId());
                            storageGoods.setAtrributeidName(itemInOrder.getAttributeSku());
                        } else {
                            storageGoods.setAttributeId("");
                            storageGoods.setAtrributeidName("");
                        }

                        int tmpQuantity = Integer.parseInt(itemInOrder.getQuantity());
                        double tmpUnitPrice = DoubleUtils.div(DoubleUtils.sub(itemInOrder.getPrice(), itemInOrder.getTaxAmt()), tmpQuantity, 2);//取正数
                        storageGoods.setPrice(tmpUnitPrice);// 价格为单价,是整数
                        storageGoods.setQuantity(tmpQuantity);// 数量是负数
                        storageGoods.setName(itemInOrder.getName());

                        StorageGoods positiveStorageGoods = getStorageGoodsById(itemInOrder, 1);
                        if (positiveStorageGoods != null) {
                            positiveStorageGoods.setReviewTotalAmt(DoubleUtils.sum(positiveStorageGoods.getReviewTotalAmt(), DoubleUtils.sub(itemInOrder.getTaxAmt(), itemInOrder.getPrice())));//取正数
                            positiveStorageGoods.setReviewReduceQuantity(positiveStorageGoods.getReviewReduceQuantity() + Math.abs(Integer.parseInt(itemInOrder.getQuantity())));//取正数
                        }

                        mStorageGoodsList.add(storageGoods);
                    }
                }
            }
        }
    }

    private StorageGoods getStorageGoodsById(ItemInOrder itemInOrder, int flag) {
        int itemId = Integer.parseInt(itemInOrder.getID());
        String attributeId = itemInOrder.getAttributeId();
        if (attributeId == null) {
            attributeId = "";
        }

        for (StorageGoods storageGoods : mStorageGoodsList) {
            if (flag == 1 && storageGoods.getId() == itemId && storageGoods.getAttributeId().equals(attributeId)) {
                return storageGoods;
            } else if (flag == 0 && storageGoods.getId() == itemId && storageGoods.getAttributeId().equals(attributeId)
                    && storageGoods.getQuantity() < 0) {
                return storageGoods;
            } else if (flag == 2 && storageGoods.getId() == itemId && storageGoods.getAttributeId().equals(attributeId)
                    && storageGoods.getPrice() < 0) {
                return storageGoods;
            }
        }
        return null;
    }

    @Override
    public int toPaySplit() {

        boolean allowSplit = true;

        // 3人均分金额小于0.01
        if ((mCost / 3) < 0.01) {
            allowSplit = false;
        }

        mCheckMenuView.startSplitView(allowSplit);
        return 0;
    }
}
