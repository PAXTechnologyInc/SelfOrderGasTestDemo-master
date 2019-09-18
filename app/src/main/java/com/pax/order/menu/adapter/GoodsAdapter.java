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
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.entity.GoodsItem;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.AnimationUtils;
import com.pax.order.util.ImageUtils;

import java.util.ArrayList;

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.ItemViewHolder> {

    private ArrayList<GoodsItem> mDataList;
    private Context mContext;
    private LayoutInflater mInflater;
    private Animation mRotateAnimation;

    public GoodsAdapter(ArrayList<GoodsItem> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

        mRotateAnimation = android.view.animation.AnimationUtils.loadAnimation(mContext, R.anim.anim_circle_rotate);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        GoodsItem item = mDataList.get(position);
        holder.imageView.setTag(item.getPicUrl());
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        }
        return mDataList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name, price, tvCount, tvSpecCount;
        private GoodsItem mItem;
        private ImageView imageView, imgAdd, imgMinus;
        private RelativeLayout layoutSpec;
        private int mStock;

        ItemViewHolder(View itemView) {
            super(itemView);
            layoutSpec = (RelativeLayout) itemView.findViewById(R.id.layoutSpec);
            tvSpecCount = (TextView) itemView.findViewById(R.id.tvSpecCount);
            imageView = (ImageView) itemView.findViewById(R.id.img);
            name = (TextView) itemView.findViewById(R.id.tvName);
            price = (TextView) itemView.findViewById(R.id.tvPrice);
            tvCount = (TextView) itemView.findViewById(R.id.count);
            imgMinus = (ImageView) itemView.findViewById(R.id.imgMinus);
            imgAdd = (ImageView) itemView.findViewById(R.id.imgAdd);
            imgMinus.setOnClickListener(this);
            imgAdd.setOnClickListener(this);
            imageView.setOnClickListener(this);
            layoutSpec.setOnClickListener(this);
        }

        public void bindData(final GoodsItem item) {
            this.mItem = item;
            if (null == item) {
                return;
            }
            //            Bitmap bitmap = ImageUtils.getBitmapByFullPath(item.getPicLocalFullpath());
            //            if (null != bitmap) {
            //                imageView.setImageBitmap(ImageUtils.bimapRound(bitmap, 15));
            //            } else {
            //                imageView.setImageResource(R.mipmap.icon_default);
            //            }
            String itemPicUrl = item.getPicUrl();
            if ((itemPicUrl != null) && (itemPicUrl.length() != 0)) {
                Glide.with(mContext)
                        .load(itemPicUrl)
                        .asBitmap()
                        .error(R.mipmap.icon_default)
                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                if (item.getBitmap() != null) {
                                    imageView.setImageBitmap(ImageUtils.bimapRound(item.getBitmap(), 15));
                                } else {

                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                    if (imageView.getTag().equals(item.getPicUrl())) {
                                        imageView.setImageDrawable(placeholder);
                                    }

                                    // 开始动画
                                    FinancialApplication.getApp().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            imageView.startAnimation(mRotateAnimation);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                item.setBitmap(resource);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                if (imageView.getTag().equals(item.getPicUrl())) {
                                    imageView.setImageBitmap(ImageUtils.bimapRound(resource, 15));
                                }

                                // 结束动画
                                FinancialApplication.getApp().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.clearAnimation();
                                    }
                                });
                            }
                        });
            } else {
                imageView.setImageResource(item.getImageIcon());
            }

            int itemCount = mGoodsItemListener.getSelectedItemCountById(item.getId());
            name.setText(item.getName());
            price.setText(AmountUtils.amountFormat(item.getPrice()));


            mStock = item.getStock(); //检查库存
            Log.i("GoodsAdapter","NAME:"+item.getName()+"...get mStock:"+mStock);
            if(mStock <= 0 ){
                //将图片设置成灰色
                Log.i("!!!!!!!   GoodsAdapter","NAME:"+item.getName()+"...get mStock:"+mStock);
                ColorMatrix cm = new ColorMatrix();
                cm.setSaturation(0);
                ColorMatrixColorFilter grayColor = new ColorMatrixColorFilter(cm);
                imageView.setColorFilter(grayColor);
                layoutSpec.setVisibility(View.GONE);
                imgAdd.setVisibility(View.VISIBLE);
                imgAdd.setColorFilter(grayColor);
                imgMinus.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);
               // imgAdd.setVisibility(View.INVISIBLE);
                return;
            }else{
                imageView.setColorFilter(null);
                imgAdd.setColorFilter(null);
            }

            //规格>0
            if (item.getGoodsAttributesList().size() > 0) {
                layoutSpec.setVisibility(View.VISIBLE);
                imgMinus.setVisibility(View.GONE);
                tvCount.setVisibility(View.GONE);
                imgAdd.setVisibility(View.GONE);

                if (itemCount < 1) {
                    tvSpecCount.setVisibility(View.GONE);
                } else {
                    tvSpecCount.setVisibility(View.VISIBLE);
                    tvSpecCount.setText(String.valueOf(itemCount));
                }

            } else {
                layoutSpec.setVisibility(View.GONE);
                imgAdd.setVisibility(View.VISIBLE);

                if (itemCount < 1) {
                    tvCount.setVisibility(View.GONE);
                    imgMinus.setVisibility(View.GONE);
                } else {
                    tvCount.setVisibility(View.VISIBLE);
                    imgMinus.setVisibility(View.VISIBLE);
                    tvCount.setText(String.valueOf(itemCount));
                }
            }
        }

        @Override
        public void onClick(View v) {
            if(mStock <= 0)  return;
            switch (v.getId()) {
                case R.id.img:
                case R.id.imgAdd: {
                    int count = mGoodsItemListener.getSelectedItemCountById(mItem.getId());
                    if (count < 1) {
                        imgMinus.setAnimation(AnimationUtils.getShowAnimation());
                        imgMinus.setVisibility(View.VISIBLE);
                        tvCount.setVisibility(View.VISIBLE);
                    }
                    mGoodsItemListener.onAddClick(mItem);
                    count++;
                    tvCount.setText(String.valueOf(count));
                    int[] loc = new int[2];
                    v.getLocationInWindow(loc);
                    mGoodsItemListener.playAnimation(loc);
                }
                break;

                case R.id.imgMinus: {
                    int count = mGoodsItemListener.getSelectedItemCountById(mItem.getId());
                    if (count < 2) {
                        imgMinus.setAnimation(AnimationUtils.getHiddenAnimation());
                        imgMinus.setVisibility(View.GONE);
                        tvCount.setVisibility(View.GONE);
                    }
                    count--;
                    mGoodsItemListener.onMinusClick(mItem);
                    tvCount.setText(String.valueOf(count));

                }
                break;

                case R.id.layoutSpec:
                    mGoodsItemListener.onSpecClick(mItem);
                    break;

                default:
                    break;
            }
        }
    }

    public interface onGoodsItemListener {

        void onAddClick(GoodsItem item);

        void onMinusClick(GoodsItem item);

        void onSpecClick(GoodsItem item);

        void onImgClick(GoodsItem item);

        void playAnimation(int[] start_location);

        int getSelectedItemCountById(int itemId);
    }

    private onGoodsItemListener mGoodsItemListener;

    public void setGoodsItemListener(onGoodsItemListener goodsItemListener) {
        this.mGoodsItemListener = goodsItemListener;
    }
}
