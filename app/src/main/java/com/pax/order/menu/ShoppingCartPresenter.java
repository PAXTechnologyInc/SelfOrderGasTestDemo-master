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
 * 2018/8/7 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.menu;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.entity.CartData;
import com.pax.order.entity.CartData.EOrderStatus;
import com.pax.order.entity.ErrDataInOpenTicket;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.PayData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.Transaction;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.orderserver.entity.modifyTicket.OpenItemReq;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class ShoppingCartPresenter extends BasePresenter<IView> implements ShoppingCartContract.Presenter {
    private static String TAG = "ShoppingCartPresenter";

    private final ShoppingCartContract.View mShoppingCartView;
    private SparseIntArray mGroupSelect;
    private List<SelectGoods> mGoodsList = new ArrayList<>(); //购物车商品列表

    public ShoppingCartPresenter(@NonNull ShoppingCartContract.View shoppingCartView) {
        mShoppingCartView = shoppingCartView;
        mShoppingCartView.setPresenter(this);
    }

    @Override
    public void start() {

        mGroupSelect = CartData.getInstance().getGroupSelect();
        mGoodsList = CartData.getInstance().getSelectedList();

        mShoppingCartView.initView(mGoodsList);
    }

    @Override
    public int add(SelectGoods goodsItem) {

        int groupCount = mGroupSelect.get(goodsItem.getCategoryId());
        mGroupSelect.append(goodsItem.getCategoryId(), ++groupCount);

        for (SelectGoods selectGoods : mGoodsList) {
            if (selectGoods.getId() == goodsItem.getId() && selectGoods.getAttributeId().equals(goodsItem.getAttributeId())) {
                selectGoods.setQuantity(selectGoods.getQuantity() + 1);
            }
        }

        mShoppingCartView.updateView(mGoodsList);
        return 0;
    }

    @Override
    public int remove(SelectGoods goodsItem) {

        int groupCount = mGroupSelect.get(goodsItem.getCategoryId());
        if (groupCount == 1) {
            mGroupSelect.delete(goodsItem.getCategoryId());
        } else if (groupCount > 1) {
            mGroupSelect.append(goodsItem.getCategoryId(), --groupCount);
        }

        for (SelectGoods selectGoods : mGoodsList) {
            if (selectGoods.getId() == goodsItem.getId() && selectGoods.getAttributeId().equals(goodsItem.getAttributeId())) {
                if (selectGoods.getQuantity() < 2) {
                    mGoodsList.remove(selectGoods);
                    break;
                } else {
                    selectGoods.setQuantity(selectGoods.getQuantity() - 1);
                    if (selectGoods.getQuantity() > selectGoods.getStockNum()) {
                        selectGoods.setUnderStock(true);
                    } else {
                        selectGoods.setUnderStock(false);
                    }

                }
            }
        }

        mShoppingCartView.updateView(mGoodsList);
        return 0;
    }

    @Override
    public int deleteItem(SelectGoods goodsItem) {
        int groupCount = mGroupSelect.get(goodsItem.getCategoryId());

        for (SelectGoods selectGoods : mGoodsList) {
            if (selectGoods.getId() == goodsItem.getId() && selectGoods.getAttributeId().equals(goodsItem.getAttributeId())) {
                if (groupCount == 1) {
                    mGroupSelect.delete(goodsItem.getCategoryId());
                } else if (groupCount > 1) {
                    mGroupSelect.append(goodsItem.getCategoryId(), groupCount - selectGoods.getQuantity());
                }
                mGoodsList.remove(selectGoods);
                break;
            }
        }
        mShoppingCartView.updateView(mGoodsList);
        return 0;
    }

    @Override
    public int back() {
        mShoppingCartView.finshView(EOrderStatus.BACK);
        return 0;
    }

    @Override
    public int clear() {
        CartData.getInstance().getGroupSelect().clear();
        CartData.getInstance().getSelectedList().clear();
        return 0;
    }

    public int clearNoGasItem(){

        return 0;
    }

    @Override
    public int addOrder() {

        //桌子存在未支付订单,使用asyncModifyTicket加菜
        List<OpenItemReq> list = new ArrayList<>();

        for (SelectGoods selectGoods : CartData.getInstance()
                .getSelectedList()) {
            OpenItemReq item = new OpenItemReq();
            item.setId(Integer.toString(selectGoods.getId()));
            item.setAttributeId(selectGoods.getAttributeId());
            item.setQuantity(Integer.toString(selectGoods.getQuantity()));
            item.setDiscountAmt("");
            list.add(item);
        }
//        OrderInstance.getInstance().asyncModifyTicket(FinancialApplication.getController().getTraceNum(),
//                "Modify", "",
//                "", null, list);
        System.out.println("Making Call to async modify ticket");
        return 0;
    }


    @Override
    public OpenTicket order() {
//        OrderInstance orderInterface = OrderInstance.getInstance();
        System.out.println("PayData: " + PayData.getInstance().ifNoTransactionData());
        if (PayData.getInstance().ifNoTransactionData()) {
            List<Transaction> list = FinancialApplication.getTransactionDb().readTransactionList();
//            orderInterface.asyncUploadMultiTrans(list, null);
            System.out.println("Making call to async Upload MultiTrans");
        }

        //判断桌子是否存在订单

        if (FinancialApplication.getController().getIsOpen()) {
            //桌子存在未支付订单,使用asyncModifyTicket加菜
            System.out.println("Adding to OpenTicket");
            addOrder();
            return new OpenTicket();

        } else {
            //桌子不存在未支付订单,使用OpenTicket下单
            System.out.println("Creating OpenTicket");
            OpenTicket ticket = new OpenTicket();

            String tableId = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                    .getString(ParamConstants.TABLE_ID, null);
            System.out.println("Order TableID: " + tableId);
            ticket.setTableId(tableId);
            ticket.setOrederType("DineIn");
            ticket.setName("");
            ticket.setIfCustomerView(true);
            ticket.setDiscountAmt("");
            ticket.setAllowMultiOrders(false);

            List<SelectGoods> selectGoodsList = new ArrayList<>();
            selectGoodsList.addAll(CartData.getInstance().getSelectedList());
            ticket.setSelectList(selectGoodsList);
            for(int i = 0; i < selectGoodsList.size(); i++){
                System.out.println("Select Good: " + selectGoodsList.get(i).getId() + " name: " + selectGoodsList.get(i).getName());
            }
            System.out.println("Selected goods: " + selectGoodsList);
//            orderInterface.asyncOpenTicket(ticket);
//            FinancialApplication.getOpenTicketDbHelper().insertTicketData(ticket);
            System.out.println("Making Call to async open ticket");
            return ticket;
        }
    }

    public void updateUnderStockItem(List<ErrDataInOpenTicket> errDataInOpenTickets) {
        final String notFound = "NotFound";
        final String underStock = "Understocked";

        if ((errDataInOpenTickets == null) || (mGoodsList == null)) {
            return;
        }
        for (ErrDataInOpenTicket errDataInOpenTicket : errDataInOpenTickets) {
            for (SelectGoods selectGoods : mGoodsList) {
                if (selectGoods.getId() == Integer.parseInt(errDataInOpenTicket.getItemID())) {
                    selectGoods.setUnderStock(true);
                    if (errDataInOpenTicket.getType().equals(notFound)) {
                        selectGoods.setStockNum(0);
                    } else {
                        if (null != errDataInOpenTicket.getStock() && (!errDataInOpenTicket.getStock().equals(""))) {
                            selectGoods.setStockNum(Integer.parseInt(errDataInOpenTicket.getStock()));
                        } else {
                            selectGoods.setStockNum(0);
                        }
                    }
                }
            }
        }

        mShoppingCartView.updateView(mGoodsList);
    }
}
