package com.pax.order.menu;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.pax.order.FinancialApplication;
import com.pax.order.OrderProcComplete;
import com.pax.order.ParamConstants;
import com.pax.order.adver.GuideActivity;
import com.pax.order.commonui.widget.CustomAlertDialog;
import com.pax.order.entity.CartData;
import com.pax.order.entity.PayData;
import com.pax.order.entity.StorageGoods;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.orderserver.Impl.GetAllCategoryManager;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.settings.GetTableInstance;
import com.pax.order.settings.SettingsParamActivity;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;
import com.pax.order.util.ToastUtils;

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
 * 2018/7/31 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class MenuPresenter extends BasePresenter<IView> implements MenuContract.Presenter {
    private static String TAG = "MenuPresenter";
    private final MenuContract.View mMenuView;

    public MenuPresenter(@NonNull MenuContract.View menuView) {
        mMenuView = menuView;
        mMenuView.setPresenter(this);
    }

    @Override
    public void start() {
        mMenuView.initView();


    }

    @Override
    public void asyncTableOrdersData() {
//        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.asyncGetAllTableOrders(false, true);
    }

    @Override
    public void syncTableId() {
        String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null);

        if(tableNum== null || tableNum.isEmpty()){
            showTableNumUnexist();
            return;
        }
        String tableId = GetTableInstance.getInstance().GetTableIdByNum(tableNum);
        if(tableId != null && !tableId.isEmpty()){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(ParamConstants.TABLE_ID, tableId);
            editor.apply();
            FinancialApplication.setLoginInfo();
        }else{
            showTableNumUnexist();
//            FinancialApplication.getApp().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.showMessage(FinancialApplication.getApp(), "Get Table ID Error");
//                }
//            });
        }
    }

    @Override
    public void asyncGetTable() {
//        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.asyncGetTable();
    }

    @Override
    public void asyncMenuData() {
//        GetAllCategoryManager getAllCategoryManager = new GetAllCategoryManager();
//        getAllCategoryManager.initLoginInfo(FinancialApplication.getLoginInfo(), OrderProcComplete.getInstance());
//        getAllCategoryManager.asyncGetCategory(null);
//
//        GetAllCategoryManager getAllCategoryManager2 = new GetAllCategoryManager();
//        getAllCategoryManager2.initLoginInfo(FinancialApplication.getLoginInfo(), OrderProcComplete.getInstance());
//        getAllCategoryManager2.asyncGetItem(null);
    }

    @Override
    public int toCart() {
        mMenuView.startCartView();
        return 0;
    }

    @Override
    public int toPay() {
        PayData payData = PayData.getInstance();
        payData.init();




//        OrderInstance orderInterface = OrderInstance.getInstance();
        //获取FixedTip
//        orderInterface.asyncGetSettings();
        // 获取本桌订单明细
//        orderInterface.asyncGetOrderDetail("");
        return 0;
    }

    @Override
    public void toClearCart() {
        CartData.getInstance().getGroupSelect().clear();
        CartData.getInstance().getSelectedList().clear();
        mMenuView.updateBottomView(0, 0, 0);
    }

    @Override
    public void toPowerOffExce() {
        if (PayData.getInstance().isIfNoPaidTicket()) {
            PayData payData = FinancialApplication.getPayDataDb().readPayData();
            if (null != payData) {
                PayData.getInstance().setPayData(payData);
            }
            //断电重启之后的判断处理
            if (PayData.getInstance().isIfSplit()) {
                SplitType splitType = PayData.getInstance().getSplitType();
                SplitStep splitStep = PayData.getInstance().getSplitStep();
                if (splitType == SplitType.TWO) {
                    if (splitStep == SplitStep.ONE) {
                        //跳转到add tip
                        PayData.getInstance().setIfPaymentProcess(true);// 设置进入支付状态
                        mMenuView.startPaymentView();
                    }
                } else if (splitType == SplitType.THREE) {
                    if (splitStep == SplitStep.ONE || splitStep == SplitStep.TWO) {
                        //跳转到add tip
                        PayData.getInstance().setIfPaymentProcess(true);
                        mMenuView.startPaymentView();
                    }
                } else if (splitType == SplitType.BYITEM && splitStep != SplitStep.ZERO) {
                    List<StorageGoods> unPaiList = FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods();
                    List<StorageGoods> prePaiList = FinancialApplication.getOpenTicketDbHelper().findAllPrePaidGoods();
                    if ((null != unPaiList && unPaiList.size() > 0) || (null != prePaiList && prePaiList.size() > 0)) {
                        //跳转到add tip
                        PayData.getInstance().setIfPaymentProcess(true);
                        mMenuView.startPaymentView();
                    }
                }
            }
        }
    }

    @Override
    public boolean checkTableNum() {
        String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null);
        if(tableNum== null || tableNum.isEmpty()){
            showTableNumUnexist();
            return false;
        }
        return true;
    }

    private void showTableNumUnexist(){
        final CustomAlertDialog dialog = new CustomAlertDialog((Activity)mMenuView)
                .setTitleText("Please Select Table Num")
                .setContentText("Login Info -> Select Table Num")
                .setConfirmText("Ok")
                .showCancelButton(false)
                .showConfirmButton(true);
        dialog.setConfirmClickListener(new CustomAlertDialog.OnCustomClickListener() {
            @Override
            public void onClick(CustomAlertDialog alertDialog) {
                dialog.dismiss();
                mMenuView.startSettingView();
            }
        });
        dialog.show();
    }
}
