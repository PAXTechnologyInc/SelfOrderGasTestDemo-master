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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.entity.PayData;
import com.pax.order.entity.StorageGoods;
import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class PaymentActivity extends BaseActivity implements View.OnClickListener,
        VerticalTabLayout.OnTabSelectedListener {

    private TextView tvHeaderTitle;
    private ImageView mHeaderBack;
    private VerticalTabLayout mTabLayout;
    private FrameLayout mContentContainer;
    private int[] mTabTitleId = {
            R.string.check_menu,
            R.string.split_select,
            R.string.item_select,
            R.string.add_tip,
            R.string.use_card,
    };
    private int[] mTabActivedImageId = {
            R.mipmap.icon_check_menu_cur,
            R.mipmap.icon_split_select_cur,
            R.mipmap.icon_item_select_cur,
            R.mipmap.icon_add_tip_cur,
            R.mipmap.icon_swipe_card_cur,
    };
    private int[] mTabNormalImageId = {
            R.mipmap.icon_check_menu_done,
            R.mipmap.icon_split_select_done,
            R.mipmap.icon_item_select_done,
            R.mipmap.icon_add_tip_done,
            R.mipmap.icon_swipe_card_done,
    };
    private List<Fragment> mFragmentList;
    private FragmentManager mFragmentManager;

    private double mTip = 0;    //小费金额
    private double mTax = 0;    //税金金额
    private double mSubTotal = 0;       //订单总计金额
    private double mNeedPayAmoumt = 0;  //待支付金额
    private double mSubSplitPay = 0; //没有分单支付是等于SubTotal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initData();
        initView();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    protected void initView() {
        System.out.println("PaymentActivity initView");
        tvHeaderTitle = (TextView) findViewById(R.id.header_title);
        mTabLayout = (VerticalTabLayout) findViewById(R.id.tab_layout);
        mContentContainer = (FrameLayout) findViewById(R.id.payment_container);

        for (int i = 0; i < mTabTitleId.length; i++) {
            mTabLayout.addTab(new QTabView(this)
//                    .setBackground(R.drawable.tab_selector)
                            .setTitle(new TabView.TabTitle.Builder()
                                    .setTextColor(getResources().getColor(R.color.left_active_textcolor),
                                            getResources().getColor(R.color.left_normal_textcolor))
                                    .setTextSize(16)
                                    .setTextBold(true)
                                    .setContent(getString(mTabTitleId[i]))
                                    .build())
                            .setIcon(new TabView.TabIcon.Builder()
                                    .setIcon(mTabActivedImageId[i], mTabNormalImageId[i])
                                    .setIconGravity(Gravity.TOP)
                                    .setIconSize(120, 108)
                                    .build())
            );
        }

        for(int i = 0; i < mFragmentList.size(); i++){
            System.out.println("mFragmentList: " + mFragmentList.get(i).toString());
        }
        mTabLayout.setupWithFragment(mFragmentManager, R.id.payment_container, mFragmentList);
        mTabLayout.setTabInterceptTouch(true);

        if (PayData.getInstance().isIfSplit()) {
            SplitType splitType = PayData.getInstance().getSplitType();
            SplitStep splitStep = PayData.getInstance().getSplitStep();
            if (splitType == SplitType.TWO) {
                if (splitStep != SplitStep.TWO && splitStep != SplitStep.ZERO) {
                    setTabVisibility(2, View.GONE);
                    setTabSelected(3);
                }
            } else if (splitType == SplitType.THREE) {
                if (splitStep != SplitStep.THREE && splitStep != SplitStep.ZERO) {
                    setTabVisibility(2, View.GONE);
                    setTabSelected(3);
                }
            } else if (splitType == SplitType.BYITEM && splitStep != SplitStep.ZERO) {
                List<StorageGoods> unPaiList = FinancialApplication.getOpenTicketDbHelper().findAllUnPaidGoods();
                List<StorageGoods> prePaiList = FinancialApplication.getOpenTicketDbHelper().findAllPrePaidGoods();
                if ((null != unPaiList && unPaiList.size() > 0) || (null != prePaiList && prePaiList.size() > 0)) {
                    setTabVisibility(2, View.VISIBLE);
                    setTabSelected(2);
                }
            }
        } else {
            setTabSelected(0);
            setTabVisibility(1, View.GONE);
            setTabVisibility(2, View.GONE);
        }

        mHeaderBack = (ImageView) findViewById(R.id.header_back);
        mHeaderBack.setOnClickListener(this);
    }

    protected void initData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new CheckMenuFragment());
        mFragmentList.add(new SplitSelectFragment());
        mFragmentList.add(new ItemSelectFragment());
        mFragmentList.add(new AddTipFragment());
        mFragmentList.add(new UseCardFragment());
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    public void onTabSelected(TabView tab, int position) {

    }

    @Override
    public void onTabReselected(TabView tab, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.header_back:
                boolean ifSplit = PayData.getInstance().isIfSplit();
                SplitStep splitStep = PayData.getInstance().getSplitStep();
                if ((ifSplit) && (splitStep != SplitStep.ZERO)) {
                    //不允许退出，后续完善.
                    Toast.makeText(PaymentActivity.this, "Please proceed with payment.", Toast.LENGTH_SHORT).show();
                } else {
                    headerBack();
                }
                break;

            default:
                break;
        }
    }

    private void headerBack() {
        int position = mTabLayout.getSelectedTabPosition();
        while (position > 0) {
            position--;
            int visibility = mTabLayout.getTabAt(position).getVisibility();
            if (visibility == View.VISIBLE) {
                setTabSelected(position);
                if (position == 0) {
                    setTabVisibility(1, View.GONE);
                    setTabVisibility(2, View.GONE);
                }
                if (position == 1) {
                    setTabVisibility(2, View.GONE);
                }
                return;
            }
        }
        finish();
    }

    public void setTabSelected(int position) {
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            if (i == position) {
                mTabLayout.setTabSelected(position);
                tvHeaderTitle.setText(getString(mTabTitleId[position]));
            }
        }
    }

    public void setTabSelectedById(int tabTitleId) {

        for (int i = 0; i < mTabTitleId.length; i++) {
            if (mTabTitleId[i] == tabTitleId) {
                mTabLayout.setTabSelected(i);
                tvHeaderTitle.setText(getString(mTabTitleId[i]));
            }
        }
    }

    public void setTabVisibility(int position, int visibility) {
        mTabLayout.getTabAt(position).setVisibility(visibility);
    }

    public void setTabVisibilityById(int tabTitleId, int visibility) {

        for (int i = 0; i < mTabTitleId.length; i++) {
            if (mTabTitleId[i] == tabTitleId) {
                mTabLayout.getTabAt(i).setVisibility(visibility);
            }
        }
    }

    public double getTip() {
        return mTip;
    }

    public void setTip(double tip) {
        this.mTip = tip;
    }

    public double getNeedPayAmout() {
        return mNeedPayAmoumt;
    }

    public void setNeedPayAmoumt(double needPayAmoumt) {
        this.mNeedPayAmoumt = needPayAmoumt;
    }

    public double getSubTotal() {
        return mSubTotal;
    }

    public void setSubTotal(double subTotal) {
        this.mSubTotal = subTotal;
    }

    public double getTax() {
        return mTax;
    }

    public void setTax(double tax) {
        this.mTax = tax;
    }

    public double getmSubSplitPay() {
        return mSubSplitPay;
    }

    public void setmSubSplitPay(double mSubSplitPay) {
        this.mSubSplitPay = mSubSplitPay;
    }
}
