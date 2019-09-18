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
import com.pax.order.entity.OrderDetail;
import com.pax.order.entity.PayData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.menu.GetFixedTip;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.DoubleUtils;
import com.pax.order.util.IView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class AddTipPresenter extends BasePresenter<IView> implements AddTipContract.Presenter {
    private static final String TAG = "AddTipPresenter";

    private final AddTipContract.View mAddTipView;
    private final List<String> mOptionsItems = new ArrayList<>();
    private int[] mTipPercent = {0, 2, 3, 5, 8, 10, 12, 15, 18, 20, 22, 25, 28, 30, 40, 50, 60, 70, 80, 90, 100};
    public static final int DEFAULT_TIP_INDEX = 5; // 默认小费索引

    private double mSubTotal;
    private double mTax;
    private double mTip;
    private double mNeedPay;
    private DecimalFormat mDecimalFormat = new DecimalFormat("0.00");

    public AddTipPresenter(@NonNull AddTipContract.View addTipView) {
        mAddTipView = addTipView;
        mAddTipView.setPresenter(this);
    }

    public int getTipIndex(){
        return (0 == getFixTipValue())?DEFAULT_TIP_INDEX:0;
    }

    @Override
    public void start() {

        int percent = 0;


        percent = (0 == getFixTipValue()? mTipPercent[DEFAULT_TIP_INDEX] : getFixTipValue() );
        mSubTotal = getSubTotal();
        mTax = getTaxTotal();
        mTip = DoubleUtils.round(mSubTotal * percent / 100);
        mNeedPay = DoubleUtils.round(mSubTotal + mTax + mTip);

        String strPercent;
        String strTip;
        int[] mTipRatio = (0 == getFixTipValue() ? mTipPercent : buildTipPercentRatio());
        for (int i = 0; i < mTipRatio.length; i++) {
            strPercent = mTipRatio[i] + "%";
            strPercent = String.format("%-8s", strPercent);
            strTip = AmountUtils.amountFormat(DoubleUtils.round(mSubTotal * mTipRatio[i] / 100)).toString();
            mOptionsItems.add(strPercent + strTip);
        }

        //显示view页面
        mAddTipView.initView(mOptionsItems, mSubTotal, mTax, mTip, mNeedPay);
    }

    @Override
    public void setTipSelected(int position) {
        int[] mTipRatio = (0 == getFixTipValue() ? mTipPercent : buildTipPercentRatio());

        mTip = DoubleUtils.round(mSubTotal * mTipRatio[position] / 100);
        mNeedPay = DoubleUtils.round(mSubTotal + mTax + mTip);

        mAddTipView.updateView(mTip, mNeedPay);
    }

    @Override
    public double getSubPay() {
        return mSubTotal;
    }

    @Override
    public double getTipSelected() {
        return mTip;
    }

    @Override
    public double getTax() {
        return mTax;
    }

    @Override
    public double getNeendPay() {
        return mNeedPay;
    }

    private Double getTaxTotal() {
        double cost = 0;
        double costFormt = 0;
        boolean ifSplit = PayData.getInstance().isIfSplit();
        SplitType splitType = PayData.getInstance().getSplitType();
        SplitStep splitStep = PayData.getInstance().getSplitStep();

        List<SelectGoods> selectGoods = FinancialApplication.getOrderTicket().getSelectList();
        for(SelectGoods selectGood : selectGoods) {
            cost += selectGood.getTaxAmt();
        }

        if ((ifSplit) && (splitType == SplitType.BYITEM)) {
            List<StorageGoods> selectGoodsList;
            selectGoodsList = FinancialApplication.getOpenTicketDbHelper().findAllStorageGoods();
            for (SelectGoods goods : selectGoodsList) {
                if (goods.getPrePaidQuantity() <= 0) {
                    continue;
                }
                cost += DoubleUtils.round(goods.getPrePaidTaxAmt());
            }
        } else {
            // 从明细中获取总税金
            List<OrderDetail> orderDetailList = FinancialApplication.getOrderDetailDb().findAllOrderDetailData();
            for (OrderDetail orderDetail : orderDetailList) {
                cost += DoubleUtils.round(Double.parseDouble(orderDetail.getTaxAmt()));
            }

            if (ifSplit) {
                if (SplitType.TWO == splitType) {
                    if (SplitStep.ONE == splitStep) {
                        costFormt = cost - Double.valueOf(mDecimalFormat.format(cost / 2));
                    } else {
                        costFormt = Double.valueOf(mDecimalFormat.format(cost / 2));
                    }
                } else if (SplitType.THREE == splitType) {
                    if (SplitStep.TWO == splitStep) {
                        costFormt = cost - 2 * Double.valueOf(mDecimalFormat.format(cost / 3));
                    } else {
                        costFormt = Double.valueOf(mDecimalFormat.format(cost / 3));
                    }
                }
                cost = costFormt;
            }
        }


        return DoubleUtils.round(cost);
    }

    private double getSubTotal() {
        int count = 0;
        double cost = 0;
        double costFormt = 0;
        boolean ifSplit = PayData.getInstance().isIfSplit();
        SplitType splitType = PayData.getInstance().getSplitType();
        SplitStep splitStep = PayData.getInstance().getSplitStep();

        List<StorageGoods> selectGoodsList;
        if ((ifSplit) && (splitType == SplitType.BYITEM)) {
            selectGoodsList = FinancialApplication.getOpenTicketDbHelper().findAllPrePaidGoods();

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
                selectGoodsList.add(goods);
            }


            for (SelectGoods goods : selectGoodsList) {
                count += goods.getPrePaidQuantity();
                cost += goods.getmPrePaidAmt();
            }
            System.out.println("First AddTipPresenter cost: " + cost);


        } else {

            // 从明细中获取总金额
//            List<OrderDetail> orderDetailList = FinancialApplication.getOrderDetailDb().findAllOrderDetailData();
//            for (OrderDetail orderDetail : orderDetailList) {
//                cost += DoubleUtils.round(Double.parseDouble(orderDetail.getTotalAmt()));
//                cost = DoubleUtils.sub(cost, Double.parseDouble(orderDetail.getTaxAmt()));
//            }

            List<SelectGoods> selectGoods = FinancialApplication.getOrderTicket().getSelectList();
            for(SelectGoods selectGood : selectGoods) {
                cost += selectGood.getPrice();
            }

            if (ifSplit) {
                if (SplitType.TWO == splitType) {
                    if (SplitStep.ONE == splitStep) {
                        costFormt = cost - Double.valueOf(mDecimalFormat.format(cost / 2));
                    } else {
                        costFormt = Double.valueOf(mDecimalFormat.format(cost / 2));
                    }
                } else if (SplitType.THREE == splitType) {
                    if (SplitStep.TWO == splitStep) {
                        costFormt = cost - 2 * Double.valueOf(mDecimalFormat.format(cost / 3));
                    } else {
                        costFormt = Double.valueOf(mDecimalFormat.format(cost / 3));
                    }
                }
                cost = costFormt;
            }
        }

        System.out.println("Return AddTipPresenter cost: " + cost);
        return DoubleUtils.round(cost);
    }
    private int getFixTipValue(){
        int guestCount = 0;
        List<OrderDetail> orderDetailList = FinancialApplication.getOrderDetailDb().findAllOrderDetailData();
        for (OrderDetail orderDetail : orderDetailList) {
            String mCount = orderDetail.getGuestCount();
            if(mCount != null && !mCount.isEmpty()){
                guestCount = Integer.parseInt(mCount);
            }
        }
        return GetFixedTip.getInstance().orderGetFixTipValue(guestCount);
    }
    private int[] buildTipPercentRatio(){
        List<Integer> ratioList = new ArrayList<Integer>();
        int fixTip = getFixTipValue();
        int mRatio;

        mRatio = fixTip;
        ratioList.add(fixTip);
        while(true){
            if(mRatio++ > 100) break;
            if(mRatio <= 20 && (0==(mRatio%2))
                    ||(mRatio>20 && mRatio<=50 && (0 == mRatio%5))
                    ||(mRatio>50 && (0 == mRatio%10))){
                ratioList.add(mRatio);
            }
        }
        int[] mTipPer = new int[ratioList.size()];
        for(int i=0; i<ratioList.size();i++){
            mTipPer[i] = ratioList.get(i);
        }
        return mTipPer;
    }
}
