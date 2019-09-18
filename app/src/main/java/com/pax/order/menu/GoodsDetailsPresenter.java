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
package com.pax.order.menu;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseIntArray;

import com.pax.order.FinancialApplication;
import com.pax.order.entity.CartData;
import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsItem;
import com.pax.order.entity.PayData;
import com.pax.order.entity.SelectGoods;
import com.pax.order.orderserver.entity.getitem.Attribute;
import com.pax.order.orderserver.entity.getitem.AttributeValue;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.Constants;
import com.pax.order.util.IView;
import com.pax.order.util.ToastUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link }), retrieves the data and updates the
 * UI as required.
 */
public class GoodsDetailsPresenter extends BasePresenter<IView> implements GoodsDetailsContract.Presenter {
    private static String TAG = "GoodsDetailsPresenter";

    private final GoodsDetailsContract.View mGoodDetailsView;
    private GoodsItem mGoodsItem;
    private SparseIntArray mGroupSelect;
    private List<SelectGoods> mSelectedList = new ArrayList<>(); //购物车商品列表
    private String mAttrId = "";
    private String mAttrName = "";
    private int mAttrPosition = -1;
    private int mCount = 0;
    private double mPrice = 0;
    final static int MAX_ATTCATE_INDEX = 4;
    private List<String> attIdList = new ArrayList<>();//菜品属性Id表，用于匹配
    public GoodsDetailsPresenter(@NonNull GoodsDetailsContract.View goodDetailsView) {
        mGoodDetailsView = goodDetailsView;
        mGoodDetailsView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public int setGoodsItem(GoodsItem mGoodsItem) {
        this.mGoodsItem = mGoodsItem;
        return 0;
    }

    @Override
    public int add() {

        //分类++
        int groupCount = mGroupSelect.get(mGoodsItem.getCategoryId());
        mGroupSelect.append(mGoodsItem.getCategoryId(), ++groupCount);

        //商品数量++
        boolean flag = false;
        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == mGoodsItem.getId()) {
                if (selectGoods.getAttributeId().equals(mAttrId)) {
                    selectGoods.setQuantity(selectGoods.getQuantity() + 1);
                    selectGoods.setLastAttrId(mAttrId);
                    mCount = selectGoods.getQuantity();
                    mPrice = selectGoods.getPrice();
                    flag = true;
                    break;
                }
                selectGoods.setLastAttrId(mAttrId);
            }
        }

        if (!flag) {
            SelectGoods goods = new SelectGoods();
            goods.setId(mGoodsItem.getId());
            goods.setQuantity(1);
            goods.setPrice(mGoodsItem.getPrice() + getPirceAdjustMent());
            goods.setTaxAmt(mGoodsItem.getTaxAmount());
            goods.setName(mGoodsItem.getName());
            goods.setAttributeId(mAttrId);

            //如果出现mAttrName为空的情况，需要重新获取。
            if("".equals(mAttrName)){
                for(GoodsAttributes goodsAtt:mGoodsItem.getGoodsAttributesList()){
                    if(goodsAtt.getId().equals(mAttrId)){
                        goods.setAtrributeidName(goodsAtt.getName());
                        break;
                    }
                }
            }else {
                goods.setAtrributeidName(mAttrName);
            }
            for (GoodsAttributes attributes : mGoodsItem.getGoodsAttributesList()) {
                if (attributes.getId().equals(mAttrId)) {
                    goods.setAttributeTaxAmt(attributes.getTaxAdjustment());
                }
            }
            goods.setCategoryId(mGoodsItem.getCategoryId());
            goods.setCategoryName(mGoodsItem.getCategoryName());
            goods.setLastAttrId(mAttrId);
//            goods.setAttValueId(attIdList);
            mSelectedList.add(goods);

            mCount = goods.getQuantity();
            mPrice = goods.getPrice();
        }

        CartData.getInstance().setSelectedList(mSelectedList);
        CartData.getInstance().setGroupSelect(mGroupSelect);
        mGoodDetailsView.updateView(mPrice, mCount);
        toUpdateBottom();

        return 0;
    }

    @Override
    public int minus() {

        //商品数量--
        Iterator<SelectGoods> it = mSelectedList.iterator();
        while (it.hasNext()) {
            SelectGoods selectGoods = it.next();
            if (selectGoods.getId() == mGoodsItem.getId() /*&& selectGoods.getAttributeId().equals(mAttrId)*/) {

                if(selectGoods.getAttributeId().equals(mAttrId)) {
                    //分类--
                    int groupCount = mGroupSelect.get(mGoodsItem.getCategoryId());
                    if (groupCount == 1) {
                        mGroupSelect.delete(mGoodsItem.getCategoryId());
                    } else if (groupCount > 1) {
                        mGroupSelect.append(mGoodsItem.getCategoryId(), --groupCount);
                    }

                    if (selectGoods.getQuantity() < 2) {
                        it.remove();
                        mCount = 0;
                        mPrice = mGoodsItem.getPrice() + getPirceAdjustMent();
                    } else {
                        selectGoods.setQuantity(selectGoods.getQuantity() - 1);
                        selectGoods.setLastAttrId(mAttrId);
                        mCount = selectGoods.getQuantity();
                        mPrice = selectGoods.getPrice();
                    }
                }
                selectGoods.setLastAttrId(mAttrId);
            }
        }

        CartData.getInstance().setSelectedList(mSelectedList);
        CartData.getInstance().setGroupSelect(mGroupSelect);
        mGoodDetailsView.updateView(mPrice, mCount);
        toUpdateBottom();

        return 0;
    }



    @Override
    public int back() {
        mGoodDetailsView.finshView();
        return 0;
    }

    private String listToString(List<String> mStList){
        StringBuilder sb = new StringBuilder("");
        for (int i=0 ; i<mStList.size() ; i++) {
            if(mStList.get(i)!= null && !mStList.get(i).isEmpty())
                sb.append(mStList.get(i));
        }
        return sb.toString();
    }

    private boolean updateAttributes(int index,String st){
        List<String> selectIdList;
         List<GoodsAttributes>  goodsAttr;
        String attIdListStr;
        attIdList.set(index,st);
        attIdListStr = listToString(attIdList);
        Log.i(TAG,"Select attrCode:"+attIdListStr);
        if(null != attIdListStr && attIdListStr.isEmpty()){
            //SP后台没有定义不选属性值的属性项，所以这种情况下不去匹配配置的属性项。
            mAttrId = "";
            mAttrName = "";
            return true;
        }
        goodsAttr = mGoodsItem.getGoodsAttributesList();
        for (GoodsAttributes gt:goodsAttr) {
            selectIdList = gt.getAttributesIdList();
            if(selectIdList != null && selectIdList.size()>0){
                String selectIdListStr = listToString(selectIdList);
                if(attIdListStr.equals(selectIdListStr)){
                    mAttrId = gt.getId();
                    mAttrName = gt.getName();
                    Log.i(TAG,"FIND OK ID:"+mAttrId+"....name:"+mAttrName);
                    return true;
                }
            }
        }
        mAttrId = "";
        mAttrName = "";
        Log.i(TAG,"attributes  not EXIST!!!!!");
        return false;

    }
    @Override
    /***
     * id:  下拉框的序号
     * index：属性项，最大为4
     */

    public int taste(int id,int index) {
        String attrId = "";
        String attrName = "";


        Log.i(TAG,"taste Id :"+id+"...cate index:"+index);
        if (id < 0) {
            mAttrId = "";
            mAttrName = "";
            attrId = "";
            attrName = "";
        } else {
//            mAttrId = mGoodsItem.getGoodsAttributesList().get(id).getId();
//            mAttrName = mGoodsItem.getGoodsAttributesList().get(id).getName();
            attrId = mGoodsItem.getmAttCate().get(index).getAttributeValue().get(id).getID();
            attrName = mGoodsItem.getmAttCate().get(index).getAttributeValue().get(id).getName();
            Log.i(TAG,"attid:"+attrId+"....attName:"+attrName
                    +"...prive:"+mGoodsItem.getmAttCate().get(index).getAttributeValue().get(id).getPrice());
        }
        if(!updateAttributes(index,attrId)){
            FinancialApplication.getApp().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showMessage(FinancialApplication.getApp(), "Out of stock!");
                }
            });
        }
        Log.i(TAG,"AtrrID:"+mAttrId+"....AttrName:"+mAttrName);
        boolean flag = false;
        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == mGoodsItem.getId() && selectGoods.getAttributeId().equals(mAttrId)) {
                mCount = selectGoods.getQuantity();
                mPrice = selectGoods.getPrice();
                flag = true;
                break;
            }
        }
        if (!flag) {
            mCount = 0;
            mPrice = mGoodsItem.getPrice() + getPirceAdjustMent();
        }
        mGoodDetailsView.updateView(mPrice, mCount);
        return 0;
    }

    @Override
    public void onResume() {
        mGroupSelect = CartData.getInstance().getGroupSelect();
        mSelectedList = CartData.getInstance().getSelectedList();

        //显示view页面
        mGoodDetailsView.initView();

        if (mGoodsItem == null) {
            return;
        }

        // 先查找购物车是否存在goodsItem id最后一次选择的属性
        boolean flag = false;
        mAttrId = "";
        mAttrName = "";



        for (SelectGoods selectGoods : mSelectedList) {
            if (selectGoods.getId() == mGoodsItem.getId()) {
                mAttrId = selectGoods.getLastAttrId();
                flag = true;
                break;
            }
        }
        initAttributeIdList();
        if (!flag) {
            // 再查找属性列表是否存在为0的属性

//            int i = 0;
//            for (GoodsAttributes goodsAttributes : mGoodsItem.getGoodsAttributesList()) {
//                if(goodsAttributes.getAttributesIdList().isEmpty()){
//                    mAttrId = goodsAttributes.getId();
//                    mAttrName = goodsAttributes.getName();
//                    mAttrPosition = i;
//                    break;
//                }
//                i++;
//            }
        }

        // 查找对应价格和数量
        flag = false;
        for (SelectGoods selectGoods : mSelectedList) {
            //退出detail界面重新进入，属性都恢复默认属性。
            if (selectGoods.getId() == mGoodsItem.getId() && selectGoods.getAttributeId().equals(mAttrId)) {
                mCount = selectGoods.getQuantity();
                mPrice = selectGoods.getPrice();
                flag = true;
                break;
            }
        }
        if (!flag) {
            mCount = 0;
            mPrice = mGoodsItem.getPrice() + getPirceAdjustMent();
        }

        mGoodDetailsView.updateView(mPrice, mCount);
        mGoodDetailsView.setNiceSpinnerGone();
        Log.i(TAG,"attcate size:"+mGoodsItem.getmAttCate().size());
        for(int i = 0 ; i < mGoodsItem.getmAttCate().size() && i< MAX_ATTCATE_INDEX;i++) {
            Log.i(TAG,"attCate size:"+i);
            mGoodDetailsView.updateAttrView(mGoodsItem.getmAttCate().get(i), mGoodsItem.getInventoryMethod(),i);
        }
        toUpdateBottom();
    }

    private void initAttributeIdList(){
            attIdList.clear();
            for(int i=0 ; i<MAX_ATTCATE_INDEX ; i++){
                attIdList.add("");
            }
    }
    @Override
    public void setAttributeIdList(int index,String value){
        attIdList.set(index,value);
    }

    @Override
    public String getAttrId() {
        return mAttrId;
    }

    @Override
    public List<String> getAttIdList() {
        return attIdList;
    }



    // 刷新底栏
    private void toUpdateBottom() {

        int mCount = 0;
        double cost = 0;
        int payCount = 0;

        for (SelectGoods selectGoods : mSelectedList) {
            mCount += selectGoods.getQuantity();
            cost += selectGoods.getQuantity() * selectGoods.getPrice();
        }

        if (PayData.getInstance().isIfNoPaidTicket()) {
            payCount = 1;
        }

        mGoodDetailsView.updateBottomView(mCount, cost, payCount);
    }

    private double getPirceAdjustMent() {
        // 根据属性ID查找对应调整金额
        for (GoodsAttributes goodsAttributes : mGoodsItem.getGoodsAttributesList()) {
            if (goodsAttributes.getId().equals(mAttrId)) {
                return goodsAttributes.getPirceAdjustMent();
            }
        }
        return 0;
    }
}
