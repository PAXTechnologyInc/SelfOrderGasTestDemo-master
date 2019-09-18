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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.FinancialApplication;
import com.pax.order.adver.GuideActivity;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.widget.CustomAlertDialog;
import com.pax.order.commonui.widget.InputPwdDialog;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.entity.CartData.EOrderStatus;
import com.pax.order.entity.DownloadItemData;
import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsCategory;
import com.pax.order.entity.GoodsItem;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.NotificationMsg;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.PayData;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.entity.TableOrderInfo;
import com.pax.order.eventbus.FCMMessageEvent;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.payment.PaymentActivity;
import com.pax.order.settings.SettingsParamActivity;
import com.pax.order.util.ActivityStack;
import com.pax.order.util.AnimationUtils;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.FragmentManagerUtils;
import com.pax.order.util.InternetCheckUtils;
import com.pax.order.util.MsgProActivity;
import com.paxsz.easylink.api.EasyLinkSdkManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_SUCCESS;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_VERIFICATION_ERROR;
import static com.pax.order.orderserver.constant.spRespCode.TM_ORDER_NO_DETAIL;

public class MenuActivity extends MsgProActivity implements View.OnClickListener, MenuContract.View {
    private static String TAG = "MenuActivity";
    private CountDownTimer mCountTimerView;

    private MenuPresenter mMenuPresenter;
    private ImageView imgCart;
    private RelativeLayout layoutCart;
    private RelativeLayout layoutPay;
    private ViewGroup anim_mask_layout;
    private TextView tvCount_Cart, tvCount_Pay, tvCost, tvToPay, tvCallWaiter, tvClearOrder,tvShowTableId;
    private RelativeLayout mHeaderLayout;
    private ImageView imgHeaderSetting;
    private int cardItemAmount;

    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;
    private FragmentManagerUtils mFragmentManagerUtils;

    private GoodsDisplayFragment mGoodsDisplayFragment;
    private GoodsDetailsFragment mGoodsDetailsFragment;

    //    private GoodsDisplayPresenter mGoodsDisplayPresenter;
    //    private GoodsDetailsPresenter mGoodsDetailsPresenter;

    List<GoodsCategory> mGoodsCategoryTmpList = new ArrayList<>();
    List<GoodsItem> mGoodsItemTmpList = new ArrayList<>();
    private boolean isDownCategoryOk = false;
    private boolean isDwonItemOk = false;
    private boolean isNeedRefresh = false;

    private final static int REQ_DETAILS = 1;
    private final static int REQ_CART = 2;
    private final static int REQ_PAY = 3;
    private final static int REQ_SETTINGS = 4;

    private GoodsItem mItemDetails;
    private GlobalVariable global_var;

    public void dealMenuEvent(ProcessMessage processMessage) {
        List<GoodsCategory> goodsCategoryListTmp;
        List<GoodsItem> goodsItemListTmp;
        MStatusDialog mStatusDialog = new MStatusDialog(this);
        File fileDr = new File(FinancialApplication.getApp().getCacheDir().getPath() + "/image_manager_disk_cache/");


        AppLog.d(TAG, "dealMenuEvent: " + processMessage.getAdvertisementList() + processMessage.getGoodsCategoryList());
        switch (processMessage.getMessageCode()) {
            case MessgeCode.DOWNCATEGORYFAIL:
                isDownCategoryOk = false;
                isDwonItemOk = false;
                isNeedRefresh = false;

                if (fileDr.exists()) {
                    if ((fileDr.list().length > 0)
                            && (DownloadItemData.getInstance().getGoodsCategoryList() != null)
                            && (DownloadItemData.getInstance().getGoodsCategoryList().size() != 0)) {
                        isDownCategoryOk = true;
                        isDwonItemOk = true;
                        isNeedRefresh = true;
                    }
                }
                mStatusDialog.show("Get Item fail", getResources().getDrawable(R.drawable.mn_icon_dialog_error));
                break;
            case MessgeCode.DOWNCATEGORYSUCC:
                isDownCategoryOk = true;
                goodsCategoryListTmp = processMessage.getGoodsCategoryList();
                if (goodsCategoryListTmp != null) {
                    if (goodsCategoryListTmp.size() == mGoodsCategoryTmpList.size()) {
                        //                        比较哈希值，效率高,只要哈希值不同就跳出循环
                        for (int i = 0; i < goodsCategoryListTmp.size(); i++) {
                            if (goodsCategoryListTmp.get(i).hashCode() != mGoodsCategoryTmpList.get(i).hashCode()) {
                                mGoodsCategoryTmpList.clear();
                                mGoodsCategoryTmpList.addAll(goodsCategoryListTmp);
                                DownloadItemData.getInstance().setGoodsCategoryList(goodsCategoryListTmp);
                                isNeedRefresh = true;
                                break;
                            }
                        }
                    } else {
                        mGoodsCategoryTmpList.clear();
                        mGoodsCategoryTmpList.addAll(goodsCategoryListTmp);
                        DownloadItemData.getInstance().setGoodsCategoryList(goodsCategoryListTmp);
                        isNeedRefresh = true;
                    }
                }
                break;
            case MessgeCode.DOWNITEMFAIL:
                isDownCategoryOk = false;
                isDwonItemOk = false;
                isNeedRefresh = false;
                if (fileDr.exists()) {
                    if ((fileDr.list().length > 0)
                            && (DownloadItemData.getInstance().getGoodsItemList() != null)
                            && (DownloadItemData.getInstance().getGoodsItemList().size() != 0)) {
                        isDownCategoryOk = true;
                        isDwonItemOk = true;
                        isNeedRefresh = true;
                    }
                }
                mStatusDialog.show("Get Item fail", getResources().getDrawable(R.drawable.mn_icon_dialog_error));
                break;
            case MessgeCode.DOWNITEMSUCC:
                isDwonItemOk = true;
                goodsItemListTmp = processMessage.getGoodsItemList();
                if (goodsItemListTmp.size() == mGoodsItemTmpList.size()) {
                    for (int i = 0; i < goodsItemListTmp.size(); i++) {
                        if (goodsItemListTmp.get(i).hashCode() != mGoodsItemTmpList.get(i).hashCode()) {
                            mGoodsItemTmpList.clear();
                            mGoodsItemTmpList.addAll(goodsItemListTmp);
                            DownloadItemData.getInstance().setGoodsItemList(goodsItemListTmp);
                            isNeedRefresh = true;
                            break;
                        }
                    }
                } else {
                    mGoodsItemTmpList.clear();
                    mGoodsItemTmpList.addAll(goodsItemListTmp);
                    DownloadItemData.getInstance().setGoodsItemList(goodsItemListTmp);
                    isNeedRefresh = true;
                }
                break;
            default:
                break;
        }

//        List<GoodsAttributes> emptyAttributes = new ArrayList<GoodsAttributes>();
//        GoodsItem itemOne = new GoodsItem(1, 1, "GoodOne", true, 1.23, 1, "CategoryOne", "PicURL", 1, emptyAttributes);
//        List<GoodsItem> items = new ArrayList<GoodsItem>();
//        items.add(itemOne);
//        GoodsCategory categoryOne = new GoodsCategory(1, "typeName", 1, "PicURL", items);
//
//        List<GoodsCategory> goodsCategoryList = new ArrayList<GoodsCategory>();
//        goodsCategoryList.add(categoryOne);
//
//        DownloadItemData.getInstance().setGoodsCategoryList(goodsCategoryList);

        isNeedRefresh = true;
        isDwonItemOk = true;
        isDownCategoryOk = true;
        if (isDwonItemOk && isDownCategoryOk && isNeedRefresh) {
            //有更新和第一次刷新才进入
            mHeaderLayout.setVisibility(View.GONE);
            setupWithFragment(mFragmentManager, R.id.menu_container, mFragmentList);
            mMenuPresenter.toClearCart();
            //            mGoodsDisplayPresenter.upDateView();
            //            mGoodsDetailsPresenter.upDateView();
            isDownCategoryOk = false;
            isDwonItemOk = false;
            isNeedRefresh = false;
        } else if (isDwonItemOk && isDownCategoryOk) {
            //页面切换重新刷新 不管是否有更新menu
            //            setupWithFragment(mFragmentManager, R.id.menu_container, mFragmentList);
            //            mGoodsDisplayPresenter.onResume();
            isDownCategoryOk = false;
            isDwonItemOk = false;
            isNeedRefresh = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MenuActivity onCreate called");
        setContentView(R.layout.activity_menu_new);
        global_var = ((GlobalVariable) this.getApplicationContext());

        initData();
//        initTimer();

        FinancialApplication.getApp().register(this);

        mMenuPresenter.start();
        System.out.println("Presenter Started");


        // change text lift nozzle
        System.out.println("This is holding 10 seconds out of handle");
        Handler h2=new Handler();
        h2.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is holding 10 seconds");
                // check shipping cart items
                global_var.setFuelUp(true);
                if(cardItemAmount>0){
                    // show dialog to check whether need to shipping or not
                    dialogShow(getString(R.string.choose_shipping), true);
                }else{
                    // re-direct to cart info page
                    dialogShow(getString(R.string.fill_reciept), false);
                }
            }
        }, 15000);
    }

    private void dialogShow(String text, final boolean isPickedItem){
        final AlertDialog alert = new AlertDialog.Builder(MenuActivity.this, AlertDialog.THEME_HOLO_DARK).create();
        alert.setCancelable(false);
        // alert.setContentView(R.layout.custom_dialog);

        alert.setTitle(text);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                        if(!isPickedItem){
                            Intent intent = new Intent(MenuActivity.this, OrderMessageActivity.class);
                            startActivity(intent);
                        }
                    }
                });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                        if(isPickedItem){
                            Intent intent = new Intent(MenuActivity.this, ShoppingCartActivity.class);
                            startActivityForResult(intent, REQ_CART);
                        }else{
                            Intent intent = new Intent(MenuActivity.this, OrderMessageActivity.class);
                            startActivity(intent);
                        }
                    }
                });

        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        startTimer();
        ActivityStack.getInstance().popAllButThis(this);

        // 断电异常处理
        mMenuPresenter.toPowerOffExce();

        System.out.println("MenuActivity Resumed ");
        if(global_var.getOrderTicket() != null){
            updatePayIcon(true);
//            FinancialApplication.getController().setTraceNum(mTableOrderInfoList.get(0).getOrderInfoList().get(0)
//                    .getTraceNum());
//            FinancialApplication.getController().setIsOpen(true);
        }
        else {
            updatePayIcon(false);
        }


        //同步数据
//        mMenuPresenter.asyncTableOrdersData();
//        mMenuPresenter.asyncGetTable();
//        mMenuPresenter.asyncMenuData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FinancialApplication.getApp().unregister(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        // Create the presenter
        mMenuPresenter = new MenuPresenter((MenuContract.View) this);
        return mMenuPresenter;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCountTimerView.cancel();
    }

    /**
     * 主要的方法，重写dispatchTouchEvent
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            //获取触摸动作，如果ACTION_UP，计时开始。
            case MotionEvent.ACTION_UP:
//                mCountTimerView.start();
                break;
            //否则其他动作计时取消
            default:
//                mCountTimerView.cancel();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setPresenter(MenuContract.Presenter presenter) {

    }

    @Override
    public void dealProcessMsg(ProcessMessage processMessage) {
        AppLog.d(TAG, "dealHandlerMsg: " + processMessage.getMessageCode());
        System.out.println("MenuActivityu DealProcessMsg");

        switch (processMessage.getMessageCode()) {
            case MessgeCode.ORDER_DETAIL_MSG:
                if (SP_RESPONSE_SUCCESS.equals(processMessage.getRspMsg()
                        .getResultCode())) {
                    startPaymentView();
                } else {
                    if (TM_ORDER_NO_DETAIL.equals(processMessage.getRspMsg()
                            .getResultCode())) {
                        new MStatusDialog(this).show(getString(R.string.loading_orders_noexist), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                        updatePayIcon(false);
                    } else if (SP_RESPONSE_VERIFICATION_ERROR.equals(processMessage.getRspMsg()
                            .getResultCode())) {
                        new MStatusDialog(this).show(getString(R.string.verification_error), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                    } else {
                        new MStatusDialog(this).show(processMessage.getRspMsg().getResultCode() + ", " + processMessage.getRspMsg().getResultMessage(),
                                getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                    }
                }
                break;
            case MessgeCode.ORDER_DETAIL_SUCC:
                startPaymentView();
                break;

            case MessgeCode.ORDER_DETAIL_FAIL:
                new MStatusDialog(this).show(getString(R.string.loading_orders_failed), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                break;

            case MessgeCode.ORDER_NO_DETAIL:
                new MStatusDialog(this).show(getString(R.string.loading_orders_noexist), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                updatePayIcon(false);
                break;

            case MessgeCode.GET_TABLE_ORDER_SUCC:

                List<TableOrderInfo> mTableOrderInfoList = processMessage.getTableOrderInfoList();
                String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                        .getString(ParamConstants.TABLE_NUM, null);
                if (null == mTableOrderInfoList || 0 == mTableOrderInfoList.size() || global_var.getPaid(tableNum)) {
                    updatePayIcon(false);
                    FinancialApplication.getController().setTraceNum("");
                    FinancialApplication.getController().setIsOpen(false);
                } else {
                    // 刷新pay图标
                    if (mTableOrderInfoList.get(0).isOpened()) {
                        updatePayIcon(true);
                        FinancialApplication.getController().setTraceNum(mTableOrderInfoList.get(0).getOrderInfoList().get(0)
                                .getTraceNum());
                        FinancialApplication.getController().setIsOpen(true);
                    } else {
                        updatePayIcon(false);
                        FinancialApplication.getController().setTraceNum("");
                        FinancialApplication.getController().setIsOpen(false);
                    }
                }


                break;

            case MessgeCode.GET_TABLE_ORDER_FAIL:
                new MStatusDialog(this).show(getString(R.string.table_status_failed), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
                break;
            case MessgeCode.GET_TABLE_ID_SUC:
//                mMenuPresenter.syncTableId();
//                mMenuPresenter.asyncTableOrdersData();
                dispTableId();
                break;
            case MessgeCode.DOWNCATEGORYFAIL:
            case MessgeCode.DOWNCATEGORYSUCC:
            case MessgeCode.DOWNITEMFAIL:
            case MessgeCode.DOWNITEMSUCC:
                dealMenuEvent(processMessage);
                break;
            case MessgeCode.REGISTER_DEVICE_RETRY:
                MProgressDialog.showProgress(MenuActivity.this, getString(R.string.device_register));
//                OrderInstance orderInterface = OrderInstance.getInstance();
//                orderInterface.asyncRegisterDevice();
                System.out.println("Making Call to async Register Device again");
                break;
            case MessgeCode.REGISTER_DEVICE_SUC:
                MProgressDialog.dismissProgress();
//                mMenuPresenter.asyncGetTable();
//                mMenuPresenter.asyncMenuData();
                break;
            default:
                break;
        }

        // 释放显示
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MProgressDialog.dismissProgress();
            }
        });
    }

    @Override
    public void initView() {

        anim_mask_layout = (RelativeLayout) findViewById(R.id.containerLayout);
        mHeaderLayout = (RelativeLayout) findViewById(R.id.layout_header);

        imgHeaderSetting = (ImageView) findViewById(R.id.menu_header_setting);
        tvCount_Cart = (TextView) findViewById(R.id.tvCount_Cart);
//        tvCount_Pay = (TextView) findViewById(R.id.tvCount_Pay);
//        tvCost = (TextView) findViewById(R.id.tvCost);
//        tvToPay = (TextView) findViewById(R.id.tvToPay);
        tvCallWaiter = (TextView) findViewById(R.id.tvCallWaiter);
        tvClearOrder = (TextView) findViewById(R.id.tvClearOrder);
        tvShowTableId = (TextView)findView(R.id.tvShowTableId);

        imgCart = (ImageView) findViewById(R.id.imgCart);
        layoutCart = (RelativeLayout) findViewById(R.id.layout_cart);
//        layoutPay = (RelativeLayout) findViewById(R.id.layout_pay);
        layoutCart.setOnClickListener(this);
//        layoutPay.setOnClickListener(this);
        tvCallWaiter.setOnClickListener(this);
        tvClearOrder.setOnClickListener(this);
        imgHeaderSetting.setOnClickListener(this);

        System.out.println("MenuActivity initView");
        mHeaderLayout.setVisibility(View.GONE);
        setupWithFragment(mFragmentManager, R.id.menu_container, mFragmentList);
        mMenuPresenter.toClearCart();
        // show table_status_processing.
//        MProgressDialog.showProgress(MenuActivity.this, getString(R.string.table_status_processing));
    }

    private void initData() {
        System.out.println("MenuActivity initData");
        mFragmentList = new ArrayList<>();
        mGoodsDisplayFragment = new GoodsDisplayFragment();
        mGoodsDetailsFragment = new GoodsDetailsFragment();

        mFragmentList.add(mGoodsDisplayFragment);

        mFragmentList.add(mGoodsDetailsFragment);
        mFragmentManager = getSupportFragmentManager();
    }

    private void setupWithFragment(FragmentManager manager, int containerResid, List<Fragment> fragments) {
        if (mFragmentManagerUtils != null) {
            mFragmentManagerUtils.detach();
        }
        if (containerResid != 0) {
            mFragmentManagerUtils = new FragmentManagerUtils(manager, containerResid, fragments);
        } else {
            mFragmentManagerUtils = new FragmentManagerUtils(manager, fragments);
        }
    }

    private void startTimer() {
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCountTimerView.start();
            }
        });
    }

    private void initTimer() {
        String standby_time = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext())
                .getString(ParamConstants.STANDBY_TIME, "30");
        //初始化CountTimer，设置倒计时为5s。
        mCountTimerView = new CountDownTimer(Integer.parseInt(standby_time) * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                System.out.println("MenuActivity Starting GuideActivity");
                // Pay小标亮或购物车小标亮（有订单/购物车有商品/在支付流程中）
//                if (tvCount_Pay.getVisibility() == View.VISIBLE || tvCount_Cart.getVisibility() == View.VISIBLE
//                        || PayData.getInstance().isIfPaymentProcess()) {
////                    startTimer();
//                    return;
//                }

//                startActivity(GuideActivity.class);
            }
        };
    }

    @Override
    public void startSettingView() {
        startSettings();
    }

    @Override
    public void startCartView() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivityForResult(intent, REQ_CART);
    }

    @Override
    public void startPaymentView() {
        EasyLinkSdkManager easyLink = global_var.getEasylink();
        try{
            easyLink.getConnectedDevice().getDeviceName();
            Intent intent = new Intent(this, PaymentActivity.class);
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MenuActivity.this,"Please Connect Bluetooth !",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity.this, MenuActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

        }

    }

    public void startSettings() {
//        mCountTimerView.cancel();// 广告页定时器取消
        final InputPwdDialog pwdDialog = new InputPwdDialog(MenuActivity.this, 32, getString(R.string.prompt_sys_pwd),
                null);
        pwdDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss();
                        mMenuPresenter.checkTableNum();
                    }
                    return true;
                }
                return false;

            }
        });
        pwdDialog.setCancelable(false);
        pwdDialog.setPwdListener(new InputPwdDialog.OnPwdListener() {
            @Override
            public void onSucc(String data) {
                String pwd = FinancialApplication.getLogPwd();
                if (!data.equals(pwd)) {
                    final CustomAlertDialog dialog1 = new CustomAlertDialog(MenuActivity.this);
                    dialog1.setTitleText("Incorrect password");
                    dialog1.setCancelText("Cancel");
                    dialog1.setConfirmText("Continue");
                    dialog1.setCancelClickListener(new CustomAlertDialog.OnCustomClickListener() {
                        @Override
                        public void onClick(CustomAlertDialog alertDialog) {
                            dialog1.dismiss();
                            pwdDialog.dismiss();
//                            mCountTimerView.start();// 广告页定时器继续
                            mMenuPresenter.checkTableNum();
                        }
                    });

                    dialog1.setConfirmClickListener(new CustomAlertDialog.OnCustomClickListener() {
                        @Override
                        public void onClick(CustomAlertDialog alertDialog) {
                            pwdDialog.clearContentText();
                            dialog1.dismiss();
                        }
                    });

                    dialog1.show();
                    dialog1.showCancelButton(true);
                    dialog1.showConfirmButton(true);
                } else {
                    pwdDialog.dismiss();
                    // 跳转参数设置界面
                    Intent intent = new Intent(MenuActivity.this, SettingsParamActivity.class);
                    startActivityForResult(intent, REQ_SETTINGS);
                }
            }

            @Override
            public void onErr() {
                pwdDialog.dismiss();
                return;
            }
        });
        pwdDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("MenuActivity onActivityResult");
        switch (requestCode) {

            case REQ_CART:
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    EOrderStatus orderStatus = (EOrderStatus) bundle.get("orderStatus");
                    if (orderStatus == EOrderStatus.BACK) {

                    } else if (orderStatus == EOrderStatus.ORDER) {
                        mMenuPresenter.toClearCart();
                        mFragmentManagerUtils.changeFragment(0, false); // 切换到商品显示页面
                    }
                } else {
                    mMenuPresenter.toClearCart();
                }
                break;

            case REQ_PAY:
                break;

            case REQ_SETTINGS:
                if (null != data) {
                    boolean downloadFlag = data.getBooleanExtra("ifdownloadGoods", false);
                    if (downloadFlag) {
                        mMenuPresenter.start();
                    }
                }

//                initTimer();//重新初始化定时器
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_cart:
                if (tvCount_Cart.getVisibility() == View.VISIBLE) {
                    mMenuPresenter.toCart();
                }
                break;

//            case R.id.layout_pay:
//                if (tvCount_Pay.getVisibility() == View.VISIBLE) {
//                    // show loading_orders...
//                    MProgressDialog.showProgress(MenuActivity.this, getString(R.string.loading_orders));
//                    mMenuPresenter.toPay();
//                    startPaymentView();
//                    if(MProgressDialog.isShowing()){
//                        MProgressDialog.dismissProgress();
//                    }
//                }
//                break;

            case R.id.tvCallWaiter:
                if ((!InternetCheckUtils.checkWifiConnect(this))
                        || (!InternetCheckUtils.checkConnectingConnect(this))) {
                    new MStatusDialog(this).show(getString(R.string.no_internet), getResources().getDrawable(R.drawable.mn_icon_dialog_warn));
                } else {
//                    OrderInstance.getInstance().asyncSendNotification("1");
                    System.out.println("Making call to async sent notification");
                }
                break;

            case R.id.tvClearOrder:
                mMenuPresenter.toClearCart();
                break;
            case R.id.menu_header_setting:
                startSettings();
                break;
            default:
                break;
        }
    }

    // 查询商品明细
    public void startDetailsView(GoodsItem item) {
        mItemDetails = item;
        mFragmentManagerUtils.changeFragment(1, true);
    }

    public GoodsItem getmItemDetails() {
        return mItemDetails;
    }

    // 查询商品明细完毕
    public void finishDetailsView() {
        mFragmentManagerUtils.changeFragment(0, false);
    }

    // 刷新底栏
    @Override
    public void updateBottomView(int cartCount, double cartCost, int payCount) {
        cardItemAmount = cartCount;

        if (cartCount < 1 ) {
            tvCount_Cart.setVisibility(View.GONE);
            imgCart.setImageResource(R.drawable.icon_cart_disabled);
            tvClearOrder.setEnabled(false);
            tvClearOrder.setBackgroundColor(getResources().getColor(R.color.pay_bg_color));
        } else {
            tvCount_Cart.setVisibility(View.VISIBLE);
            imgCart.setImageResource(R.drawable.icon_cart_new);
            tvClearOrder.setEnabled(true);
            tvClearOrder.setBackgroundColor(getResources().getColor(R.color.primary_yellow_color));
        }

        tvCount_Cart.setText(String.valueOf(cartCount));
        //        tvCost.setText(AmountUtils.amountFormat(cartCost));

//        if (cartCost > 0) {
//            tvCost.setTextColor(getResources().getColor(R.color.primary_red_color));
//        } else {
//            tvCost.setTextColor(getResources().getColor(R.color.pay_text_color));
//        }

    }

    private void updatePayIcon(boolean isEnable) {
        System.out.println("UpdatePayIcon Called");
        if (isEnable) {
//            tvToPay.setTextColor(getResources().getColor(R.color.primary_black_color));
//            tvToPay.setBackgroundColor(getResources().getColor(R.color.primary_yellow_color));
//            tvCount_Pay.setVisibility(View.VISIBLE);
//            tvCount_Pay.setText(String.valueOf(1));

//            mCountTimerView.cancel();// 广告页定时器取消
        } else {
//            tvToPay.setTextColor(getResources().getColor(R.color.pay_text_color));
//            tvToPay.setBackgroundColor(getResources().getColor(R.color.pay_bg_color));
//            tvCount_Pay.setVisibility(View.GONE);

//            mCountTimerView.start();// 广告页定时器启动
        }
    }

    public void playAnimation(int[] start_location) {
        int[] loc = new int[2];
        imgCart.getLocationInWindow(loc);
        loc[0] += 38;
        loc[1] += 38;
        AnimationUtils.playAnimation(this, anim_mask_layout, start_location, loc);
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFCMMessageEvent(FCMMessageEvent event) {
        AppLog.d("TAG", "onFCMMessageEvent");

        NotificationMsg notificationMsg = (NotificationMsg) event.getData();

        if (event.getStatus() == FCMMessageEvent.Status.MSG_SERVER) {

            // 判断是否在支付流程中
            if (PayData.getInstance().isIfPaymentProcess()) {
                return;
            }

            // 释放其他页面，回到主界面
            ActivityStack.getInstance().popAllButThis(this);

            FCMMessageEvent.MsgType msgType = FCMMessageEvent.MsgType.getByValue(Integer.parseInt(notificationMsg.getMessageType()));
            switch (msgType) {
                case MSG_TYPE_CALL_SERVERR:
                    break;

                case MSG_TYPE_GET_CHECK:
                    new MStatusDialog(this).show(getString(R.string.msg_get_check),
                            getResources().getDrawable(R.drawable.mn_icon_dialog_ok), 5000);
                    break;

                case MSG_TYPE_READY_TO_PAY:
                    new MStatusDialog(this).show(getString(R.string.msg_ready_to_pay),
                            getResources().getDrawable(R.drawable.mn_icon_dialog_ok), 5000);
                    break;

                case MSG_TYPE_PAYMENT_COMPLETE:
                    new MStatusDialog(this).show(getString(R.string.msg_payment_complete),
                            getResources().getDrawable(R.drawable.mn_icon_dialog_ok), 5000);
                    break;

                case MSG_TYPE_MODIFY_TICKET:
                    new MStatusDialog(this).show(getString(R.string.msg_modify_ticket),
                            getResources().getDrawable(R.drawable.mn_icon_dialog_ok), 5000);
                    break;
                case MSG_TYPE_ORDER_COMPLETE:
                    new MStatusDialog(this).show(getString(R.string.settle_complete),
                            getResources().getDrawable(R.drawable.mn_icon_dialog_ok), 5000);
                    break;

                default:
                    break;
            }
        } else {
            FCMMessageEvent.MsgType msgType = FCMMessageEvent.MsgType.getByValue(Integer.parseInt(notificationMsg.getMessageType()));
//            if ((FCMMessageEvent.MsgType.MSG_TYPE_PAYMENT_COMPLETE == msgType) && ("0".equals
////                    (notificationMsg.getIsOpen())))//没有订单清除数据库
            if ((FCMMessageEvent.MsgType.MSG_TYPE_PAYMENT_COMPLETE == msgType) && (!notificationMsg.isHaveUnpaidOrder()))//没有订单清除数据库
                clearDatabase();
        }

        // 刷新pay图标
        if (notificationMsg.isHaveUnpaidOrder()) {
            updatePayIcon(true);
            FinancialApplication.getController().setIsOpen(true);
            FinancialApplication.getController().setTraceNum(notificationMsg.getTraceNum());
        } else {
            updatePayIcon(false);
            FinancialApplication.getController().setIsOpen(false);
            FinancialApplication.getController().setTraceNum("");
        }
    }

    private void onProcessFCMMessage(String traceNum) {
        AppLog.d("onProcessFCMMessage", "onProcessFCMMessage");

        // 判断是否在支付流程中
        if (PayData.getInstance().isIfPaymentProcess()) {
            return;
        }

        // 释放其他页面，回到主界面
        ActivityStack.getInstance().popAllButThis(this);

        // 判断是否是存在订单号
        OpenTicket openTicket = FinancialApplication.getOpenTicketDbHelper().findOpenTicketByTraceNum(traceNum);
        if (openTicket == null) {
            new MStatusDialog(this).show(getString(R.string.modify_ticket_no_exist), getResources().getDrawable(R.drawable.mn_icon_dialog_error), 5000);
            return;
        }

        // 显示 Modify Ticket...
        MProgressDialog.showProgress(this, getString(R.string.modify_ticket));

        // 调用获取modify接口同步数据库
//        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.asyncGetOrderDetail(traceNum);
        System.out.println("Making call to async get order detail");
    }

    private void clearDatabase() {
        FinancialApplication.getOpenTicketDbHelper()
                .deleteAllOpenTicket();
        FinancialApplication.getOpenTicketDbHelper()
                .deleteAllStorageGoods();
        FinancialApplication.getPayDataDb()
                .clearPayData();
        FinancialApplication.getTransactionDb()
                .clearTransaction();
    }

    private void dispTableId(){
        String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null);
        String s = String.format("Table id: %s",tableNum);
        tvShowTableId.setText(s);
    }

}
