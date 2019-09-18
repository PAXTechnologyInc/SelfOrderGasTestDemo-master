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
 * 18-8-14 上午10:45           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.print;

import android.graphics.Bitmap;

public interface IReceiptGenerator {

    String TAG = "ReceiptGenerator";

    int FONT_BIG = 30;
    int FONT_NORMAL = 24;
    int FONT_SMALL = 20;
    String TYPE_FACE = Constants.FONT_PATH + Constants.FONT_NAME;

    /**
     * generate receipt
     *
     * @return
     */
    Bitmap generateBitmap();

    /**
     * generate simplified receipt string
     */
    String generateString();
}
