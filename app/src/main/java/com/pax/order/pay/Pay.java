/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 18-8-4 下午4:24           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.pax.order.pay.paydata.IBaseResponse;
import com.pax.order.pay.paydata.RequestVar;

public interface Pay {

    interface Payment {
        /**
         * this method uesd to call payment
         *
         * @param context:Context  start from this
         * @param requestVar:trans paras need to fill
         * @return true:trans succ  false -- trans fail
         */
        boolean doPayment(Context context, RequestVar requestVar);

        void doCommSetting(Context context, boolean isUSB);

        void abortTrans();

        boolean doBatch(Context context);

        void setPayListener(PayListener listener);

        void setFragmentMannager(FragmentManager fragmentMannager);
    }

    interface PayListener {
        /**
         * this callback method is used to deal trans result
         *
         * @param responseVar:the Trans result object
         * @return none
         */
        void onResult(IBaseResponse responseVar);
    }
}

