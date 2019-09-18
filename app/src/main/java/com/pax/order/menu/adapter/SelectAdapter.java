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
package com.pax.order.menu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.entity.SelectGoods;
import com.pax.order.menu.ShoppingCartActivity;
import com.pax.order.util.AmountUtils;

import java.util.List;

public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {
    private ShoppingCartActivity mActivity;
    private List<SelectGoods> mDataList;
    private LayoutInflater mInflater;

    public SelectAdapter(ShoppingCartActivity mActivity, List<SelectGoods> mDataList) {
        this.mActivity = mActivity;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_goods, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SelectGoods item = mDataList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SelectGoods mItem;
        private TextView tvCost, tvCount, tvName, tvAttributes, tvUnderStock,unitPrice;
        private ImageView imgAdd, imgMinus,imgDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            imgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);
            tvAttributes = (TextView) itemView.findViewById(R.id.tvAttributes);
            tvUnderStock = (TextView) itemView.findViewById(R.id.underStockNum);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            unitPrice = (TextView) itemView.findViewById(R.id.unitPrice);
            imgDelete.setVisibility(View.VISIBLE);
			
            imgMinus.setOnClickListener(this);
            imgAdd.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imgAdd:
                    mActivity.mShoppingCartPresenter.add(mItem);
                    break;
                case R.id.imgMinus:
                    mActivity.mShoppingCartPresenter.remove(mItem);
                    break;
                case R.id.imgDelete:
                    mActivity.mShoppingCartPresenter.deleteItem(mItem);
                    break;
                default:
                    break;
            }
        }

        public void bindData(SelectGoods item) {
            this.mItem = item;
            if (item.isUnderStock()) {
                tvName.setTextColor(mActivity.getResources().getColor(R.color.pay_bg_color));
                tvCost.setTextColor(mActivity.getResources().getColor(R.color.pay_bg_color));
                tvCount.setTextColor(mActivity.getResources().getColor(R.color.pay_bg_color));
                if (item.getAtrributeidName() != null) {
                    tvAttributes.setTextColor(mActivity.getResources().getColor(R.color.pay_bg_color));
                }
                tvUnderStock.setVisibility(View.VISIBLE);
                tvUnderStock.setText("Stock:" + String.valueOf(item.getStockNum()));
            } else {
                tvName.setTextColor(mActivity.getResources().getColor(R.color.select_goods_textcolor));
                tvCost.setTextColor(mActivity.getResources().getColor(R.color.select_goods_textcolor));
                tvCount.setTextColor(mActivity.getResources().getColor(R.color.select_count_textcolor));
                if (item.getAtrributeidName() != null) {
                    tvAttributes.setTextColor(mActivity.getResources().getColor(R.color.select_attr_textcolor));
                }
                tvUnderStock.setVisibility(View.GONE);
            }
            tvName.setText(item.getName());
            tvCost.setText(AmountUtils.amountFormat(item.getQuantity() * item.getPrice()));
            tvCount.setText(String.valueOf(item.getQuantity()));
            unitPrice.setText("Unit Price:" + String.valueOf(item.getPrice()));

            if (item.getAtrributeidName() == null || item.getAtrributeidName().equals("")) {
                tvAttributes.setVisibility(View.GONE);
            } else {
                tvAttributes.setText(item.getAtrributeidName());
                tvAttributes.setVisibility(View.VISIBLE);
            }
        }
    }
}
