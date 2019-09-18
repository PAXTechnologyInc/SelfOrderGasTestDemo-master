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
 * 2018/12/29 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.payment;

import com.pax.order.enums.SplitStep;
import com.pax.order.enums.SplitType;
import com.pax.order.pay.paydata.IPayResponse;
import com.pax.order.util.IPresenter;
import com.pax.order.util.IView;

import java.util.List;

public interface UseCardContract {
    interface View extends IView<Presenter> {

        void displayPayStatus(SplitType splitType, SplitStep splitStep);

        void startPay();

        void retryPay(int payCounter, String messageText);

        void startPrint(List<String> prnInfoList);

        void dealPayFailResult();

        void finishView();

        String getTotalAmt();

        String getTax();

        String getTip();

        String getUpLoadAmt();

        String getBaseAmt();



    }

    interface Presenter extends IPresenter {

        void dealPayFlow(IPayResponse payResponse);

        void setPayCounter(int payCounter);

    }
}
