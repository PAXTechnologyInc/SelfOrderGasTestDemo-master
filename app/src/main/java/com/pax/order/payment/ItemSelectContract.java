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
package com.pax.order.payment;

import com.pax.order.entity.StorageGoods;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface ItemSelectContract {
    interface View extends IView<Presenter> {

        void initView(List<StorageGoods> selectList, List<StorageGoods> myItemList,
                      String strPerson, double total);

        void updateView(List<StorageGoods> selectList, List<StorageGoods> myItemList,
                        String strPerson, double total);
    }

    interface Presenter extends IPresenter {
        void addMyItem(int index);

        void minusMyItem(int index);

        void confirmItem();
    }
}
