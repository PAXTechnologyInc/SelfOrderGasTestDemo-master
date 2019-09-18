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
 * 18-10-9 上午10:23           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.entity;

import com.pax.order.orderserver.entity.getAllTableInfo.TableInfo;

import java.util.List;

public class ProcessMessage {
    private int messageCode;
    private List<ErrDataInOpenTicket> mErrDataInOpenTickets;
    private DownLoadProgress mDownLoadProgress;
    private RspMsg mRspMsg;
    private List<TableInfo> mTableInfoList;
    private List<TableOrderInfo> mTableOrderInfoList;
    private List<GoodsCategory> mGoodsCategoryList;
    private List<GoodsItem> mGoodsItemList;
    private List<Advertisement> mAdvertisementList;

    public int getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(int messageCode) {
        this.messageCode = messageCode;
    }

    public List<ErrDataInOpenTicket> getErrDataInOpenTickets() {
        return mErrDataInOpenTickets;
    }

    public void setErrDataInOpenTickets(List<ErrDataInOpenTicket> errDataInOpenTickets) {
        if ((errDataInOpenTickets != null) && (errDataInOpenTickets.size() != 0)) {
            mErrDataInOpenTickets = errDataInOpenTickets;
        }
    }

    public DownLoadProgress getDownLoadProgress() {
        return mDownLoadProgress;
    }

    public void setDownLoadProgress(DownLoadProgress downLoadProgress) {
        mDownLoadProgress = downLoadProgress;
    }

    public RspMsg getRspMsg() {
        return mRspMsg;
    }

    public void setRspMsg(RspMsg mRspMsg) {
        this.mRspMsg = mRspMsg;
    }

    public List<TableInfo> getTableInfoList() {
        return mTableInfoList;
    }

    public void setTableInfoList(List<TableInfo> tableInfoList) {
        this.mTableInfoList = tableInfoList;
    }

    public List<TableOrderInfo> getTableOrderInfoList() {
        return mTableOrderInfoList;
    }

    public void setTableOrderInfoList(List<TableOrderInfo> mTableOrderInfoList) {
        this.mTableOrderInfoList = mTableOrderInfoList;
    }

    public List<Advertisement> getAdvertisementList() {
        return mAdvertisementList;
    }

    public void setAdvertisementList(List<Advertisement> advertisementList) {
        mAdvertisementList = advertisementList;
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
}
