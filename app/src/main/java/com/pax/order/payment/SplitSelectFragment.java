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
import android.view.View;
import android.widget.LinearLayout;

import com.pax.order.R;
import com.pax.order.entity.PayData;
import com.pax.order.enums.SplitType;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

public class SplitSelectFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mLayoutSplit2;
    private LinearLayout mLayoutSplit3;
    private LinearLayout mLayoutSplit_item;
    private PaymentActivity mPaymentActivity;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPaymentActivity = (PaymentActivity) mActivity;
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_split_select;
    }

    @Override
    protected void initView(View view) {
        mLayoutSplit2 = (LinearLayout) view.findViewById(R.id.layoutSplit2);
        mLayoutSplit3 = (LinearLayout) view.findViewById(R.id.layoutSplit3);
        mLayoutSplit_item = (LinearLayout) view.findViewById(R.id.layoutSplitItem);
    }

    @Override
    protected void bindEvent() {
        mLayoutSplit2.setOnClickListener(this);
        mLayoutSplit3.setOnClickListener(this);
        mLayoutSplit_item.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layoutSplit2:
                PayData.getInstance().setIfSplit(true);
                PayData.getInstance().setSplitType(SplitType.TWO);
                mPaymentActivity.setTabSelected(3);
                break;

            case R.id.layoutSplit3:
                PayData.getInstance().setIfSplit(true);
                PayData.getInstance().setSplitType(SplitType.THREE);
                mPaymentActivity.setTabSelected(3);
                break;

            case R.id.layoutSplitItem:
                PayData.getInstance().setIfSplit(true);
                PayData.getInstance().setSplitType(SplitType.BYITEM);
                mPaymentActivity.setTabVisibility(2, View.VISIBLE);
                mPaymentActivity.setTabSelected(2);
                break;

            default:
                break;
        }
    }
}
