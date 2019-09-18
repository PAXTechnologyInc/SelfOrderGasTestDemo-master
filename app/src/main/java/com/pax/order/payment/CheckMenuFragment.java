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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.entity.PayData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.payment.adapter.CheckMenuAdapter;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

import java.util.List;

public class CheckMenuFragment extends BaseFragment implements View.OnClickListener, CheckMenuContract.View {

    private CheckMenuPresenter mCheckMenuPresenter;
    private Button mBtPayTotal;
    private Button mTvPaySplit;
    //    private TextView mTvPaySplit;
    private TextView mTvCount;
    private TextView mTvCost;
    private PaymentActivity mPaymentActivity;
    private RecyclerView mRvSelected;
    private CheckMenuAdapter mSelectAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPaymentActivity = (PaymentActivity) mActivity;

        mCheckMenuPresenter.start();
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_check_menu;
    }

    @Override
    protected void initView(View view) {
        mBtPayTotal = (Button) view.findViewById(R.id.topay_total);
//        mTvPaySplit = (TextView) view.findViewById(R.id.topay_split);
        mTvPaySplit = (Button) view.findViewById(R.id.topay_split);
        mRvSelected = (RecyclerView) view.findViewById(R.id.selectRecyclerView);
        mTvCount = (TextView) view.findViewById(R.id.cart_sum);
        mTvCost = (TextView) view.findViewById(R.id.cart_total);
    }

    @Override
    protected void bindEvent() {
        mBtPayTotal.setOnClickListener(this);
        mTvPaySplit.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        mCheckMenuPresenter = new CheckMenuPresenter((CheckMenuContract.View) this);
        return mCheckMenuPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.topay_total:
                PayData.getInstance().setIfSplit(false);
                mPaymentActivity.setTabSelected(3);
                break;

            case R.id.topay_split:
                mCheckMenuPresenter.toPaySplit();
                break;

            default:
                break;
        }
    }

    @Override
    public void initView(List<StorageGoods> selectList, int count, double cost) {
        System.out.println("CheckMenuFragment initView");
        mTvPaySplit.setVisibility(View.GONE);
        if(count <= 0 && cost<= 0){
            mBtPayTotal.setVisibility(View.GONE);
            mTvPaySplit.setVisibility(View.GONE);
        }
        mRvSelected.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mSelectAdapter = new CheckMenuAdapter(this.getContext(), selectList);
        mRvSelected.setAdapter(mSelectAdapter);

        mTvCount.setText(String.valueOf(count));
        mTvCost.setText(AmountUtils.amountFormat(cost));

        mPaymentActivity.setSubTotal(cost);
    }

    @Override
    public void startSplitView(boolean allowSplit) {
        if (allowSplit) {
            mPaymentActivity.setTabVisibility(1, View.VISIBLE);
            mPaymentActivity.setTabSelected(1);
        } else {
            new MStatusDialog(mPaymentActivity).show(getString(R.string.not_allow_split), getResources().getDrawable(R.drawable.mn_icon_dialog_warn), 5000);
        }
    }

    @Override
    public void setPresenter(CheckMenuContract.Presenter presenter) {

    }
}
