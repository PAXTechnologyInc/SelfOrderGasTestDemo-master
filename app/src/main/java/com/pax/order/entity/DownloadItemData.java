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
 * 18-11-6 下午2:40           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.entity;

import java.util.List;

public class DownloadItemData {
    private static DownloadItemData sInstance = new DownloadItemData();
    private List<GoodsCategory> mGoodsCategoryList;
    private List<GoodsItem> mGoodsItemList;

    private DownloadItemData() {
    }

    public List<GoodsCategory> getGoodsCategoryList() {
        return mGoodsCategoryList;
    }

    public void setGoodsCategoryList(List<GoodsCategory> goodsCategoryList) {
        mGoodsCategoryList = goodsCategoryList;
    }

    public List<GoodsItem> getGoodsItemList() {
        return mGoodsItemList;
    }

    public void setGoodsItemList(List<GoodsItem> goodsItemList) {
        mGoodsItemList = goodsItemList;
    }

    public static DownloadItemData getInstance() {
        return sInstance;
    }

    public GoodsItem findGoodsItemById(int goodItemId) {
        int i = 0;
        if ((mGoodsItemList != null) && (mGoodsItemList.size() != 0)) {
            for (GoodsItem goodsItem : mGoodsItemList) {
                i++;
                if (goodsItem.getId() == goodItemId) {
                    return goodsItem;
                }
                if (i == mGoodsItemList.size()) {
                    return null;
                }
            }
        }
        return null;
    }
}
