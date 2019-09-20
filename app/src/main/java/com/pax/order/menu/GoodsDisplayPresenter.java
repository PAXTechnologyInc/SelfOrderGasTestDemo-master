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

import android.support.annotation.NonNull;
import android.util.SparseIntArray;

import com.pax.order.R;
import com.pax.order.entity.CartData;
import com.pax.order.entity.DownloadItemData;
import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsCategory;
import com.pax.order.entity.GoodsItem;
import com.pax.order.entity.PayData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.IView;
import com.pax.order.util.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class GoodsDisplayPresenter extends BasePresenter<IView> implements GoodsDisplayContract.Presenter {
    private static String TAG = "GoodsDisplayPresenter";
    private final GoodsDisplayContract.View mGoodsDisplayView;

    private ArrayList<GoodsItem> mGoodsItemList;
    private ArrayList<GoodsCategory> mGoodsCategoryList;
    private List<SelectGoods> mSelectedList = new ArrayList<SelectGoods>();
    private SparseIntArray mGroupSelect = new SparseIntArray();

    public GoodsDisplayPresenter(@NonNull GoodsDisplayContract.View goodsDisplayView) {
        mGoodsDisplayView = goodsDisplayView;
        mGoodsDisplayView.setPresenter(this);
    }

    @Override
    public void start() {
        mGoodsCategoryList = getGoodsCategoryList();
        if (null != mGoodsCategoryList && mGoodsCategoryList.size() > 0) {
            setGoodsItemListByTypeId(mGoodsCategoryList.get(0).getTypeId());
        }
        // 显示view页面
        mGoodsDisplayView.initView(mGoodsCategoryList, mGoodsItemList);
        toUpdateBottom();
    }

    //  get all商品信息列表
    private ArrayList<GoodsCategory> getGoodsCategoryList() {
//        List<GoodsCategory> categoryList = DownloadItemData.getInstance().getGoodsCategoryList();

        List<GoodsAttributes> emptyAttributes = new ArrayList<GoodsAttributes>();
        // cigarette
        GoodsItem itemOne = new GoodsItem(1, 1, "Marlboro", true, 4.50, 1, "Cigarette", "", R.drawable.marlboro_cigarette_brand, 1,  emptyAttributes);
        GoodsItem itemTwo = new GoodsItem(2, 1, "Nat Sherman", true, 9.99, 1, "Cigarette", "", R.drawable.nat_sherman_cigarette_brand, 1, emptyAttributes);
        GoodsItem itemThree = new GoodsItem(3, 1, "Davidoff", true, 4.80, 1, "Cigarette", "", R.drawable.davidoff_cigarettes, 1, emptyAttributes);
        GoodsItem item4 = new GoodsItem(4, 1, "Dunhill", true, 4.80, 1, "Cigarette", "", R.drawable.dunhill_cigarettes, 1, emptyAttributes);
        GoodsItem item5 = new GoodsItem(5, 1, "Parliament", true, 4.80, 1, "Cigarette", "", R.drawable.parliament_cigarettes, 1, emptyAttributes);
        GoodsItem item6 = new GoodsItem(6, 1, "Vogue", true, 4.80, 1, "Cigarette", "", R.drawable.vogue_cigarettes, 1, emptyAttributes);
        GoodsItem item7 = new GoodsItem(7, 1, "Sobranie", true, 4.80, 1, "Cigarette", "", R.drawable.sobranie_cigarettes, 1, emptyAttributes);
        GoodsItem item8 = new GoodsItem(8, 1, "Treasurer", true, 19.99, 1, "Cigarette", "", R.drawable.treasurer_london_cigarette_brand, 1, emptyAttributes);

        // food
        GoodsItem item9 = new GoodsItem(9, 1, "Burger", true, 4.50, 2, "Food", "", R.drawable.image_burger, 1,  emptyAttributes);
        GoodsItem item10 = new GoodsItem(10, 1, "Sandwich", true, 9.99, 2, "Food", "", R.drawable.image_sandwich, 1, emptyAttributes);
        GoodsItem item11 = new GoodsItem(11, 1, "Fruit Snack", true, 4.80, 2, "Food", "", R.drawable.image_fruit_snack, 1, emptyAttributes);
        GoodsItem item12 = new GoodsItem(12, 1, "Bites Snack", true, 4.80, 2, "Food", "", R.drawable.image_bites_snack, 1, emptyAttributes);

        // beverage
        GoodsItem item13 = new GoodsItem(13, 1, "Coca", true, 4.50, 3, "Beverage", "", R.drawable.image_coca_cola, 1,  emptyAttributes);
        GoodsItem item14 = new GoodsItem(14, 1, "Cracker", true, 9.99, 3, "Beverage", "", R.drawable.image_cracker, 1, emptyAttributes);
        GoodsItem item15 = new GoodsItem(15, 1, "Fanta", true, 4.80, 3, "Beverage", "", R.drawable.image_fanta, 1, emptyAttributes);
        GoodsItem item16 = new GoodsItem(16, 1, "Soda", true, 4.80, 3, "Beverage", "", R.drawable.image_soda, 1, emptyAttributes);
        GoodsItem item17 = new GoodsItem(17, 1, "Sprite", true, 4.80, 3, "Beverage", "", R.drawable.image_sprite, 1, emptyAttributes);


        List<GoodsItem> itemsOne = new ArrayList<GoodsItem>();
        itemsOne.add(itemOne);
        itemsOne.add(itemTwo);
        itemsOne.add(itemThree);
        itemsOne.add(item4);
        itemsOne.add(item5);
        itemsOne.add(item6);
        itemsOne.add(item7);
        itemsOne.add(item8);
        itemsOne.add(item9);
        itemsOne.add(item10);
        itemsOne.add(item11);
        itemsOne.add(item12);
        itemsOne.add(item13);
        itemsOne.add(item14);
        itemsOne.add(item15);
        itemsOne.add(item16);

        GoodsCategory categoryOne = new GoodsCategory(1, "Cigarette", 1, R.drawable.icon_cigarette, itemsOne);
        GoodsCategory categoryTwo = new GoodsCategory(2, "Food", 1, R.drawable.icon_food, itemsOne);
        GoodsCategory categoryThree = new GoodsCategory(3, "Beverage", 1, R.drawable.icon_beverage, itemsOne);

        List<GoodsCategory> categoryList = new ArrayList<GoodsCategory>();
        categoryList.add(categoryThree);
        categoryList.add(categoryTwo);
        categoryList.add(categoryOne);


        DownloadItemData.getInstance().setGoodsCategoryList(categoryList);

//        GoodsItem goodOne = new GoodsItem(1, -1, "GoodsItem1",true , 3.50, 1,"Category1", null, 0, null);
//        GoodsItem goodTwo = new GoodsItem(1, -1, "GoodsItem1",true , 3.50, 1,"Category1", null, 0, null);
//        GoodsItem goodThree = new GoodsItem(1, -1, "GoodsItem1",true , 3.50, 1,"Category1", null, 0, null);
//
//        List<GoodsItem> goodsList = Arrays.asList(goodOne,goodTwo,goodThree);

        // TODO:: Create GoodsCategory List stored locally.
//        GoodsCategory categoryOne = new GoodsCategory(1, "Category1", 2, null, goodsList);
//        List<GoodsCategory> categoryList = Arrays.asList(categoryOne);
        GoodsCategory category = null;

//        List<GoodsItem> goodsItemList = Tools.deepCopy(DownloadItemData.getInstance().getGoodsItemList());
        List<GoodsItem> goodsItemList = Tools.deepCopy(itemsOne);


        if (categoryList == null || goodsItemList == null) {
            return null;
        }

        for (int i = 0; i < categoryList.size(); i++) {
            category = categoryList.get(i);
            List<GoodsItem> goodsTtemListTmp = new ArrayList<>();
            for (GoodsItem goodsItem : goodsItemList) {
                if (goodsItem.getCategoryId() == category.getTypeId()) {
                    goodsTtemListTmp.add(goodsItem);
                }
            }
            category.setGoodsItemList(goodsTtemListTmp);
            // 分类下存在商品信息
        }
        mGoodsCategoryList = (ArrayList<GoodsCategory>) categoryList;

        return mGoodsCategoryList;
    }

    // 根据类别id获取分类商品信息列表
    private void setGoodsItemListByTypeId(int typeId) {
        if (mGoodsItemList == null) {
            mGoodsItemList = new ArrayList<>();
        }
        for (int i = 0; i < mGoodsCategoryList.size(); i++) {
            if (mGoodsCategoryList.get(i).getTypeId() == typeId) {
                mGoodsItemList.addAll(mGoodsCategoryList.get(i).getGoodsItemList());
                break;
            }
        }
        System.out.println("Goods Items Set");

    }

    @Override
    public void add(GoodsItem item) {
        int groupCount = mGroupSelect.get(item.getCategoryId());
        if (groupCount == 0) {
            mGroupSelect.append(item.getCategoryId(), 1);
        } else {
            mGroupSelect.append(item.getCategoryId(), ++groupCount);
        }

        boolean flag = false;
        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == item.getId() && selectGoods.getAttributeId().equals("")) {
                selectGoods.setQuantity(selectGoods.getQuantity() + 1);
                flag = true;
            }
        }
        if (!flag) {
            SelectGoods goods = new SelectGoods();
            goods.setId(item.getId());
            goods.setQuantity(1);
            goods.setPrice(item.getPrice());
            goods.setTaxAmt(item.getTaxAmount());
            goods.setName(item.getName());
            goods.setAttributeId("");
            goods.setAtrributeidName("");
            goods.setAttributeTaxAmt(0);
            goods.setCategoryId(item.getCategoryId());
            goods.setCategoryName(item.getCategoryName());
            mSelectedList.add(goods);
        }

        CartData.getInstance().setSelectedList(mSelectedList);
        CartData.getInstance().setGroupSelect(mGroupSelect);
        mGoodsDisplayView.updateGroupSelected(mGoodsCategoryList, mGroupSelect);
        toUpdateBottom();
    }

    @Override
    public void minus(GoodsItem item) {

        int groupCount = mGroupSelect.get(item.getCategoryId());
        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == item.getId() && selectGoods.getAttributeId().equals("")) {
                if (selectGoods.getQuantity() < 2) {
                    mSelectedList.remove(selectGoods);
                    if (groupCount == 1) {
                        mGroupSelect.delete(item.getCategoryId());
                    } else if (groupCount > 1) {
                        mGroupSelect.append(item.getCategoryId(), --groupCount);
                    }
                    break;
                } else {
                    selectGoods.setQuantity(selectGoods.getQuantity() - 1);
                    if (groupCount > 1) {
                        mGroupSelect.append(item.getCategoryId(), --groupCount);
                    }
                }
            }
        }

        CartData.getInstance().setSelectedList(mSelectedList);
        CartData.getInstance().setGroupSelect(mGroupSelect);
        mGoodsDisplayView.updateGroupSelected(mGoodsCategoryList, mGroupSelect);
        toUpdateBottom();
    }

    @Override
    public void toDetails(GoodsItem item) {
        CartData.getInstance().setSelectedList(mSelectedList);
        CartData.getInstance().setGroupSelect(mGroupSelect);
        mGoodsDisplayView.startDetailsView(item);
    }

    @Override
    public int toSettings() {
        mGoodsDisplayView.startSettings();
        return 0;
    }

    @Override
    public int getSelectedItemCountById(int itemId) {
        // 根据商品id获取当前商品(所有规格)的采购数量
        int count = 0;
        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == itemId) {
                count += selectGoods.getQuantity();
            }
        }
        return count;
    }

    @Override
    public void onTypeSelected(int position) {
        mGoodsItemList.clear();
        mGoodsItemList.addAll(mGoodsCategoryList.get(position).getGoodsItemList());

        // 刷新右侧商品栏
        mGoodsDisplayView.updateGoodList(mGoodsCategoryList.get(position).getTypeName());
    }

    @Override
    public void onResume() {
        mGoodsDisplayView.updateGroupSelected(mGoodsCategoryList, mGroupSelect);
        mGoodsDisplayView.updateGoodList("");
        toUpdateBottom();
    }

    // 刷新底栏
    private void toUpdateBottom() {
        int count = 0;
        double cost = 0;
        int payCount = 0;

        for (SelectGoods selectGoods : mSelectedList) {
            count += selectGoods.getQuantity();
            cost += selectGoods.getQuantity() * selectGoods.getPrice();
        }

        if (PayData.getInstance().isIfNoPaidTicket()) {
            payCount = 1;
        }

        mGoodsDisplayView.updateBottomView(count, cost, payCount);
    }

    @Override
    public void upDateView() {
        mGoodsDisplayView.updateGroupSelected(mGoodsCategoryList, mGroupSelect);
        mGoodsDisplayView.updateGoodList("");
    }
}
