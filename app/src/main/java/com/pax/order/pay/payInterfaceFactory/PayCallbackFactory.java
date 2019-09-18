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
 * 18-9-29 上午9:30           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.pay.payInterfaceFactory;

import com.pax.order.pay.Pay;

public class PayCallbackFactory {
    private static PayCallbackFactory mPayCallbackFactory = null;
    private Pay.PayListener mPayCallback = null;

    private PayCallbackFactory() {
    }

    public static PayCallbackFactory getInstance() {
        if (mPayCallbackFactory == null) {
            mPayCallbackFactory = new PayCallbackFactory();
        }
        return mPayCallbackFactory;
    }

    public Pay.PayListener getPayCallback() {
        return mPayCallback;
    }

    public void setPayCallback(Pay.PayListener payCallback) {
        mPayCallback = payCallback;
    }
}
