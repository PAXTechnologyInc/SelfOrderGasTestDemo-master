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
package com.pax.order.payment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.entity.SelectGoods;
import com.pax.order.entity.StorageGoods;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.DoubleUtils;

import java.util.List;

public class CheckMenuAdapter extends RecyclerView.Adapter<CheckMenuAdapter.ViewHolder> {
    private List<StorageGoods> mDataList;
    private LayoutInflater mInflater;

    public CheckMenuAdapter(Context context, List<StorageGoods> mDataList) {
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StorageGoods item = mDataList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvCost, mTvCount, mTvName, mTvAttributes;
        private ImageView mImgAdd, mImgMinus;

        ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvCost = (TextView) itemView.findViewById(R.id.tvCost);
            mTvCount = (TextView) itemView.findViewById(R.id.count);
            mImgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            mImgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);
            mTvAttributes = (TextView) itemView.findViewById(R.id.tvAttributes);
            mImgMinus.setVisibility(View.GONE);
            mImgAdd.setVisibility(View.GONE);
        }


        public void bindData(StorageGoods item) {
//            this.mItem = item;
            mTvName.setText(item.getName());
            if (item.getQuantity() != 0 || item.getReviewReduceQuantity() != 0) {
                mTvCost.setText(AmountUtils.amountFormat(DoubleUtils.sum(DoubleUtils.mul(item.getQuantity(), item.getPrice()), item.getReviewTotalAmt())));
                mTvCount.setText(Integer.toString(item.getQuantity() + item.getReviewReduceQuantity()));
            } else {
                mTvCost.setText(AmountUtils.amountFormat(item.getPrice()));
                mTvCount.setText("*");
            }

            if (item.getAtrributeidName() == null || item.getAtrributeidName().equals("")) {
                mTvAttributes.setVisibility(View.GONE);
            } else {
                mTvAttributes.setText(item.getAtrributeidName());
                mTvAttributes.setVisibility(View.VISIBLE);
            }
        }
    }
}
