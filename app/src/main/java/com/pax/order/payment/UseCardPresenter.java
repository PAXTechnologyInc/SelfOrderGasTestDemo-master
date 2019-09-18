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
 * 2018/12/29 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.payment;

import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.db.PrintDataDb;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.Order;
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.PayData;
import com.pax.order.entity.PrintData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.entity.Transaction;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.orderserver.entity.getorderdetail.ItemInOrder;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.order.print.PrintBitmapFactory;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.IView;
import com.pax.order.util.Tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class UseCardPresenter extends BasePresenter<IView> implements UseCardContract.Presenter {
    private static final String TAG = "UseCardPresenter";
    private final UseCardContract.View mUseCardView;

    private List<String> mPrnInfoList = new ArrayList<>();
    private String mCardNo;
    private String mCardOrg;
    private String mCardHolderName;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");
    private int mPayCounter = 0;
    private String mCardBin;
    private String mEntryMode;
    private String mTendType;
    private String mRefNum;
    private String mBatchNum;
    private String mAuthCode;




    private static final String TOTALPAY = "TOTALPAY";
    private static final String BYITEM = "BYITEM";
    private static final String BYEVENLY = "BYEVENLY";

    public UseCardPresenter(@NonNull UseCardContract.View useCardView) {
        mUseCardView = useCardView;
        mUseCardView.setPresenter(this);
    }

    @Override
    public void start() {

        if (PayData.getInstance().isIfSplit() && (PayData.getInstance().getSplitType() != SplitType.BYITEM)) {
            mUseCardView.displayPayStatus(PayData.getInstance().getSplitType(), PayData.getInstance().getSplitStep());
        }
        mUseCardView.startPay();
    }

    @Override
    public void dealPayFlow(IPayResponse payResponse) {
        final String ERROR_AMOUNT = "3";
        boolean ifSplit = PayData.getInstance().isIfSplit();
        SplitStep splitStep = PayData.getInstance().getSplitStep();

        //if (payResponse.getResult().equals(IBaseResponse.TRANS_SUCESS)) {
        if(true){
            savePayStep();
            savePayTip();
            mCardNo = payResponse.getCardNo();
            mCardOrg = payResponse.getCardOrg();
            mCardHolderName = payResponse.getCardHolderName();
            mEntryMode = payResponse.getEntryMode();
            mPayCounter = 0;
            if(payResponse instanceof PayResponseVar){
                PayResponseVar payRsp = (PayResponseVar) payResponse;
                mCardBin = payRsp.getCardBin();
                mRefNum = payRsp.getRefNum();
                mBatchNum = payRsp.getBatchNum();
                mAuthCode = payRsp.getAuthCode();

                String transType = payRsp.getTransType(); // transType = TendType+空格+Sale
                if(transType != null){
                    String[] s = transType.split("\\s+");
                    mTendType = s[0];
                }

            }
            //0金额无需用卡，故不用生成支付凭条
//            if (Double.parseDouble(mUseCardView.getTotalAmt()) != 0) {
//                generPayPrintData((IBaseResponse) payResponse);
//            }
//            generPrintData();
            //两张凭条合并为一张
            generOrderAndPayPrintData(payResponse);
            dealPayResult();

            mUseCardView.startPrint(mPrnInfoList);

        } else {
            //从支付流程中退出，未发生支付的情况下，需要允许改价。如在支付过程中，例如分单中，则不允许改价
            if ((!ifSplit) || ((ifSplit) && (splitStep == SplitStep.ZERO))) {
                PayData.getInstance().setIfPaymentProcess(false);
            }
            if (payResponse.getResult().equals(IBaseResponse.TRANS_CANCEL)) {
                AppLog.d(TAG, "onResult: " + payResponse.getResult());
                mUseCardView.dealPayFailResult();

            } else if (payResponse.getResult().equals(IBaseResponse.TRANS_ERROR_AMOUNT)) {
                FinancialApplication.getOpenTicketDbHelper().deleteAllOpenTicket();
                FinancialApplication.getOpenTicketDbHelper().deleteAllStorageGoods();
                FinancialApplication.getPayDataDb().clearPayData();
                FinancialApplication.getTransactionDb().clearTransaction();

                mUseCardView.finishView();
            } else {
                AppLog.d(TAG, "onResult: " + payResponse.getResult());
                mUseCardView.retryPay(mPayCounter, payResponse.getMessageText());
            }
        }

    }

    @Override
    public void setPayCounter(int payCounter) {
        mPayCounter = payCounter;
    }

    private void savePayStep() {
        SplitType splitType = PayData.getInstance().getSplitType();
        if ((splitType == SplitType.TWO) || (splitType == SplitType.THREE)) {
            switch (PayData.getInstance().getSplitStep()) {
                case ZERO:
                    PayData.getInstance().setSplitStep(SplitStep.ONE);
                    break;
                case ONE:
                    PayData.getInstance().setSplitStep(SplitStep.TWO);
                    break;
                case TWO:
                    PayData.getInstance().setSplitStep(SplitStep.THREE);
                    break;
                default:
                    break;
            }
        } else if (SplitType.BYITEM == splitType) {
            if (PayData.getInstance().getSplitStep() == SplitStep.ZERO) {
                PayData.getInstance().setSplitStep(SplitStep.ONE);
            }
        }
        FinancialApplication.getPayDataDb().savePayData(PayData.getInstance());
    }


    private void savePayTip() {
        String[] tipAmtBuf = PayData.getInstance().getTipAmt();
        if (null == tipAmtBuf) {
            tipAmtBuf = new String[1];
            tipAmtBuf[0] = mUseCardView.getTip();
            PayData.getInstance().setTipAmt(tipAmtBuf);
            FinancialApplication.getPayDataDb().savePayData(PayData.getInstance());
        } else {
            int num = tipAmtBuf.length;
            String[] newTipAmtBuf = new String[num + 1];
            for (int i = 0; i < num; i++) {
                newTipAmtBuf[i] = tipAmtBuf[i];
            }
            newTipAmtBuf[num] = mUseCardView.getTip();
            PayData.getInstance().setTipAmt(newTipAmtBuf);
            FinancialApplication.getPayDataDb().savePayData(PayData.getInstance());
        }
    }

    private void generPayPrintData(IBaseResponse baseResponse) {
        if (PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp()).getBoolean(ParamConstants.PRT_SWITCH, true)) {
            Bitmap prnBitmap = PrintBitmapFactory.genPrnBitmap(baseResponse);
            PrintBitmapFactory.savePrnBitmap(PrintBitmapFactory.PAYPRN_NAME, prnBitmap);
            mPrnInfoList.add(PrintBitmapFactory.PAYPRN_NAME);
        }
    }

    private void generOrderAndPayPrintData(IPayResponse payResponse) {
        PrintData printData = null;
        if (PayData.getInstance().isIfSplit()) {
            if (PayData.getInstance().getSplitType() == SplitType.BYITEM) {
                printData = getByItemPrintOrder();
            } else {
                if (PayData.getInstance().getSplitType() == SplitType.TWO) {
                    printData = getByEvenlyOrTotalPrintOrder(SplitType.TWO);
                } else if (PayData.getInstance().getSplitType() == SplitType.THREE) {
                    printData = getByEvenlyOrTotalPrintOrder(SplitType.THREE);
                }
            }
        } else {
            printData = getByEvenlyOrTotalPrintOrder(SplitType.NULL);
        }
        PrintDataDb.getInstance().savePrintData(printData);
        Bitmap prnBitmap = PrintBitmapFactory.genPrnBitmap(printData, payResponse);
        PrintBitmapFactory.savePrnBitmap(PrintBitmapFactory.ORDERPAYPRN_NAME, prnBitmap);
        mPrnInfoList.add(PrintBitmapFactory.ORDERPAYPRN_NAME);
    }

    private void generPrintData() {
        PrintData printData = null;
        if (PayData.getInstance().isIfSplit()) {
            if (PayData.getInstance().getSplitType() == SplitType.BYITEM) {
                printData = getByItemPrintOrder();
            } else {
                if (PayData.getInstance().getSplitType() == SplitType.TWO) {
                    printData = getByEvenlyOrTotalPrintOrder(SplitType.TWO);
                } else if (PayData.getInstance().getSplitType() == SplitType.THREE) {
                    printData = getByEvenlyOrTotalPrintOrder(SplitType.THREE);
                }
            }
        } else {
            printData = getByEvenlyOrTotalPrintOrder(SplitType.NULL);
        }
        Bitmap prnBitmap = PrintBitmapFactory.genPrnBitmap(printData);
        PrintBitmapFactory.savePrnBitmap(PrintBitmapFactory.ORDERPRN_NAME, prnBitmap);
        PrintDataDb.getInstance().savePrintData(printData);
        mPrnInfoList.add(PrintBitmapFactory.ORDERPRN_NAME);
    }

    private PrintData getByItemPrintOrder() {
        PrintData mPrintData = new PrintData();
        List<Order> orders = new ArrayList<>();
        List<StorageGoods> storageGoods = FinancialApplication.getOpenTicketDbHelper().findAllPrePaidGoods();
        Order order = new Order();
        int num = storageGoods.size();
        String[] dishName = new String[num];
        String[] dishPrice = new String[num];
        String[] dishNum = new String[num];
        String[] dishId = new String[num];
        String[] attrName = new String[num];
        String[] attrpriceAjust = new String[num];
        String[] attrId = new String[num];
        String[] dishUnitPrice = new String[num];
        double subtotalPrice = 0.0;

        for (int i = 0; i < num; i++) {
            dishId[i] = String.valueOf(storageGoods.get(i).getId());
            dishName[i] = storageGoods.get(i).getName();
            if (storageGoods.get(i).getAttributeId() != null) {
                attrId[i] = storageGoods.get(i).getAttributeId();
            } else {
                attrId[i] = "";
            }
            if ((storageGoods.get(i).getAtrributeidName() != null) && (storageGoods.get(i).getAtrributeidName().length() != 0)) {
                //dishName[i] += "(" + storageGoods.get(i).getAtrributeidName() + ")";
                attrName[i] = storageGoods.get(i).getAtrributeidName();
            } else {
                attrName[i] = "";
            }

            dishPrice[i] = mDecimalFormat.format(storageGoods.get(i).getmMergePrice()
                    * storageGoods.get(i).getPrePaidQuantity());
            if ((storageGoods.get(i).getAttributePrice() != null)
                    && (storageGoods.get(i).getAttributePrice().length() > 0)) {
                attrpriceAjust[i] = String.valueOf(storageGoods.get(i).getAttributePrice());
            }
            dishUnitPrice[i] = mDecimalFormat.format(storageGoods.get(i).getPrice());
            subtotalPrice += storageGoods.get(i).getmMergePrice() * storageGoods.get(i).getPrePaidQuantity();
            dishNum[i] = String.valueOf(storageGoods.get(i).getPrePaidQuantity());
        }
        order.setTermId(dishId);
        order.setAttributeId(attrId);
        order.setAtrributeidName(attrName);
        order.setAtrributeidPrice(attrpriceAjust);
        order.setDishName(dishName);
        order.setSingleDishTotalPrice(dishPrice);
        order.setSingleDishTotal(dishNum);
        order.setDishSortTotal(String.valueOf(num));
        order.setSingleDishPrice(dishUnitPrice);
        //        Date date = new Date();
        //        SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd HH:mm:ss");
        //        order.setOrderTime(formatDate.format(date));
        List<OpenTicket> openTickets = FinancialApplication.getOpenTicketDbHelper().findAllOpenTicketData();
        order.setOrderTime(openTickets.get(0).getOrderTime());

        order.setTotalPrice(mDecimalFormat.format(subtotalPrice));
        order.setDishTotal(String.valueOf(dishName.length));
        orders.add(order);
        mPrintData.setTableId(PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null));
        mPrintData.setTotalGuest("1");
        mPrintData.setCurrentPayer("1");
        mPrintData.setCardOrg(mCardOrg);
        mPrintData.setCardNo(mCardNo);
        mPrintData.setTotalOrderTipAmount(mUseCardView.getTip());
        mPrintData.setTotalOrderTaxAmount(mUseCardView.getTax());
        mPrintData.setTotalOrderPrice(mUseCardView.getTotalAmt());
        mPrintData.setPrintMode(BYITEM);
        mPrintData.setOrders(orders);
        return mPrintData;
    }

    private PrintData getByEvenlyOrTotalPrintOrder(SplitType splitType) {
        PrintData printData = new PrintData();
        List<Order> orders = new ArrayList<>();
        double subTotal = 0.0;

        List<OpenTicket> openTickets = FinancialApplication.getOpenTicketDbHelper().findAllOpenTicketData();
        if ((openTickets != null) && (openTickets.size() != 0)) {
            for (OpenTicket openTicket : openTickets) {
                Order order = new Order();
                int i = 0;
                int num = 0;
                openTicket.setSelectList(openTicket.getSelectListformDb());
                num = openTicket.getGoodsKind();
                int totalQuantity = 0;
                String[] dishName = new String[num];
                String[] dishUnitPrice = new String[num];
                String[] dishPrice = new String[num];
                String[] dishNum = new String[num];
                String[] dishId = new String[num];
                String[] attrName = new String[num];
                String[] attrpriceAjust = new String[num];
                String[] attrId = new String[num];
                String[] priceAjust = new String[num];
                String[] quantityAdjust = new String[num];

                OrderDetail orderDetail = FinancialApplication.getOrderDetailDb().readOrderDetailByTraceNum(openTicket.getTraceNum());
                if (orderDetail == null) {
                    return null;
                }

                for (SelectGoods selectGoods : openTicket.getSelectList()) {
                    dishId[i] = String.valueOf(selectGoods.getId());
                    dishName[i] = selectGoods.getName();
                    if (selectGoods.getAttributeId() != null) {
                        attrId[i] = selectGoods.getAttributeId();
                    }
                    if ((selectGoods.getAtrributeidName() != null) && (selectGoods.getAtrributeidName().length() != 0)) {
                        //dishName[i] += "(" + selectGoods.getAtrributeidName() + ")";
                        attrName[i] = selectGoods.getAtrributeidName();
                    } else {
                        attrName[i] = "";
                    }
                    dishNum[i] = String.valueOf(selectGoods.getQuantity());

                    // 统计修改记录
                    for (ItemInOrder itemInOrder : orderDetail.getmItemInOrdersList()) {
                        if (itemInOrder.getAttributeId() == null) {
                            itemInOrder.setAttributeId("");
                        }

                        if (itemInOrder.getID().equals(dishId[i]) && itemInOrder.getAttributeId().equals(attrId[i])) {

                            if ("Amount".equals(itemInOrder.getDiscountType()) && Double.parseDouble(itemInOrder.getDiscountValue()) > 0) {
                                String discountAmt = String.valueOf(DoubleUtils.sub(itemInOrder.getDiscountTax(), itemInOrder.getDiscountValue()));
                                if (priceAjust[i] == null) {
                                    priceAjust[i] = discountAmt;
                                } else {
                                    priceAjust[i] = String.valueOf(DoubleUtils.sum(priceAjust[i], discountAmt));
                                }
                            } else if (Integer.parseInt(itemInOrder.getQuantity()) < 0 && Double.parseDouble(itemInOrder.getPrice()) < 0) {
                                if (quantityAdjust[i] == null) {
                                    quantityAdjust[i] = itemInOrder.getQuantity();
                                } else {
                                    quantityAdjust[i] = String.valueOf(Integer.parseInt(quantityAdjust[i]) + Integer.parseInt
                                            (itemInOrder.getQuantity()));
                                }
                                dishNum[i] = String.valueOf(selectGoods.getQuantity() + Math.abs(Integer.parseInt(quantityAdjust[i])));
                            }
                        }
                    }

                    dishPrice[i] = String.valueOf(DoubleUtils.mul(Double.parseDouble(dishNum[i]), selectGoods.getPrice()));
                    dishUnitPrice[i] = String.valueOf(selectGoods.getPrice());
                    if ((selectGoods.getAttributePrice() != null)
                            && (!selectGoods.getAttributePrice().isEmpty())) {
                        attrpriceAjust[i] = String.valueOf(selectGoods.getAttributePrice());
                    }
                    totalQuantity += selectGoods.getQuantity();
                    i++;
                }
                order.setTermId(dishId);
                order.setAttributeId(attrId);
                order.setDishSortTotal(String.valueOf(num));
                order.setDishName(dishName);
                order.setAtrributeidName(attrName);
                order.setAtrributeidPrice(attrpriceAjust);
                order.setSingleDishPrice(dishUnitPrice);
                order.setSingleDishTotalPrice(dishPrice);
                order.setSingleDishTotal(dishNum);
                order.setOrderTime(openTicket.getOrderTime());
                subTotal = Double.valueOf(openTicket.getTotalAmt()) - Double.valueOf(openTicket.getTaxAmt());
                order.setTotalPrice(mDecimalFormat.format(subTotal));
                order.setTraceNo(openTicket.getTraceNum());
                order.setDishTotal(String.valueOf(totalQuantity));
                order.setPriceAdjustment(priceAjust);
                order.setQuantityAdjustment(quantityAdjust);
                orders.add(order);
                printData.setTableId(PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                        .getString(ParamConstants.TABLE_NUM, null));
            }

            //            checkGoodsIfModify(orders);
            if (splitType == SplitType.TWO) {
                printData.setTotalGuest("2");
                if (PayData.getInstance().getSplitStep() == SplitStep.ONE) {
                    printData.setCurrentPayer("1");
                } else if (PayData.getInstance().getSplitStep() == SplitStep.TWO) {
                    printData.setCurrentPayer("2");
                }
                printData.setTotalOrderPrice(mUseCardView.getTotalAmt());
                printData.setPrintMode(BYEVENLY);
            } else if (splitType == SplitType.THREE) {
                printData.setTotalGuest("3");
                if (PayData.getInstance().getSplitStep() == SplitStep.ONE) {
                    printData.setCurrentPayer("1");
                } else if (PayData.getInstance().getSplitStep() == SplitStep.TWO) {
                    printData.setCurrentPayer("2");
                } else if (PayData.getInstance().getSplitStep() == SplitStep.THREE) {
                    printData.setCurrentPayer("3");
                }
                printData.setTotalOrderPrice(mUseCardView.getTotalAmt());
                printData.setPrintMode(BYEVENLY);
            } else {
                printData.setTotalGuest("1");
                printData.setCurrentPayer("1");
                printData.setTotalOrderPrice(mUseCardView.getTotalAmt());
                printData.setPrintMode(TOTALPAY);
            }
            printData.setCardOrg(mCardOrg);
            printData.setCardNo(mCardNo);
            printData.setTotalOrderTipAmount(mUseCardView.getTip());
            printData.setTotalOrderTaxAmount(mUseCardView.getTax());

            printData.setOrders(orders);
            return printData;
        } else {
            return null;
        }
    }

    private void dealPayResult() {
        if (PayData.getInstance().isIfSplit()) {
            splitUploadTrans();
            if (PayData.getInstance().getSplitType() == SplitType.BYITEM) {
                FinancialApplication.getOpenTicketDbHelper().clearPrePaidGoods();
                if ((null == FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods())
                        || (FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods().size() == 0)) {
                    //By item分单交易完成，可以上送交易结果到后台
                    clearAllOrder(false);
                    FinancialApplication.getOrderDetailDb().deleteOrderDetail();
                    PayData.getInstance().init(); //交易完成清除过程数据
                }
            } else {
                if (PayData.getInstance().getSplitType() == SplitType.TWO) {
                    if (PayData.getInstance().getSplitStep() == SplitStep.TWO) {
                        //2人分单交易完成，上送交易结果到后台
                        clearAllOrder(false);
                        FinancialApplication.getOrderDetailDb().deleteOrderDetail();
                        PayData.getInstance().init(); //交易完成清除过程数据
                    }
                } else if (PayData.getInstance().getSplitType() == SplitType.THREE) {
                    if (PayData.getInstance().getSplitStep() == SplitStep.THREE) {
                        //3人分单交易完成，可以上送交易结果到后台
                        clearAllOrder(false);
                        FinancialApplication.getOrderDetailDb().deleteOrderDetail();
                        PayData.getInstance().init(); //交易完成清除过程数据
                    }
                }
            }
        } else {
            clearAllOrder(true);
            FinancialApplication.getOrderDetailDb().deleteOrderDetail();
            PayData.getInstance().init();//交易完成清除过程数据
        }

    }

    private void splitUploadTrans(){
        PayData.getInstance().setIfPaymentProcess(false);
        List<OpenTicket> openTicketList = FinancialApplication.getOpenTicketDbHelper().findAllOpenTicketData();
        Transaction trans = new Transaction();

        trans.setTableID(openTicketList.get(0).getTableId());
        trans.setTraceNum(openTicketList.get(0).getTraceNum());
        trans.setmEmployeeId(openTicketList.get(0).getEmployeeId());
        trans.setTotalAmt(openTicketList.get(0).getTotalAmt());
        trans.setApprovedAmt(mUseCardView.getUpLoadAmt());
        trans.setBaseAmt(mUseCardView.getBaseAmt());
        trans.setTipAmt(mUseCardView.getTip());
        trans.setTaxAmt(mUseCardView.getTax());

        trans.setmCardBin(mCardBin);
        trans.setmCardType(mCardOrg);
        trans.setmCardHolderName(mCardHolderName);
        trans.setTransType(RequestVar.TRANS_SALE);


        if(mEntryMode != null && !mEntryMode.isEmpty()) {
            trans.setEntryMode(mEntryMode.toUpperCase());
        }
        trans.setmTipType("CARD");
        trans.setmTenderType(mTendType);
        trans.setmTerminalRefNum(mRefNum);
        trans.setmBatchNum(mBatchNum);
        trans.setAuthCode(mAuthCode);
        if(mCardNo.length() >= 5) {
            trans.setMaskedPan(mCardNo.substring(mCardNo.length() - 5, mCardNo.length()));
        }

        Log.i(TAG,"ASYNC UPLOAD TRANS.............");
        System.out.println("OrderInterface Upload Trans");
        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.asyncUploadTrans(trans,"");

    }


    private void clearAllOrder(Boolean ifUpload) {
        PayData.getInstance().setIfPaymentProcess(false);
        String totalAmt = null;
        List<Transaction> transactionList = new ArrayList<>();
        List<OpenTicket> openTicketList = FinancialApplication.getOpenTicketDbHelper().findAllOpenTicketData();

        for (OpenTicket openTicket : openTicketList) {
            OrderDetail orderDetail = FinancialApplication.getOrderDetailDb().readOrderDetailByTraceNum(openTicket.getTraceNum());
            if (orderDetail != null) {
                totalAmt = orderDetail.getTotalAmt();
            } else {
                totalAmt = openTicket.getTotalAmt();
            }
            Transaction trans = new Transaction();
            trans.setTableID(openTicket.getTableId());
            trans.setTraceNum(openTicket.getTraceNum());
            trans.setTotalAmt(totalAmt);
            trans.setApprovedAmt(totalAmt);
            trans.setBaseAmt(mUseCardView.getBaseAmt());
            trans.setTipAmt(mUseCardView.getTip());
            trans.setTaxAmt(mUseCardView.getTax());

            trans.setmCardBin(mCardBin);
            trans.setmCardType(mCardOrg);
            trans.setmCardHolderName(mCardHolderName);
//            trans.setTransType(RequestVar.TRANS_SALE);
            trans.setTransType("ONLINE");
            trans.setmEmployeeId(openTicket.getEmployeeId());
            if(mEntryMode != null && !mEntryMode.isEmpty()) {
                trans.setEntryMode(mEntryMode.toUpperCase());
            }
            trans.setmTipType("CARD");
            trans.setmTenderType(mTendType);
            trans.setmTerminalRefNum(mRefNum);
            trans.setmBatchNum(mBatchNum);
            trans.setAuthCode(mAuthCode);
            if(mCardNo.length() >= 5) {
                trans.setMaskedPan(mCardNo.substring(mCardNo.length() - 4, mCardNo.length()));
            }

//            String[] tipAmt = PayData.getInstance().getTipAmt();
//            if (null != tipAmt) {
//                Double tipNum = 0.00;
//                for (int i = 0; i < tipAmt.length; i++) {
//                    tipNum = DoubleUtils.sum(tipNum, Double.valueOf(tipAmt[i]));
//                }
//                trans.setTipAmt(Double.toString(tipNum));
//            }
            transactionList.add(trans);
        }

        FinancialApplication.getTransactionDb().saveTransactionList(transactionList);
        if(ifUpload) {
            System.out.println("Upload MultiTrans");
//            OrderInstance orderInterface = OrderInstance.getInstance();
//            orderInterface.asyncUploadMultiTrans(transactionList, "");
        }else{
            //分单上送完成后删除db数据
            FinancialApplication.getOpenTicketDbHelper().deleteAllOpenTicket();
            FinancialApplication.getPayDataDb().clearPayData();
        }
    }

}
