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

import android.util.SparseIntArray;

import com.pax.order.entity.GoodsCategory;
import com.pax.order.entity.GoodsItem;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.ArrayList;

public interface GoodsDisplayContract {
    interface View extends IView<Presenter> {

        void initView(ArrayList<GoodsCategory> goodsCategoryList, ArrayList<GoodsItem> goodsItemList);

        void updateGroupSelected(ArrayList<GoodsCategory> goodsCategoryList, SparseIntArray groupSelect);

        void updateGoodList(String strTitle);

        void updateBottomView(int cartCount, double cartCost, int payCount);

        void startDetailsView(GoodsItem item);

        void startSettings();
    }

    interface Presenter extends IPresenter {
        void add(GoodsItem item);

        void minus(GoodsItem item);

        void toDetails(GoodsItem item);

        int toSettings();

        int getSelectedItemCountById(int itemId);

        void onTypeSelected(int position);

        void onResume();

        void upDateView();
    }
}
