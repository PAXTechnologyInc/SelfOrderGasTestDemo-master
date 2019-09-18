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
 * 2018/8/8 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.SparseIntArray;

public class CartData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static CartData sInstance = null;

    private List<SelectGoods> mSelectedList = new ArrayList<>(); //购物车商品列表
    private SparseIntArray mGroupSelect = new SparseIntArray(); //购物车每个分类的数量

    /**
     * 购物车返回状态
     */
    public enum EOrderStatus {
        /**
         * 返回
         */
        BACK,
        /**
         * 已订单
         */
        ORDER,
    }

    public static CartData getInstance() {
        if (sInstance == null) {
            sInstance = new CartData();
        }
        return sInstance;
    }

    public List<SelectGoods> getSelectedList() {
        return mSelectedList;
    }

    public void setSelectedList(List<SelectGoods> selectedList) {

        this.mSelectedList = selectedList;
    }

    public SparseIntArray getGroupSelect() {
        return mGroupSelect;
    }

    public void setGroupSelect(SparseIntArray groupSelect) {
        this.mGroupSelect = groupSelect;
    }

    public void clearCartData() {
        mSelectedList = null;
        mGroupSelect = null;
    }
}
