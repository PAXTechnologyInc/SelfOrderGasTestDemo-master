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

import android.support.annotation.NonNull;

import com.pax.order.FinancialApplication;
import com.pax.order.entity.StorageGoods;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class ItemSelectPresenter extends BasePresenter<IView> implements ItemSelectContract.Presenter {
    private static final String TAG = "ItemSelectPresenter";

    private final ItemSelectContract.View mItemSelectView;

    private List<StorageGoods> mAllList = new ArrayList<>();
    private List<StorageGoods> mSelectList = new ArrayList<>();
    private List<StorageGoods> mMyItemList = new ArrayList<>();
    private String mStrPerson;
    private double mTotal;

    public ItemSelectPresenter(@NonNull ItemSelectContract.View itemSelectView) {
        mItemSelectView = itemSelectView;
        mItemSelectView.setPresenter(this);
    }

    @Override
    public void start() {
        mAllList = FinancialApplication.getOpenTicketDbHelper().findAllStorageGoods();

        getSelectList();
        getMyItemList();
        mStrPerson = "Total ";
        mTotal = getMyItemTotal();
        System.out.println("ItemSelectPresenter start");
        for(int i = 0; i < mSelectList.size(); i++){
            System.out.println("Select Good: " + mSelectList.get(i).toString());
        }
        for(int i = 0; i < mMyItemList.size(); i++){
            System.out.println("Select Good: " + mMyItemList.get(i).toString());
        }
        //显示view页面
        mItemSelectView.initView(mSelectList, mMyItemList, mStrPerson, mTotal);
    }

    @Override
    public void addMyItem(int index) {

        StorageGoods storageGoods = mSelectList.get(index);
        for (StorageGoods goods : mAllList) {
            if ((goods.getId() == storageGoods.getId())
                    && (goods.getAttributeId().equals(storageGoods.getAttributeId()))) {
                //未支付--
                int unPaidQuantity = goods.getUnPaidQuantity();
                //预支付++
                int prePaidQuantity = goods.getPrePaidQuantity();

                // 均分金额<0.01不分单
                if ((goods.getmUnpaidlAmt() / goods.getUnPaidQuantity()) < 0.01) {
                    prePaidQuantity += unPaidQuantity;
                    unPaidQuantity = 0;

                    goods.setUnPaidQuantity(unPaidQuantity);
                    goods.setPrePaidQuantity(prePaidQuantity);
                    goods.setmPrePaidAmt(goods.getmUnpaidlAmt());
                    goods.setmUnpaidlAmt(0);
                    goods.setPrePaidTaxAmt(goods.getUnpaidlTaxAmt());
                    goods.setUnpaidlTaxAmt(0);
                } else {
                    if (unPaidQuantity > 0) {
                        unPaidQuantity--;
                    }
                    prePaidQuantity++;

                    goods.setUnPaidQuantity(unPaidQuantity);
                    goods.setPrePaidQuantity(prePaidQuantity);
                    if (0 == unPaidQuantity) {
                        goods.setmPrePaidAmt(DoubleUtils.round(goods.getmPrePaidAmt() + goods.getmUnpaidlAmt()));
                        goods.setPrePaidTaxAmt(DoubleUtils.round(goods.getPrePaidTaxAmt() + goods.getUnpaidlTaxAmt()));
                        goods.setmUnpaidlAmt(0.00);
                        goods.setUnpaidlTaxAmt(0.00);
                    } else {
                        goods.setmPrePaidAmt(DoubleUtils.round(goods.getPrePaidQuantity() * goods.getmMergePrice()));
                        goods.setPrePaidTaxAmt(DoubleUtils.round(goods.getPrePaidQuantity() * goods.getUnitTaxAmt()));
                        goods.setmUnpaidlAmt(DoubleUtils.round(goods.getmTotalAmt() - goods.getmPrePaidAmt()));
                        goods.setUnpaidlTaxAmt(DoubleUtils.round(goods.getTaxAmt() - goods.getPrePaidTaxAmt()));
                    }
                }

                break;
            }
        }

        getSelectList();
        getMyItemList();
        mTotal = getMyItemTotal();
        mItemSelectView.updateView(mSelectList, mMyItemList, mStrPerson, mTotal);
    }

    @Override
    public void minusMyItem(int index) {
        StorageGoods storageGoods = mMyItemList.get(index);
        for (StorageGoods goods : mAllList) {
            if ((goods.getId() == storageGoods.getId())
                    && (goods.getAttributeId().equals(storageGoods.getAttributeId()))) {
                //未支付++
                int unPaidQuantity = goods.getUnPaidQuantity();
                //预支付--
                int prePaidQuantity = goods.getPrePaidQuantity();

                // 均分金额<0.01不分单
                if ((goods.getmPrePaidAmt() / goods.getPrePaidQuantity()) < 0.01) {
                    unPaidQuantity += prePaidQuantity;
                    prePaidQuantity = 0;

                    goods.setUnPaidQuantity(unPaidQuantity);
                    goods.setPrePaidQuantity(prePaidQuantity);
                    goods.setmUnpaidlAmt(goods.getmPrePaidAmt());
                    goods.setmPrePaidAmt(0);
                    goods.setUnpaidlTaxAmt(goods.getPrePaidTaxAmt());
                    goods.setPrePaidTaxAmt(0);
                } else {
                    if (prePaidQuantity > 0) {
                        prePaidQuantity--;
                    }
                    unPaidQuantity++;

                    goods.setUnPaidQuantity(unPaidQuantity);
                    goods.setPrePaidQuantity(prePaidQuantity);
                    if (0 == prePaidQuantity) {
                        goods.setmUnpaidlAmt(DoubleUtils.round(goods.getmPrePaidAmt() + goods.getmUnpaidlAmt()));
                        goods.setUnpaidlTaxAmt(DoubleUtils.round(goods.getPrePaidTaxAmt() + goods.getUnpaidlTaxAmt()));
                        goods.setmPrePaidAmt(0.00);
                        goods.setPrePaidTaxAmt(0.00);
                    } else {
                        goods.setmUnpaidlAmt(DoubleUtils.round(goods.getUnPaidQuantity() * goods.getmMergePrice()));
                        goods.setUnpaidlTaxAmt(DoubleUtils.round(goods.getUnPaidQuantity() * goods.getUnitTaxAmt()));
                        goods.setmPrePaidAmt(DoubleUtils.round(goods.getmTotalAmt() - goods.getmUnpaidlAmt()));
                        goods.setPrePaidTaxAmt(DoubleUtils.round(goods.getTaxAmt() - goods.getUnpaidlTaxAmt()));
                    }
                }

                break;
            }
        }

        getSelectList();
        getMyItemList();
        mTotal = getMyItemTotal();
        mItemSelectView.updateView(mSelectList, mMyItemList, mStrPerson, mTotal);
    }

    @Override
    public void confirmItem() {
        FinancialApplication.getOpenTicketDbHelper().updateSelectGoods(mAllList);
    }

    private double getMyItemTotal() {
        double itemTotal = 0;
        for (int i = 0; i < mMyItemList.size(); i++) {
            //            itemTotal += (mMyItemList.get(i).getPrePaidQuantity() * mMyItemList.get(i).getPrice());
            itemTotal += mMyItemList.get(i).getmPrePaidAmt();
        }
        return itemTotal;
    }

    private void getSelectList() {
        //未支付
        //        mSelectList.clear();
        for (StorageGoods storageGoods : mAllList) {
            if (storageGoods.getUnPaidQuantity() > 0) {
                int index = mSelectList.indexOf(storageGoods);
                if (index < 0) {
                    mSelectList.add(storageGoods);
                } else {
                    mSelectList.get(index).setUnPaidQuantity(storageGoods.getUnPaidQuantity());
                    mSelectList.get(index).setPrePaidQuantity(storageGoods.getPrePaidQuantity());
                }
            }
        }

        for (StorageGoods storageGoods : mSelectList) {
            if (storageGoods.getUnPaidQuantity() <= 0) {
                mSelectList.remove(storageGoods);
                break;
            }
        }
    }

    private void getMyItemList() {
        //预支付
        //        mMyItemList.clear();
        for (StorageGoods storageGoods : mAllList) {
            if (storageGoods.getPrePaidQuantity() > 0) {
                int index = mMyItemList.indexOf(storageGoods);
                if (index < 0) {
                    mMyItemList.add(storageGoods);
                } else {
                    mMyItemList.get(index).setUnPaidQuantity(storageGoods.getUnPaidQuantity());
                    mMyItemList.get(index).setPrePaidQuantity(storageGoods.getPrePaidQuantity());
                }
            }
        }

        for (StorageGoods storageGoods : mMyItemList) {
            if (storageGoods.getPrePaidQuantity() <= 0) {
                mMyItemList.remove(storageGoods);
                break;
            }
        }
    }

}
