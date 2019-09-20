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

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pax.order.R;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.entity.GoodsCategory;
import com.pax.order.entity.GoodsItem;
import com.pax.order.menu.adapter.GoodsAdapter;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class GoodsDisplayFragment extends BaseFragment implements View.OnClickListener, GoodsDisplayContract.View {
    private static final String TAG = GoodsDisplayFragment.class.getSimpleName();

    public GoodsDisplayPresenter mGoodsDisplayPresenter;
    private MenuActivity mMenuActivity;

    private ImageView imgHeaderSetting,loading;
    private TextView tvHeaderTitle;
    private VerticalTabLayout mTabLayout;
    private RecyclerView mItemListView;
    private GoodsAdapter mGoodsAdapter;
    private GlobalVariable global_var;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        global_var = ((GlobalVariable) this.getApplicationContext());

        mMenuActivity = (MenuActivity) mActivity;
        System.out.println("GoodsDisplayFragment created");

        mGoodsDisplayPresenter.start();
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_goods_display;
    }

    @Override
    protected void initView(View view) {
        imgHeaderSetting = (ImageView) view.findViewById(R.id.menu_header_setting);
//        tvHeaderTitle = (TextView) view.findViewById(R.id.menu_header_title);
        mItemListView = (RecyclerView) view.findViewById(R.id.itemListView);
        mTabLayout = (VerticalTabLayout) view.findViewById(R.id.tab_layout);

        loading = (ImageView) view.findViewById(R.id.loading);
        // gif
        Glide.with(GoodsDisplayFragment.this).load(R.drawable.loading)
                .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(loading);
    }

    @Override
    protected void bindEvent() {
        imgHeaderSetting.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {
        mGoodsDisplayPresenter = new GoodsDisplayPresenter((GoodsDisplayContract.View) this);
        return mGoodsDisplayPresenter;
    }

    @Override
    public void setPresenter(GoodsDisplayContract.Presenter presenter) {
        mGoodsDisplayPresenter = (GoodsDisplayPresenter) presenter;
    }

    @Override
    public void initView(ArrayList<GoodsCategory> goodsCategoryList, ArrayList<GoodsItem> goodsItemList) {

        if (goodsCategoryList == null || goodsItemList == null) {
            return ;
        }

        // 右侧商品栏
        mGoodsAdapter = new GoodsAdapter(goodsItemList, mMenuActivity);
        mGoodsAdapter.setGoodsItemListener(new GoodsAdapter.onGoodsItemListener() {

            @Override
            public void onAddClick(GoodsItem item) {
                mGoodsDisplayPresenter.add(item);
            }

            @Override
            public void onMinusClick(GoodsItem item) {
                mGoodsDisplayPresenter.minus(item);
            }

            @Override
            public void onSpecClick(GoodsItem item) {
                mGoodsDisplayPresenter.toDetails(item);
            }

            @Override
            public void onImgClick(GoodsItem item) {
                mGoodsDisplayPresenter.add(item);
            }

            @Override
            public void playAnimation(int[] start_location) {
                mMenuActivity.playAnimation(start_location);
            }

            @Override
            public int getSelectedItemCountById(int itemId) {
                return mGoodsDisplayPresenter.getSelectedItemCountById(itemId);
            }
        });
        mItemListView.setAdapter(mGoodsAdapter);
        mItemListView.setLayoutManager(new GridLayoutManager(this.getContext(), 3));
        //mItemListView.addItemDecoration(new SpaceItemDecoration(0, 20));
        mItemListView.addItemDecoration(new GridDividerItemDecoration(getContext()));


        // 左侧分类栏
        mTabLayout.removeAllTabs();
        for (int i = 0; i < goodsCategoryList.size(); i++) {
            //最多显示8个字节
            StringBuilder sbTypeName = new StringBuilder(goodsCategoryList.get(i).getTypeName());
//            if(sbTypeName.length()>8){
//                sbTypeName.insert(7,'-');
//            }
            String typeName = sbTypeName.toString();
            mTabLayout.addTab(new QTabView(this.getContext())
//                    .setBadge(new TabView.TabBadge.Builder().setBadgeNumber(0)
//                            .setBackgroundColor(getResources().getColor(R.color.red))
//                            .setExactMode(true)
//                            .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
//                                @Override
//                                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
//                                    AppLog.d(TAG, "onDragStateChanged!!!" + dragState);
//                                    if(dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED){
//
//                                    }
//                                }
//                            })
//                            .build())
                    .setBackground(R.drawable.tab_selector)
                    .setTitle(new TabView.TabTitle.Builder()
                            .setTextColor(getResources().getColor(R.color.left_active_textcolor),
                                    getResources().getColor(R.color.left_normal_textcolor))
                            .setTextSize(16)
                            .setTextBold(true)
                            .setContent(typeName)
                            .build())
                    .setIcon(new TabView.TabIcon.Builder()
                            .setIcon(goodsCategoryList.get(i).getPicUrl(),
                                    goodsCategoryList.get(i).getPicUrl())
                            .setIconGravity(Gravity.TOP)
                            .setIconSize(72, 72)
                            .build()));
        }
        mTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                mGoodsDisplayPresenter.onTypeSelected(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });

        // set title
//        if (goodsCategoryList.size() > 0) {
//            tvHeaderTitle.setText(goodsCategoryList.get(0).getTypeName());
//        }
    }

    @Override
    public void updateGroupSelected(ArrayList<GoodsCategory> goodsCategoryList, SparseIntArray groupSelect) {

        // 刷新左侧栏小标
        for (int i = 0; i < goodsCategoryList.size(); i++) {
            mTabLayout.setTabBadge(i, groupSelect.get(goodsCategoryList.get(i).getTypeId()));
        }
    }

    @Override
    public void updateGoodList(String strTitle) {
        // 刷新右侧栏
        mGoodsAdapter.notifyDataSetChanged();

        //刷新Title
        if (!strTitle.equals("")) {
            mItemListView.scrollToPosition(0); // 回到顶部
//            tvHeaderTitle.setText(strTitle);
        }
    }

    @Override
    public void updateBottomView(int cartCount, double cartCost, int payCount) {
        // 刷新底栏
        mMenuActivity.updateBottomView(cartCount, cartCost, payCount);
    }

    @Override
    public void startDetailsView(GoodsItem item) {
        mMenuActivity.startDetailsView(item);
    }

    @Override
    public void startSettings() {
        mMenuActivity.startSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoodsDisplayPresenter.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {

        } else {
            mGoodsDisplayPresenter.onResume();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_header_setting:
                mGoodsDisplayPresenter.toSettings();
                break;

            default:
                break;
        }
    }

    private float getDensityDpi() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

}
