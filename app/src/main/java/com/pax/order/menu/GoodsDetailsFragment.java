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
package com.pax.order.menu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsItem;
import com.pax.order.menu.adapter.AttrListAdapter;
import com.pax.order.orderserver.entity.getitem.AttributeCategorie;
import com.pax.order.orderserver.entity.getitem.AttributeValue;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.Constants;
import com.pax.order.util.IView;

//import org.angmarch.views.NiceSpinner;
import org.w3c.dom.Text;
import com.pax.nsview.NiceSpinner;
import java.util.ArrayList;
import java.util.List;

public class GoodsDetailsFragment extends BaseFragment implements View.OnClickListener, GoodsDetailsContract.View {
    private static String TAG = "GoodsDetailsFragment";
    private GoodsDetailsPresenter mGoodsDetailsPresenter;
    private MenuActivity mMenuActivity;

    private ImageView headerBack;
    private LinearLayout layoutAdd, layoutAddDefault;
    private TextView name, tvPrice, tvCount, tvTasteOne,tvTasteTwo,tvTasteThree,tvTasteFour;
    private TextView tvDescription, tvShowMore;
    private ImageView imgAdd, imgMinus;
    private ImageView imageView;
    private boolean isShowDes = false;
    private ScrollView  scrollView;
    private NiceSpinner niceSpinnerOne,niceSpinnerTwo,niceSpinnerThree,niceSpinnerFour;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMenuActivity = (MenuActivity) mActivity;
        mGoodsDetailsPresenter.start();
    }

    @Override
    protected int getViewID() {
        //return R.layout.fragment_goods_details;
       // return R.layout.frament_goods_details_sroll;
        return R.layout.fragment_goods_details_new;
//        return R.layout.activity_goods_details;
    }

    @Override
    protected void initView(View view) {
        headerBack = (ImageView) view.findViewById(R.id.header_back);
        imageView = (ImageView) view.findViewById(R.id.img_details);
        name = (TextView) view.findViewById(R.id.tvName_details);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvShowMore = (TextView) view.findViewById(R.id.tvShowMore);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice_details);
        tvCount = (TextView) view.findViewById(R.id.count_details);
        imgMinus = (ImageView) view.findViewById(R.id.tvMinus_details);
        imgAdd = (ImageView) view.findViewById(R.id.tvAdd_details);
        layoutAdd = (LinearLayout) view.findViewById(R.id.layoutAdd_details);
        layoutAddDefault = (LinearLayout) view.findViewById(R.id.layoutAdd_details_default);
        scrollView = (ScrollView) view.findViewById(R.id.scrollVw);


        tvTasteOne = (TextView) view.findViewById(R.id.tvAttributesOne);
        niceSpinnerOne = (NiceSpinner) view.findViewById(R.id.nsAttributesOne);


        tvTasteTwo = (TextView) view.findViewById(R.id.tvAttributesTwo);
        niceSpinnerTwo = (NiceSpinner) view.findViewById(R.id.nsAttributesTwo);

        tvTasteThree = (TextView) view.findViewById(R.id.tvAttributesThree);
        niceSpinnerThree = (NiceSpinner) view.findViewById(R.id.nsAttributesThree);

        tvTasteFour = (TextView) view.findViewById(R.id.tvAttributesFour);
        niceSpinnerFour = (NiceSpinner) view.findViewById(R.id.nsAttributesFour);
    }

    @Override
    protected void bindEvent() {
        headerBack.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        imageView.setOnClickListener(this);
        layoutAddDefault.setOnClickListener(this);
        tvShowMore.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        mGoodsDetailsPresenter = new GoodsDetailsPresenter((GoodsDetailsContract.View) this);
        return mGoodsDetailsPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAdd_details:
            case R.id.layoutAdd_details_default:

                int[] loc = new int[2];
                v.getLocationInWindow(loc);
                mMenuActivity.playAnimation(loc);
                mGoodsDetailsPresenter.add();
                break;
            case R.id.img_details:
                System.out.println("Thisi is image");
                mGoodsDetailsPresenter.add();
            case R.id.tvMinus_details:
                mGoodsDetailsPresenter.minus();
                break;

            case R.id.header_back:
                mGoodsDetailsPresenter.back();
                break;

            case R.id.tvShowMore:
                if (isShowDes) {
                    tvDescription.setEllipsize(TextUtils.TruncateAt.END);//收起
                    tvDescription.setMaxLines(5);
                    tvShowMore.setText(R.string.more);
                    FinancialApplication.getApp().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvDescription.scrollTo(0, 0);
                            tvDescription.setMovementMethod(null);
                        }
                    });
                } else {
                    tvDescription.setEllipsize(null);//展开
                    tvDescription.setSingleLine(false);//这个方法是必须设置的，否则无法展开
//                    tvDescription.setMaxLines(12);
                    tvShowMore.setText(R.string.collapse);
//                    FinancialApplication.getApp().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (tvDescription.getLineCount() > 12) {
//                                tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
//                            }
//                        }
//                    });
                }
                isShowDes = !isShowDes;
                break;

            default:
                break;
        }
    }


    @Override
    public void initView() {
        //获取商品信息
        final GoodsItem goodsItem = mMenuActivity.getmItemDetails();
        if (goodsItem == null) {
            return;
        }
        //scroll控件置顶
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        mGoodsDetailsPresenter.setGoodsItem(goodsItem);

        Bitmap bitmap = goodsItem.getBitmap();
        if (null != bitmap) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.icon_default);
            if ((goodsItem.getPicUrl() != null) && (goodsItem.getPicUrl().length() != 0)) {
                Glide.with(this).load(goodsItem.getPicUrl()).asBitmap().priority(Priority.IMMEDIATE)
                        .placeholder(R.mipmap.icon_default).error(R.mipmap.icon_default).centerCrop()
                        .dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        goodsItem.setBitmap(resource);
                    }
                });
            }
        }

        name.setText(goodsItem.getName());

        tvDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvDescription.setText("");
        isShowDes = false;
        if (goodsItem.getDescription() == null) {
            tvDescription.setVisibility(View.GONE);
            tvShowMore.setVisibility(View.GONE);
        } else {
            if (goodsItem.getDescription().equals("")) {
                tvDescription.setVisibility(View.GONE);
                tvShowMore.setVisibility(View.GONE);
            } else {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(goodsItem.getDescription());
                tvDescription.setEllipsize(TextUtils.TruncateAt.END);//收起
                tvDescription.setMaxLines(5);
                tvShowMore.setText(R.string.more);
            }
        }
        // 获得textview显示行数不要在设置值后直接获取，可能会得到0，因为内容还没完全加载
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvDescription.getLineCount() <= 5) {
                    tvShowMore.setVisibility(View.GONE);
                } else {
                    tvShowMore.setVisibility(View.VISIBLE);
                }

                tvDescription.scrollTo(0, 0);
                tvDescription.setMovementMethod(null);
            }
        });
    }

    @Override
    public void updateView(double price, int count) {
        tvCount.setText(String.valueOf(count));
        tvPrice.setText(AmountUtils.amountFormat(price));

        if (count > 0) {
            layoutAdd.setVisibility(View.VISIBLE);
            layoutAddDefault.setVisibility(View.GONE);
        } else {
            layoutAdd.setVisibility(View.GONE);
            layoutAddDefault.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void updateAttrView(AttributeCategorie goodsAttributes, final String inventoryMethod, final int index) {
        NiceSpinner niceSpinner;
        TextView tvTaste;
        NiceSpinner[] nsArray = new NiceSpinner[]{niceSpinnerOne, niceSpinnerTwo, niceSpinnerThree, niceSpinnerFour};
        TextView[] tvArray = new TextView[]{tvTasteOne, tvTasteTwo, tvTasteThree, tvTasteFour};


        if(index > GoodsDetailsPresenter.MAX_ATTCATE_INDEX){
            Log.e(TAG,"updateAttrView INDEX ERROR!!!!!");
            return ;
        }
        niceSpinner = nsArray[index];
        tvTaste = tvArray[index];
        if (goodsAttributes != null) {
            tvTaste.setVisibility(View.VISIBLE);
            niceSpinner.setVisibility(View.VISIBLE);
            tvTaste.setText(goodsAttributes.getType());

            final List<AttributeValue> attributeValue = new ArrayList<>();

            AttributeValue goodsAttr = new AttributeValue();
            goodsAttr.setID("");
            goodsAttr.setName("Normal");
            attributeValue.add(goodsAttr);
            attributeValue.addAll(goodsAttributes.getAttributeValue());

//            if (inventoryMethod != null && inventoryMethod.equals(Constants.TRACK_INVENTORY_BY_ATTR)) {
//                attributeValue.addAll(goodsAttributes.getAttributeValue());
//            } else {
//                AttributeValue goodsAttr = new AttributeValue();
//                goodsAttr.setID("");
//                goodsAttr.setName("Normal");
//                attributeValue.add(goodsAttr);
//                attributeValue.addAll(goodsAttributes.getAttributeValue());
//            }



            ListAdapter listAdapter = new AttrListAdapter(this.getContext(),attributeValue/* goodsAttributes.getAttributeValue()*/);
            niceSpinner.setAdapter(listAdapter);
            niceSpinner.setTextSize(12);
            niceSpinner.setBackgroundResource(R.drawable.tv_round_border);
            niceSpinner.setTextColor(getResources().getColor(R.color.primary_black_color));
            niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mGoodsDetailsPresenter.taste(position-1,index);
//                    if (inventoryMethod != null && inventoryMethod.equals(Constants.TRACK_INVENTORY_BY_ATTR)) {
//                        mGoodsDetailsPresenter.taste(position,index);
//                    } else {
//                        mGoodsDetailsPresenter.taste(position-1,index);
//                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

/**
//            int setIndex = mGoodsDetailsPresenter.getAttIdList().get(index);
            List<String> attIdList = mGoodsDetailsPresenter.getAttIdList();
            String attributeValueId = attIdList.get(index);
            if(attributeValueId.isEmpty()){
                niceSpinner.setSelectedIndex(0);
            }else {
                for (int i = 0; i < goodsAttributes.getAttributeValue().size(); i++) {
                    if (attributeValueId.equals(goodsAttributes.getAttributeValue().get(i).getID())){
                        niceSpinner.setSelectedIndex(i+1);
                        break;
                    }
                }
            }
//            niceSpinner.setSelectedIndex();
 */
            String attrId = mGoodsDetailsPresenter.getAttrId();
            final GoodsItem goodsItem = mMenuActivity.getmItemDetails();
            if(!("".equals(attrId))) {
                for (GoodsAttributes goodsAt : goodsItem.getGoodsAttributesList()) {
                    if (goodsAt.getId().equals(attrId)) {
                        List<AttributeValue> attriLst = goodsAt.getAttributeValue();
                        for(AttributeValue attriValue : attriLst){
                            if(goodsAttributes.getType().equals(attriValue.getType())){
                                int k = 0;
                               for(AttributeValue mAtt:goodsAttributes.getAttributeValue()){
                                   if(attriValue.getID().equals(mAtt.getID())){
                                       niceSpinner.setSelectedIndex(k+1);
                                       mGoodsDetailsPresenter.setAttributeIdList(index,mAtt.getID());
                                       break;
                                   }
                                   k++;
                               }
                               break;
                            }
                        }
                        break;
                    }
                }
            }


            // 设置默认属性
//            for (int i = 0; i < goodsAttributesList.size(); i++) {
//                if (goodsAttributesList.get(i).getId().equals(mGoodsDetailsPresenter.getAttrId())) {
//                    niceSpinner.setSelectedIndex(i);
//                    break;
//                }
//            }

        } else {
        //    tvTaste.setVisibility(View.GONE);
            niceSpinner.setVisibility(View.GONE);
        }
    }
    @Override
     public void setNiceSpinnerGone(){
        niceSpinnerOne.setVisibility(View.INVISIBLE);
        niceSpinnerTwo.setVisibility(View.INVISIBLE);
        niceSpinnerThree.setVisibility(View.INVISIBLE);
        niceSpinnerFour.setVisibility(View.INVISIBLE);

        tvTasteOne.setVisibility(View.INVISIBLE);
        tvTasteTwo.setVisibility(View.INVISIBLE);
        tvTasteThree.setVisibility(View.INVISIBLE);
        tvTasteFour.setVisibility(View.INVISIBLE);


    }


    @Override
    public void finshView() {
        mMenuActivity.finishDetailsView();
    }

    @Override
    public void updateBottomView(int cartCount, double cartCost, int payCount) {
        // 刷新底栏
        mMenuActivity.updateBottomView(cartCount, cartCost, payCount);
    }

    @Override
    public void setPresenter(GoodsDetailsContract.Presenter presenter) {
        mGoodsDetailsPresenter = (GoodsDetailsPresenter) presenter;
    }


    @Override
    public void onResume() {
        super.onResume();
        mGoodsDetailsPresenter.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {
            mGoodsDetailsPresenter.onResume();
        }
    }

}
