/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 19-2-18 下午5:03           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.adver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pax.ecrsdk.message.BaseResponse;
import com.pax.order.FinancialApplication;
import com.pax.order.OrderProcComplete;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.commonui.widget.CustomAlertDialog;
import com.pax.order.entity.AdverItemData;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.menu.MenuActivity;
import com.pax.order.orderserver.Impl.GetAllCategoryManager;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.pay.Pay;
import com.pax.order.pay.payInterfaceFactory.PayCallbackFactory;
import com.pax.order.pay.payInterfaceFactory.PayFactory;
import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.PayResponseVar;
import com.pax.order.util.ADView;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.InternetCheckUtils;
import com.pax.order.util.MsgProActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

public class GuideActivity extends MsgProActivity implements GuideContract.View {

    private BGABanner mGuideBanner;
    private final String TAG = GuideActivity.class.getSimpleName();
    private GuidePresenter mGuidePresenter;
    private List<View> adViewList;

    @Override
    public void dealProcessMsg(ProcessMessage processMessage) {
        System.out.println("DealProcessMsg called GuideActivity");
        switch (processMessage.getMessageCode()) {
            case MessgeCode.DOWNADVERFAIL:
                //首次下载失败才提示
                if (AdverItemData.getAdvertisementList() == null) {
                    new MStatusDialog(this, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                        @Override
                        public void onDismiss() {
                        }
                    }).build()).show("DownLoad adver failed", getResources().getDrawable(R.drawable.mn_icon_dialog_error));
                }
                break;
            case MessgeCode.DOWNADVERTIMEOUT:
                new MStatusDialog(this, new MDialogConfig.Builder().setOnDialogDismissListener(new OnDialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        //                        mAdvertisementList = AdverItemData.getAdvertisementList();
                        //                        dispAdver();
                    }
                }).build()).show("Oops! Network disconnect!", getResources().getDrawable(R.drawable.mn_icon_dialog_error));
                break;
            case MessgeCode.DOWNADVERSUCC:
                Log.i(TAG,"ADV DOWN SUC!!");
                mGuidePresenter.setAdverisement(processMessage.getAdvertisementList());
                break;

            case MessgeCode.REGISTER_DEVICE_FAIL:
                Log.i(TAG,"REG FAIL");
//                reRegisterDevice();
                break;
            case MessgeCode.REGISTER_DEVICE_SUC:
                Log.i(TAG,"reg----- suc DeviceId:"+FinancialApplication.getApp().getDeviceId()+
                        "....Token:"+FinancialApplication.getApp().getToken());
                MProgressDialog.dismissProgress();
                checkInternetAndDownloadAdver();
                asyncStoreInfo();
                break;
            default:
                break;
        }

    }

    @Override
    public void dispAdver(List<String> adverUrlList) {
        System.out.println("Start Display Advertisement GuideActivity" );
        if ((adverUrlList == null) || (adverUrlList.size() == 0)) {
            Intent intent = new Intent(GuideActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            adViewList = new ArrayList<>();
            for (String url : adverUrlList) {
                adViewList.add(new ADView(GuideActivity.this));
            }
            System.out.println("Display Advertisement GuideActivity" );
            processDisplay();
            processClick();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("GuideActivity Created");
        commSet();
        System.out.println("GuideActivity commSet");
        initView();
        System.out.println("GuideActivity initView");
    }

    private void promptOperConnect() {
        new MStatusDialog(this).show(getString(R.string.q20comm_err),
                this.getResources().getDrawable(R.drawable.mn_icon_dialog_error), 4000);
    }

    private void commSet() {
        Pay.Payment sale;

        PayCallbackFactory.getInstance().setPayCallback(new Pay.PayListener() {
            @Override
            public void onResult(IBaseResponse responseVar) {
                if (Integer.parseInt(responseVar.getResult()) != 0) {
                    promptOperConnect();
                }
            }
        });

        sale = PayFactory.getPaymentInstance();
        // Not using
//        if (sale != null) {
//            sale.doCommSetting(GuideActivity.this, true);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseResponse baseResponse;
        IBaseResponse responseVar;

        baseResponse = FinancialApplication.getITransAPI().onResult(requestCode, resultCode, data);
        if ((baseResponse != null) && (baseResponse.getRspCode() != 0)) {
            responseVar = new PayResponseVar();
            responseVar.setResult(String.valueOf(baseResponse.getRspCode()));
            PayCallbackFactory.getInstance().getPayCallback().onResult(responseVar);
        }
    }

    private void checkInternetAndDownloadAdver() {
        System.out.println("CheckInternetAndDownloadAdver");
        if (!InternetCheckUtils.checkWifiConnect(this)) {
            final CustomAlertDialog dialog = new CustomAlertDialog(this)
                    .setTitleText("WIFI disconnect, go to setting?")
                    .setCancelText("Cancel")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .showConfirmButton(true);
            dialog.setCancelClickListener(new CustomAlertDialog.OnCustomClickListener() {
                @Override
                public void onClick(CustomAlertDialog alertDialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(GuideActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setConfirmClickListener(new CustomAlertDialog.OnCustomClickListener() {
                @Override
                public void onClick(CustomAlertDialog alertDialog) {
                    dialog.dismiss();
                    FinancialApplication.getDal().getSys().enableNavigationBar(true);
                    FinancialApplication.getDal().getSys().enableStatusBar(true);
                    FinancialApplication.getDal().getSys().showNavigationBar(true);
                    FinancialApplication.getDal().getSys().showStatusBar(true);
                    Intent intent = null;
                    if (Build.VERSION.SDK_INT > 10) {
                        intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    } else {
                        intent = new Intent();
                        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                        intent.setComponent(componentName);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    startActivity(intent);
                }
            });
            dialog.show();
        } else {
            mGuidePresenter.start();
//            GetAllCategoryManager categoryManager = new GetAllCategoryManager();
//            categoryManager.initLoginInfo(FinancialApplication.getLoginInfo(), OrderProcComplete.getInstance());
//            categoryManager.asyncGetAdvertisement(null);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        mGuideBanner = findViewById(R.id.guide_banner);

    }

    private void processDisplay() {
        System.out.println("Process Display GuideActivity");
        downloadImage();

        mGuideBanner.setAdapter(new BGABanner.Adapter<ADView, String>() {
            @Override
            public void fillBannerItem(final BGABanner banner, final ADView itemView, @Nullable final String model, int position) {

                // 开始动画
                FinancialApplication.getApp().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemView.startAnimation();
                    }
                });

                Glide.with(GuideActivity.this)
                        .load(model)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model,
                                                           Target<GlideDrawable> target,
                                                           boolean isFromMemoryCache,
                                                           boolean isFirstResource) {
                                // 结束动画
                                FinancialApplication.getApp().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        itemView.stopAnimation();
                                        itemView.getTextView().setVisibility(View.GONE);
                                    }
                                });

                                return false;
                            }
                        })
                        .error(R.mipmap.icon_default)
                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .dontAnimate()
                        .into(itemView.getImageView());
            }
        });
        mGuideBanner.setData(adViewList, mGuidePresenter.getAdverUrl(), null);
    }

    private void downloadImage() {
        System.out.println("Download Image GuideActivity");
        FinancialApplication.getApp().runInBackground(new Runnable() {
            @Override
            public void run() {
                try {
                    for (String adverUrl : mGuidePresenter.getAdverUrl()) {
                        final Context context = getApplicationContext();
                        FutureTarget<File> target = Glide.with(context)
                                .load(adverUrl)
                                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
//                        final File imageFile = target.get();
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(context, imageFile.getPath(), Toast.LENGTH_LONG).show();
//                            }
//                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processClick() {
        System.out.println("ProcessClick GuideActivity");
        mGuideBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                Intent intent = new Intent(GuideActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String reqKey = FinancialApplication.getApp().getReqEncryptKey();
//        if(null != reqKey && !reqKey.isEmpty()){
//            checkInternetAndDownloadAdver();
//        }else{
////            registerDevice();
//        }
    }

    @Override
    protected BasePresenter createPresenter() {
        mGuidePresenter = new GuidePresenter(this);
        return mGuidePresenter;
    }

    @Override
    public void setPresenter(GuideContract.Presenter presenter) {

    }

    private void reRegisterDevice() {

        final CustomAlertDialog dialog = new CustomAlertDialog(this)
                .setTitleText("Register fail! please retry")
                .setCancelText("Cancel")
                .setConfirmText("Ok")
                .showCancelButton(true)
                .showConfirmButton(true);
        dialog.setCancelClickListener(new CustomAlertDialog.OnCustomClickListener() {
            @Override
            public void onClick(CustomAlertDialog alertDialog) {
                FinancialApplication.finishAll();
               // checkInternetAndDownloadAdver();
            }
        });
        dialog.setConfirmClickListener(new CustomAlertDialog.OnCustomClickListener() {
            @Override
            public void onClick(CustomAlertDialog alertDialog) {
                registerDevice();
            }
        });
        dialog.show();


    }

    private  void registerDevice(){
        System.out.println("RegisterDevice Called GuideActivity");
        if (!InternetCheckUtils.checkWifiConnect(this)) {
            final CustomAlertDialog dialog = new CustomAlertDialog(this)
                    .setTitleText("WIFI disconnect, go to setting?")
                    .setCancelText("Cancel")
                    .setConfirmText("Ok")
                    .showCancelButton(true)
                    .showConfirmButton(true);
            dialog.setCancelClickListener(new CustomAlertDialog.OnCustomClickListener() {
                @Override
                public void onClick(CustomAlertDialog alertDialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(GuideActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.setConfirmClickListener(new CustomAlertDialog.OnCustomClickListener() {
                @Override
                public void onClick(CustomAlertDialog alertDialog) {
                    dialog.dismiss();
                    FinancialApplication.getDal().getSys().enableNavigationBar(true);
                    FinancialApplication.getDal().getSys().enableStatusBar(true);
                    FinancialApplication.getDal().getSys().showNavigationBar(true);
                    FinancialApplication.getDal().getSys().showStatusBar(true);
                    Intent intent = null;
                    if (Build.VERSION.SDK_INT > 10) {
                        intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    } else {
                        intent = new Intent();
                        ComponentName componentName = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                        intent.setComponent(componentName);
                        intent.setAction("android.intent.action.VIEW");
                    }
                    startActivity(intent);
                }
            });
            dialog.show();
        }else {
            MProgressDialog.showProgress(GuideActivity.this, getString(R.string.device_register));
            System.out.println("Making Call to Device Registration");
//            OrderInstance orderInterface = OrderInstance.getInstance();
//            orderInterface.asyncRegisterDevice();
        }
    }

    private void asyncStoreInfo() {
        System.out.println("Making Call to Async Storing info");
//        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.asyncGetStoreInfo();
    }


}


