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

import com.pax.order.entity.CartData.EOrderStatus;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.SelectGoods;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface ShoppingCartContract {
    interface View extends IView<Presenter> {

        void initView(List<SelectGoods> goodstList);

        void updateView(List<SelectGoods> selectList);

        void finshView(EOrderStatus orderStatus);

        void startMessageView();
    }

    interface Presenter extends IPresenter {

        int add(SelectGoods goodsItem);

        int remove(SelectGoods goodsItem);

        int deleteItem(SelectGoods goodsItem);

        int back();

        int clear();

        OpenTicket order();

        int addOrder();
    }
}
