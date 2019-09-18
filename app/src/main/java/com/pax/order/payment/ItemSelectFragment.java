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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.entity.StorageGoods;
import com.pax.order.payment.adapter.ItemSelectAdapter;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

import java.util.List;

public class ItemSelectFragment extends BaseFragment implements View.OnClickListener, ItemSelectContract.View {

    private ItemSelectPresenter mItemSelectPresenter;
    private Button mBtConfirmItem;
    private PaymentActivity mPaymentActivity;

    private RecyclerView mRvSelected;
    private ItemSelectAdapter mSelectAdapter;
    private RecyclerView mRvMyItem;
    private ItemSelectAdapter mMyItemAdapter;
    private ImageView mImgAddItem;
    private TextView mTvItemTip;
    private TextView mTvPerson;
    private TextView mTvTotal;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPaymentActivity = (PaymentActivity) mActivity;
        System.out.println("ItemSelectFragment onViewCreated");
        mItemSelectPresenter.start();
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_item_select;
    }

    @Override
    protected void initView(View view) {

        mRvSelected = (RecyclerView) view.findViewById(R.id.rvFullList);
        mRvMyItem = (RecyclerView) view.findViewById(R.id.rvMyItem);
        mBtConfirmItem = (Button) view.findViewById(R.id.btConfirmItem);
        mImgAddItem = (ImageView) view.findViewById(R.id.imgAddItem);
        mTvItemTip = (TextView) view.findViewById(R.id.tvItemTip);
        mTvPerson = (TextView) view.findViewById(R.id.tvPerson);
        mTvTotal = (TextView) view.findViewById(R.id.tvTotal);
    }

    @Override
    protected void bindEvent() {
        mBtConfirmItem.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        mItemSelectPresenter = new ItemSelectPresenter((ItemSelectContract.View) this);
        return mItemSelectPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btConfirmItem:
                mItemSelectPresenter.confirmItem();
                mPaymentActivity.setTabSelected(3);
                break;

            default:
                break;
        }
    }

    @Override
    public void initView(List<StorageGoods> selectList, List<StorageGoods> myItemList,
                         String strPerson, double total) {

        if ((0 == selectList.size()) && (0 == myItemList.size())) {
            mPaymentActivity.finish();
        }
        mRvSelected.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mSelectAdapter = new ItemSelectAdapter(this.getContext(), selectList, ItemSelectAdapter.ITEM_SELECT_UNPAID);
        mRvSelected.setAdapter(mSelectAdapter);
        //添加Android自带的分割线
        mRvSelected.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mSelectAdapter.setItemClickListener(new ItemSelectAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mItemSelectPresenter.addMyItem(position);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });

        // My Item
        mRvMyItem.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mMyItemAdapter = new ItemSelectAdapter(this.getContext(), myItemList, ItemSelectAdapter.ITEM_SELECT_PREPAID);
        mRvMyItem.setAdapter(mMyItemAdapter);
        //添加Android自带的分割线
        mRvMyItem.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
        mMyItemAdapter.setItemClickListener(new ItemSelectAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mItemSelectPresenter.minusMyItem(position);
            }

            @Override
            public void onLongItemClick(int position) {

            }
        });

        updateView(selectList, myItemList, strPerson, total);
    }

    @Override
    public void updateView(List<StorageGoods> selectList, List<StorageGoods> myItemList,
                           String strPerson, double total) {
        if (myItemList.size() > 0) {
            mRvMyItem.setVisibility(View.VISIBLE);
            mImgAddItem.setVisibility(View.GONE);
            mTvItemTip.setVisibility(View.GONE);
            mBtConfirmItem.setEnabled(true);
        } else {
            mRvMyItem.setVisibility(View.GONE);
            mImgAddItem.setVisibility(View.VISIBLE);
            mTvItemTip.setVisibility(View.VISIBLE);
            mBtConfirmItem.setEnabled(false);
        }

        mTvTotal.setText(AmountUtils.amountFormat(total));
        mTvPerson.setText(strPerson);

        mSelectAdapter.notifyDataSetChanged();
        mMyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ItemSelectContract.Presenter presenter) {

    }
}
