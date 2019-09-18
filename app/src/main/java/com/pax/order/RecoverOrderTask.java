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
 * 2018/11/15 	        chenyr           	Create/Add/Modify/Delete
 * ============================================================================
 */

package com.pax.order;

import com.pax.order.entity.DownloadItemData;
import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsItem;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.entity.RspMsg;
import com.pax.order.entity.SelectGoods;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;
import com.pax.order.orderserver.entity.getorderdetail.SPOrderDetail;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.Tools;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.pax.order.enums.DiscountType.Amount;
import static com.pax.order.enums.DiscountType.Percentage;

/**
 * Created by chenyr on 2018/11/7.
 */

public class RecoverOrderTask implements Runnable {
    private static final String TAG = "RecoverOrderTask";
    private List<SPOrderDetail> mSPOrderDetailList = new ArrayList<>();

    public RecoverOrderTask(List<SPOrderDetail> spOrderDetailList) {
        mSPOrderDetailList.addAll(spOrderDetailList);
    }

    @Override
    public void run() {

//        List<OrderDetail> mOrderDetailList = new ArrayList<>();

        if (null != mSPOrderDetailList && !mSPOrderDetailList.isEmpty()) {
            for (SPOrderDetail spOrderDetail : mSPOrderDetailList) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setTableID(spOrderDetail.getTableID());
                orderDetail.setTransDate(spOrderDetail.getTransDate());
                orderDetail.setTransTime(spOrderDetail.getTransTime());
                orderDetail.setServer(spOrderDetail.getServer());
                orderDetail.setGuestCount(spOrderDetail.getGuestCount());
                orderDetail.setSeatNum(spOrderDetail.getSeatNum());
                orderDetail.setDueAmt(spOrderDetail.getDueAmt());
                orderDetail.setSubTotalAmt(spOrderDetail.getSubTotalAmt());
                orderDetail.setTaxAmt(spOrderDetail.getTaxAmt());
                orderDetail.setTipAmt(spOrderDetail.getTipAmt());
                orderDetail.setSurchargeAmt(spOrderDetail.getSurchargeAmt());
                orderDetail.setDiscountType(spOrderDetail.getDiscountType());
                orderDetail.setDiscountValue(spOrderDetail.getDiscountValue());
                orderDetail.setTotalAmt(spOrderDetail.getTotalAmt());
                orderDetail.setTraceNum(spOrderDetail.getTraceNum());
                orderDetail.setCustomerName(spOrderDetail.getCustomerName());
                orderDetail.setStatus(spOrderDetail.getStatus());
                orderDetail.setmEmployeeId(spOrderDetail.getServer());

                int orderGoodsSize = null == spOrderDetail.getItems() ? 0 : spOrderDetail.getItems().size();
                if (orderGoodsSize > 0) {
                    List<ItemInOrder> itemInOrders = new ArrayList<>();
                    for (ItemInOrder itemInOrder : spOrderDetail.getItems()) {
                        itemInOrders.add(itemInOrder);
                    }
                    orderDetail.setmItemInOrdersList(itemInOrders);
                }
//                mOrderDetailList.add(orderDetail);
                RecoverOrder(orderDetail);
            }
        }

//        for (OrderDetail orderDetail : mOrderDetailList) {
//            RecoverOrder(orderDetail);
//        }

        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setMessageCode(MessgeCode.ORDER_DETAIL_MSG);
        processMessage.setRspMsg(new RspMsg("0000", ""));
        EventBus.getDefault().post(processMessage);
    }

    private String getAtrributeidName(int selectGoodID, String atrributeID) {
        GoodsItem goodsItem = DownloadItemData.getInstance().findGoodsItemById(selectGoodID);
        if (null == goodsItem) {
            return "";
        }

        List<GoodsAttributes> goodsAttributesList = goodsItem.getGoodsAttributesList();
        if (null == goodsAttributesList || goodsAttributesList.isEmpty()) {
            return "";
        }

        for (GoodsAttributes goodsAttributes : goodsAttributesList) {
            if (goodsAttributes.getId()
                    .equals(atrributeID)) {
                return goodsAttributes.getName();
            }
        }

        return "";
    }

    private int RecoverOrder(OrderDetail orderDetail) {
        String quantityAdjust;
        double tmpUnitTax;
        double tmpUnitPrice;
        double totalTax;

        if (Double.parseDouble(orderDetail.getTotalAmt()) < 0)//过滤掉异常的openTicket
        {
            return -1;
        }

        OpenTicket openTicket = new OpenTicket();

        openTicket.setTableId(orderDetail.getTableID());
        if (null != orderDetail.getGuestCount() && !orderDetail.getGuestCount()
                .isEmpty()) {
            openTicket.setGuestCount(Integer.parseInt(orderDetail.getGuestCount()));
        } else {
            openTicket.setGuestCount(1);
        }


        openTicket.setOrederType("");
        openTicket.setName("");
        openTicket.setIfCustomerView(true);

        openTicket.setTraceNum(orderDetail.getTraceNum());
        if (null != orderDetail.getDiscountType()) {
            openTicket.setDiscountType(orderDetail.getDiscountType()
                    .equals("Amount") ? Amount : Percentage);
        }
        openTicket.setEmployeeId(orderDetail.getmEmployeeId());
        openTicket.setDiscountValue(orderDetail.getDiscountValue());
        openTicket.setDueAmt(orderDetail.getDueAmt());
        openTicket.setTotalAmt(orderDetail.getTotalAmt());
        openTicket.setTaxAmt(orderDetail.getTaxAmt());
        openTicket.setSubTotalAmt(orderDetail.getSubTotalAmt());

        String strDateTime = Tools.dateTimeFormat(orderDetail.getTransDate(), orderDetail.getTransTime());
        openTicket.setOrderTime(strDateTime);

        List<SelectGoods> selectList = new ArrayList<>();

        List<ItemInOrder> itemInOrdersList = orderDetail.getmItemInOrdersList();
        if (null == itemInOrdersList || itemInOrdersList.isEmpty()) {
            return -1;
        }

        List<ItemInOrder> itemInOrdersListBak = Tools.deepCopy(itemInOrdersList);

        while (true) {
            if (itemInOrdersListBak.isEmpty()) {
                break;
            }

            Iterator<ItemInOrder> iter_1 = itemInOrdersListBak.iterator();
            if (iter_1.hasNext()) {
                ItemInOrder itemInOrder_1 = iter_1.next();

                SelectGoods selectGood = new SelectGoods();
                selectGood.setId(Integer.parseInt(itemInOrder_1.getID()));
                selectGood.setName(itemInOrder_1.getName());

                int tmpQuantity = Integer.parseInt(itemInOrder_1.getQuantity());
                selectGood.setQuantity(tmpQuantity);

                if (null != itemInOrder_1.getAttributeTaxAdj()) {
                    selectGood.setAttributeTaxAmt(Double.parseDouble(itemInOrder_1.getAttributeTaxAdj()));
                } else {
                    selectGood.setAttributeTaxAmt(0);
                }


                GoodsItem goodsItem = DownloadItemData.getInstance()
                        .findGoodsItemById(selectGood.getId());
                if (null != goodsItem) {
                    selectGood.setCategoryId(goodsItem.getCategoryId());
                    selectGood.setCategoryName(goodsItem.getCategoryName());
                } else {
                    selectGood.setCategoryId(0);
                    selectGood.setCategoryName("");
                }

                if (null == itemInOrder_1.getAttributeId()) {
                    selectGood.setAttributeId("");
                    selectGood.setAtrributeidName("");
                } else {
                    selectGood.setAttributeId(itemInOrder_1.getAttributeId());
                    if (null == itemInOrder_1.getAttributeSku()) {
                        selectGood.setAtrributeidName(getAtrributeidName(selectGood.getId(),
                                itemInOrder_1.getAttributeId()));
                    } else {
                        selectGood.setAtrributeidName(itemInOrder_1.getAttributeSku());
                    }
                    selectGood.setAttributePrice(itemInOrder_1.getAttributePriceAdj());
                }

                if (tmpQuantity == 0) {
                    if (null != goodsItem) {
                        tmpUnitPrice = goodsItem.getPrice();
                        if (null != itemInOrder_1.getAttributePriceAdj())
                            tmpUnitPrice = DoubleUtils.sum(tmpUnitPrice, itemInOrder_1.getAttributePriceAdj());

                        tmpUnitTax = goodsItem.getTaxAmount();
                    } else {
                        tmpUnitPrice = Double.parseDouble(itemInOrder_1.getUnitPrice());
                        tmpUnitTax = 0;
                    }

                    totalTax = 0.00;
                } else {
                    totalTax = Double.parseDouble(itemInOrder_1.getTaxAmt());
                    tmpUnitTax = DoubleUtils.div(totalTax, tmpQuantity, 2);

                    // original tax and price
                    double originalTotalTax = DoubleUtils.sum(itemInOrder_1.getDiscountValue(), itemInOrder_1.getPrice());
                    double originalUnitTax = DoubleUtils.sum(itemInOrder_1.getDiscountTax(), itemInOrder_1.getTaxAmt());
                    tmpUnitPrice = DoubleUtils.div(DoubleUtils.sub(originalTotalTax, originalUnitTax), tmpQuantity, 2);
                }

                AppLog.e(TAG, Tools._FUNC_LINE_() + "tmpUnitPrice : " + tmpUnitPrice + ", tmpUnitTax : " + tmpUnitTax);
                selectGood.setTaxAmt(totalTax);
                selectGood.setUnitTaxAmt(tmpUnitTax);
                selectGood.setPrice(tmpUnitPrice);
                selectGood.setDiscountAmt(DoubleUtils.sub(itemInOrder_1.getDiscountValue(), itemInOrder_1.getDiscountTax()));
                selectGood.setmTotalAmt(DoubleUtils.sub(itemInOrder_1.getPrice(), itemInOrder_1.getTaxAmt()));

                Iterator<ItemInOrder> iter_2 = itemInOrdersListBak.iterator();
                while (iter_2.hasNext()) {
                    ItemInOrder itemInOrder_2 = iter_2.next();

                    if (Integer.parseInt(itemInOrder_2.getQuantity()) < 0 || Double.parseDouble(itemInOrder_2.getPrice()) < 0) { //过滤负数量的
                        iter_2.remove();
                        continue;
                    }

                    if (null == itemInOrder_1.getAttributeId()) {
                        itemInOrder_1.setAttributeId("");
                    }

                    if (null == itemInOrder_2.getAttributeId()) {
                        itemInOrder_2.setAttributeId("");
                    }

                    if (itemInOrder_1.getID()
                            .equals(itemInOrder_2.getID()) && itemInOrder_1.getAttributeId()
                            .equals(itemInOrder_2.getAttributeId())) {
                        if (itemInOrder_1.getTicketItemNum()
                                .equals(itemInOrder_2.getTicketItemNum())) {
                            iter_2.remove();
                        } else {
                            quantityAdjust = itemInOrder_2.getQuantity();
                            if (quantityAdjust.startsWith("-")) {
                                int tmpAdjustQ = Integer.parseInt(quantityAdjust.substring(1));
                                tmpQuantity += tmpAdjustQ;
                            } else {
                                int tmpAdjustQ = Integer.parseInt(quantityAdjust);
                                tmpQuantity += tmpAdjustQ;
                            }

                            if (!itemInOrder_1.getUnitPrice().equals(itemInOrder_2.getUnitPrice())) {
                                if (Integer.parseInt(itemInOrder_2.getQuantity()) != 0) {

                                    // original tax and price
                                    double originalTotalTax_2 = DoubleUtils.sum(itemInOrder_2.getDiscountValue(), itemInOrder_2.getPrice());
                                    double originalUnitTax_2 = DoubleUtils.sum(itemInOrder_2.getDiscountTax(), itemInOrder_2.getTaxAmt());
                                    double tmpUnitPrice_2 = DoubleUtils.div(DoubleUtils.sub(originalTotalTax_2, originalUnitTax_2), Integer.parseInt(itemInOrder_2.getQuantity()), 2);

                                    double totalPrice_1 = DoubleUtils.mul(tmpUnitPrice_2, itemInOrder_2.getQuantity());
                                    double totalPrice_2 = DoubleUtils.mul(selectGood.getPrice(), selectGood.getQuantity());
                                    tmpUnitPrice = DoubleUtils.div(DoubleUtils.sum(totalPrice_1, totalPrice_2), tmpQuantity, 2);
                                    AppLog.e(TAG, Tools._FUNC_LINE_() + "tmpUnitPrice : " + tmpUnitPrice);
                                    selectGood.setPrice(tmpUnitPrice);

                                }
                            }

                            Double tmpDiscountAmt = DoubleUtils.sub(itemInOrder_2.getDiscountValue(),
                                    itemInOrder_2.getDiscountTax());
                            selectGood.setDiscountAmt(DoubleUtils.sum(selectGood.getDiscountAmt(), tmpDiscountAmt));

                            Double tmpTotalAmt = DoubleUtils.sub(itemInOrder_2.getPrice(), itemInOrder_2.getTaxAmt());

                            selectGood.setmTotalAmt(DoubleUtils.sum(selectGood.getmTotalAmt(), tmpTotalAmt));
                            iter_2.remove();
                            selectGood.setQuantity(tmpQuantity);

                            // uptate tax
                            selectGood.setTaxAmt(DoubleUtils.sum(selectGood.getTaxAmt(), itemInOrder_2.getTaxAmt()));
                            if (selectGood.getQuantity() != 0)
                                selectGood.setUnitTaxAmt(DoubleUtils.div(selectGood.getTaxAmt(), selectGood.getQuantity(), 2));
                        }
                    }
                }//while (iter_2.hasNext())

                selectList.add(selectGood);
            } else {
                break;
            }
        } //while (true)

        openTicket.setSelectList(selectList);
        for (SelectGoods selectGoods : openTicket.getSelectList()) {
            selectGoods.setOpenTicket(openTicket);
        }

        FinancialApplication.getOpenTicketDbHelper()
                .insertTicketData(openTicket);
        FinancialApplication.getOrderDetailDb()
                .saveOrderDetailData(orderDetail);
        FinancialApplication.getController()
                .addOrderDetailList(orderDetail);

        return 0;
    }
}
