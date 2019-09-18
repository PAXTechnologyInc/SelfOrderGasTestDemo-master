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

import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface AddTipContract {
    interface View extends IView<Presenter> {
        void initView(List<String> strTip, double subTotal, double tax, double tip, double needPay);

        void updateView(double tip, double needPay);
    }

    interface Presenter extends IPresenter {
        void setTipSelected(int position);

        double getTax();

        double getTipSelected();

        double getNeendPay();

        double getSubPay();
    }
}
