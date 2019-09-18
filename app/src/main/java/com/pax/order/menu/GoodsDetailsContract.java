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

import com.pax.order.entity.GoodsAttributes;
import com.pax.order.entity.GoodsItem;
import com.pax.order.orderserver.entity.getitem.AttributeCategorie;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface GoodsDetailsContract {
    interface View extends IView<Presenter> {

        void initView();

        void updateView(double price, int count);

//        void updateAttrView(List<GoodsAttributes> goodsAttributes, String inventoryMethod);
        void updateAttrView(AttributeCategorie attributeCategorie,String inventoryMethod,int index);
        void finshView();

        void updateBottomView(int cartCount, double cartCost, int payCount);
        void setNiceSpinnerGone();
    }

    interface Presenter extends IPresenter {

        int setGoodsItem(GoodsItem goodsItem);

        int add();

        int minus();

        int back();

        int taste(int id,int index);

        void onResume();

        String getAttrId();

        List<String> getAttIdList();

        void setAttributeIdList(int index,String value);

    }
}
