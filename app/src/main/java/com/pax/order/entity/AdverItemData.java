/*
 *
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
 * 18-12-10 下午3:51           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.entity;

import java.util.List;

public class AdverItemData {
    private static List<Advertisement> mAdvertisementList;

    public static List<Advertisement> getAdvertisementList() {
        return mAdvertisementList;
    }

    public static void setAdvertisementList(List<Advertisement> advertisementList) {
        if ((advertisementList != null)
                && (advertisementList.size() != 0))
            mAdvertisementList = advertisementList;
    }
}
