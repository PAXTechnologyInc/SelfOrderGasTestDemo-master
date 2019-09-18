/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2018 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 18-9-13 下午8:25  	     JoeyTan           	    Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.orderserver.Impl;

import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.Transaction;
import com.pax.order.orderserver.entity.LoginInfo;
import com.pax.order.orderserver.entity.modifyTicket.ModifyItemReq;
import com.pax.order.orderserver.entity.modifyTicket.OpenItemReq;
import com.pax.order.orderserver.inf.OnComplete;

import java.util.List;




public final class OrderInstance {

    private AsyncPostApiImpl mAsyncPostApiImpl;

    private OrderInstance() {
        mAsyncPostApiImpl = new AsyncPostApiImpl();
    }

    public static OrderInstance getInstance() {
        return SingletonHolder.sInstance;
    }

    public int setCallback(OnComplete onComplete) {
        return mAsyncPostApiImpl.setCallback(onComplete);
    }

    public int asyncVerifyLoginInfo(String userName, String password, String token) {
        return mAsyncPostApiImpl.asyncVerifyLoginInfo(
                userName, password, token);
    }

    public int initLoginInfo(LoginInfo loginInfo) {
        return mAsyncPostApiImpl.initLoginInfo(loginInfo);
    }

    public int asyncOpenTicket(OpenTicket req) {
        return mAsyncPostApiImpl.asyncOpenTicket(req);
    }

    public int asyncUploadTrans(Transaction req, String serialNumber) {
        return mAsyncPostApiImpl.asyncUploadTrans(req, serialNumber);
    }

    public int asyncUploadMultiTrans(List<Transaction> req, String serialNumber) {
        return mAsyncPostApiImpl.asyncUploadMultiTrans(req, serialNumber);
    }

    public int asyncGetAllTableOrders(boolean isOnlyShowOpened, boolean isByTableID) {
        return mAsyncPostApiImpl.asyncGetAllTableOrders(isOnlyShowOpened, isByTableID);
    }

    public int asyncGetUnpaidOrders() {
        return mAsyncPostApiImpl.asyncGetUnpaidOrders();
    }

    public int asyncGetOrderAmount(String traceNum) {
        return mAsyncPostApiImpl.asyncGetOrderAmount(traceNum);
    }

    public int asyncGetOrderDetail(String traceNum) {
        return mAsyncPostApiImpl.asyncGetOrderDetail(traceNum);
    }

    public int asyncGetEmployee() {
        return mAsyncPostApiImpl.asyncGetEmployee();
    }

    public int asyncGetAllTableInfo() {
        return mAsyncPostApiImpl.asyncGetAllTableInfo();
    }

    public int asyncSendNotification(String sendType) {
        return mAsyncPostApiImpl.asyncSendNotification(sendType);
    }

    public int asyncModifyTicket(String TraceNum, String ModifyType, String discountAmt, String serialNumber,
                                 List<ModifyItemReq> modifyItemList, List<OpenItemReq> openItemList) {
        return mAsyncPostApiImpl.asyncModifyTicket(TraceNum, ModifyType,
                discountAmt, serialNumber, modifyItemList, openItemList);
    }

    public int asyncRegisterDevice() {
        return mAsyncPostApiImpl.asyncRegisterDevice();
    }

    public int asyncUnregisterDevice() {
        return mAsyncPostApiImpl.asyncUnregisterDevice();
    }

    public int asyncGetSettings(){
        return mAsyncPostApiImpl.asyncGetSettings();
    }

    public int asyncGetTable(){
        return mAsyncPostApiImpl.asyncGetTable();
    }

    public int asyncGetStoreInfo(){
        return mAsyncPostApiImpl.asyncGetStoreInfo();
    }

    private static class SingletonHolder {
        private static final OrderInstance sInstance = new OrderInstance();
    }
}
