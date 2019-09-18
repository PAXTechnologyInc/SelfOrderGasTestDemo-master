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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.entity.StorageGoods;
import com.pax.order.util.AmountUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemSelectAdapter extends RecyclerView.Adapter<ItemSelectAdapter.ViewHolder> {
    private List<StorageGoods> mDataList;
    private LayoutInflater mInflater;
    private Context mContext;
    private int mItemType;
    private Animation mAnimation;
    private Map<String, Boolean> mIsFrist;
    public static final int ITEM_SELECT_UNPAID = 1;
    public static final int ITEM_SELECT_PREPAID = 2;

    public ItemSelectAdapter(Context context, List<StorageGoods> mDataList, int itemType) {
        this.mContext = context;
        this.mDataList = mDataList;
        this.mItemType = itemType;
        mInflater = LayoutInflater.from(context);

        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.in_from_right);
        mIsFrist = new HashMap<String, Boolean>();
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

        // 如果是第一次加载该view，则使用动画
        if (mIsFrist.get(item.getId() + item.getAtrributeidName()) == null
                || mIsFrist.get(item.getId() + item.getAtrributeidName())) {

            holder.itemView.startAnimation(mAnimation);
            mIsFrist.put(item.getId() + item.getAtrributeidName(), false);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener,
            RecyclerView.OnLongClickListener {
        private TextView mTvCost, mTvCount, mTvName, mTvAttributes;
        private ImageView mImgAdd, mImgMinus;
        private LinearLayout mLayout;

        ViewHolder(View itemView) {
            super(itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.layoutItemSelected);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
            mTvCost = (TextView) itemView.findViewById(R.id.tvCost);
            mTvCount = (TextView) itemView.findViewById(R.id.count);
            mImgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            mImgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);
            mTvAttributes = (TextView) itemView.findViewById(R.id.tvAttributes);
            mImgMinus.setVisibility(View.GONE);
            mImgAdd.setVisibility(View.GONE);
            mLayout.setBackground(mContext.getResources().getDrawable(R.drawable.layout_water_selector));

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }


        public void bindData(StorageGoods item) {
//            this.mItem = item;
            mTvName.setText(item.getName());

            if (mItemType == ITEM_SELECT_UNPAID) {
//                mTvCost.setText(AmountUtils.amountFormat(item.getUnPaidQuantity() * item.getPrice()));
                mTvCost.setText(AmountUtils.amountFormat(item.getmUnpaidlAmt()));
                mTvCount.setText(String.valueOf(item.getUnPaidQuantity()));
            } else {
//                mTvCost.setText(AmountUtils.amountFormat(item.getPrePaidQuantity() * item.getPrice()));
                mTvCost.setText(AmountUtils.amountFormat(item.getmPrePaidAmt()));
                mTvCount.setText(String.valueOf(item.getPrePaidQuantity()));
            }

            if (item.getAtrributeidName() == null || item.getAtrributeidName().equals("")) {
                mTvAttributes.setVisibility(View.GONE);
            } else {
                mTvAttributes.setText(item.getAtrributeidName());
                mTvAttributes.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if (mItemType == ITEM_SELECT_UNPAID) {
                if (mDataList.get(getAdapterPosition()).getUnPaidQuantity() <= 1) {
                    mIsFrist.put(mDataList.get(getAdapterPosition()).getId()
                            + mDataList.get(getAdapterPosition()).getAtrributeidName(), true);
                }
            } else {
                if (mDataList.get(getAdapterPosition()).getPrePaidQuantity() <= 1) {
                    mIsFrist.put(mDataList.get(getAdapterPosition()).getId()
                            + mDataList.get(getAdapterPosition()).getAtrributeidName(), true);
                }
            }
            mItemClickListener.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mItemClickListener.onItemClick(getAdapterPosition());
            return true;
        }
    }


    public interface onItemClickListener {

        void onItemClick(int position);

        void onLongItemClick(int position);

    }

    private onItemClickListener mItemClickListener;

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
}
