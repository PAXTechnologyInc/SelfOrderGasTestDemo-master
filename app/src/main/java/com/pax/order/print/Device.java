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
 * 18-8-13 下午5:38           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.print;

import com.pax.gl.page.IPage;
import com.pax.order.FinancialApplication;

public class Device {
    public static IPage generatePage() {
        IPage page = FinancialApplication.getApp().getGlPage().createPage();
        page.adjustLineSpace(-9);
        page.setTypeFace(IReceiptGenerator.TYPE_FACE);
        return page;
    }
}
