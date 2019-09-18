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

import com.pax.order.FinancialApplication;
import com.pax.order.pay.Pay;

public class PayFactory {

    private PayFactory() {
    }


    public static Pay.Payment getPaymentInstance() {
        Pay.Payment payment = null;

        try {
            payment = (Pay.Payment) Class.forName(FinancialApplication.getPayModule()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return payment;

    }
}
