package com.pax.order.menu;

import com.pax.order.entity.GoodsItem;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

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
 * 2018/7/31 	                   	Create/Add/Modify/Delete
 * ============================================================================
 */
public interface MenuContract {
    interface View extends IView<Presenter> {

        void initView();

        void startDetailsView(GoodsItem item);

        void startCartView();

        void startPaymentView();

        void startSettingView();

        void updateBottomView(int cartCount, double cartCost, int payCount);
    }

    interface Presenter extends IPresenter {

        int toCart();

        int toPay();

        void toClearCart();

        void toPowerOffExce();

        void asyncTableOrdersData();

        void asyncMenuData();

        void asyncGetTable();

        void syncTableId();

        boolean checkTableNum();
    }
}
