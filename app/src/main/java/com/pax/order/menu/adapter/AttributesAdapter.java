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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.entity.GoodsAttributes;

import java.util.List;


public class AttributesAdapter extends RecyclerView.Adapter<AttributesAdapter.ViewHolder> {
    private Context mContext;
    private List<GoodsAttributes> mDataList;
    private LayoutInflater mInflater;

    public AttributesAdapter(Context context, List<GoodsAttributes> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_attributes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsAttributes item = mDataList.get(position);
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
        private GoodsAttributes mItem;
        private TextView tvAttributes;

        ViewHolder(View itemView) {
            super(itemView);

            tvAttributes = (TextView) itemView.findViewById(R.id.tvAttributes);
            tvAttributes.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvAttributes:
                    mAttrItemListener.onAttrClick(getAdapterPosition());
                    break;

                default:
                    break;
            }
        }

        public void bindData(GoodsAttributes item) {
            this.mItem = item;
            if (item.getName() == null) {
                tvAttributes.setVisibility(View.GONE);
                return;
            }
            tvAttributes.setText(item.getName());

            String attrId = mAttrItemListener.getAttrId();
            if (item.getId().equals(attrId)) {
                //相同设置高亮
                tvAttributes.setBackground(mContext.getResources().getDrawable(R.drawable.layout_spec_selected_shape));
                tvAttributes.setTextColor(mContext.getResources().getColor(R.color.primary_black_color));

            } else {
                //不同设置
                tvAttributes.setBackground(mContext.getResources().getDrawable(R.drawable.layout_spec_unselected_shape));
                tvAttributes.setTextColor(mContext.getResources().getColor(R.color.left_normal_textcolor));
            }
        }
    }

    public interface onAttrItemListener {

        void onAttrClick(int position);

        String getAttrId();
    }

    private onAttrItemListener mAttrItemListener;

    public void setAttrItemListener(onAttrItemListener attrItemListener) {
        this.mAttrItemListener = attrItemListener;
    }
}
