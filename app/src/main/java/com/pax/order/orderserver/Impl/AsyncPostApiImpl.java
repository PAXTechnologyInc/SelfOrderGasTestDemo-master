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

import android.util.Log;

import com.pax.order.FinancialApplication;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.entity.Transaction;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.retrofit.HttpServiceManager;
import com.pax.order.orderserver.Impl.retrofit.OnSuccessAndFaultSub;
import com.pax.order.orderserver.Impl.retrofit.api.HttpService;
import com.pax.order.orderserver.entity.LoginInfo;
import com.pax.order.orderserver.entity.OrderResult;
import com.pax.order.orderserver.entity.baseModel.BaseReqModel;
import com.pax.order.orderserver.entity.baseModel.BaseRspModel;
import com.pax.order.orderserver.entity.getAllTableInfo.GetAllTableInfoReq;
import com.pax.order.orderserver.entity.getAllTableInfo.GetAllTableInfoRsp;
import com.pax.order.orderserver.entity.getAllTableInfo.TableInfo;
import com.pax.order.orderserver.entity.getEmployee.Employee;
import com.pax.order.orderserver.entity.getEmployee.GetEmployeeReq;
import com.pax.order.orderserver.entity.getEmployee.GetEmployeeRsp;
import com.pax.order.orderserver.entity.getadvertisement.GetAdvertisementReq;
import com.pax.order.orderserver.entity.getadvertisement.GetAdvertisementRsp;
import com.pax.order.orderserver.entity.getcategory.GetCategoryReq;
import com.pax.order.orderserver.entity.getcategory.GetCategoryRsp;
import com.pax.order.orderserver.entity.getitem.GetItemReq;
import com.pax.order.orderserver.entity.getitem.GetItemRsp;
import com.pax.order.orderserver.entity.getorderdetail.GetOrderDetailReq;
import com.pax.order.orderserver.entity.getorderdetail.GetOrderDetailRsp;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoReq;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;
import com.pax.order.orderserver.entity.gettable.GetTableReq;
import com.pax.order.orderserver.entity.gettable.GetTableRsp;
import com.pax.order.orderserver.entity.gettableorders.GetTableOrdersReq;
import com.pax.order.orderserver.entity.gettableorders.GetTableOrdersRsp;
import com.pax.order.orderserver.entity.modifyTicket.ModifyItemReq;
import com.pax.order.orderserver.entity.modifyTicket.ModifyTicketReq;
import com.pax.order.orderserver.entity.modifyTicket.ModifyTicketRsp;
import com.pax.order.orderserver.entity.modifyTicket.OpenItemReq;
import com.pax.order.orderserver.entity.openticket.ItemInTicket;
import com.pax.order.orderserver.entity.openticket.OpenTicketReq;
import com.pax.order.orderserver.entity.openticket.OpenTicketRsp;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterReq;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterRsp;
import com.pax.order.orderserver.entity.registerDevice.RegisterInfoInstance;
import com.pax.order.orderserver.entity.sendnotification.SendNotificationReq;
import com.pax.order.orderserver.entity.sendnotification.SendNotificationRsp;
import com.pax.order.orderserver.entity.setting.GetSettingReq;
import com.pax.order.orderserver.entity.setting.GetSettingRsp;
import com.pax.order.orderserver.entity.uploadmultitrans.Trans;
import com.pax.order.orderserver.entity.uploadmultitrans.UploadMultiTransReq;
import com.pax.order.orderserver.entity.uploadmultitrans.UploadMultiTransRsp;
import com.pax.order.orderserver.entity.uploadtrans.UploadTransReq;
import com.pax.order.orderserver.entity.uploadtrans.UploadTransRsp;
import com.pax.order.orderserver.inf.AsyncPostApi;
import com.pax.order.orderserver.inf.OnComplete;
import com.pax.order.util.SecurityUtils;
import com.pax.order.util.Tools;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import io.reactivex.Observable;

import static com.pax.order.orderserver.constant.spRespCode.TM_INTERNAL_ERR;
import static com.pax.order.orderserver.constant.spRespCode.TM_NULL_RESPONSE;

/**
 * Async post retrofit class
 */
public class AsyncPostApiImpl implements AsyncPostApi {
    private static final String TAG = "AsyncPostApiImpl";

    private OnComplete mOnComplete;
    private LoginInfo mLoginInfo;
    private OrderResult mOrderResult;
    private String deviceId ;
    private String tokenId;
    private String timeStamp ;
    private String encryptKey;

    public AsyncPostApiImpl() {
        this.mLoginInfo = new LoginInfo("PAXTestTerminal01", "PAXTestTerminal01", "PAXTestTerminal01",
                "PAXTestTerminal01", "PAXTestTerminal01", "PAXTestTable01");

        this.mOrderResult = new OrderResult(TM_INTERNAL_ERR, "Internal error.");
    }

    public int setCallback(OnComplete callback) {
        this.mOnComplete = callback;
        return 0;
    }

    private HttpService orderInterface() {
        return HttpServiceManager.getInstance().getHttpService();
    }

    @Override
    public int asyncVerifyLoginInfo(String userName, String password, String token) {

        if (null == userName || null == password || null == token) {
            mOrderResult.setResultCode(TM_NULL_RESPONSE);
            mOrderResult.setResultMessage("Response is null.");
            mOnComplete.onVerifyLoginInfoComplete(mOrderResult);
        } else {
            GetAdvertisementReq mVerifyLoginInfoReq = new GetAdvertisementReq();
            mVerifyLoginInfoReq.setUserName(userName);
            mVerifyLoginInfoReq.setPassword(password);
            mVerifyLoginInfoReq.setToken(token);

            Observable<GetAdvertisementRsp> observable = orderInterface().post_GetAdvertisement(mVerifyLoginInfoReq);
            HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetAdvertisementRsp>() {
                @Override
                public void onSuccess(GetAdvertisementRsp rsp) {
                    mOrderResult.setResultCode(null == rsp.getResultCode() ? "" : rsp.getResultCode());
                    mOrderResult.setResultMessage(null == rsp.getResultMessage() ? "" : rsp.getResultMessage());
                    mOnComplete.onVerifyLoginInfoComplete(mOrderResult);
                }

                @Override
                public void onFault(String code, String msg) {
                    mOrderResult.setResultCode(code);
                    mOrderResult.setResultMessage(msg);
                    mOnComplete.onVerifyLoginInfoComplete(mOrderResult);
                }
            });
        }

        return 0;
    }

    @Override
    public int initLoginInfo(LoginInfo loginInfo) {
        this.mLoginInfo = loginInfo;
        return 0;
    }


    @Override
    public int asyncGetAdvertisement() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        GetAdvertisementReq mGetAdvertisementReq = new GetAdvertisementReq();
        mGetAdvertisementReq.setUserName(this.mLoginInfo.getUserName());
        mGetAdvertisementReq.setPassword(this.mLoginInfo.getPassword());
        fillRegisterInfo(mGetAdvertisementReq);




        Observable<GetAdvertisementRsp> observable = orderInterface().post_GetAdvertisement(mGetAdvertisementReq);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetAdvertisementRsp>() {
            @Override
            public void onSuccess(GetAdvertisementRsp rsp) {
                Log.i(TAG,"on succ ..code:"+rsp.getResultCode()+"...msg:"+rsp.getResultMessage());
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null.");
                    mOnComplete.onGetAdvertisementComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(null == rsp.getResultCode() ? "" : rsp.getResultCode());
                    mOrderResult.setResultMessage(null == rsp.getResultMessage() ? "" : rsp.getResultMessage());

                    if (null != rsp.getSpAdvertisements()) {
                        mOnComplete.onGetAdvertisementComplete(mOrderResult, rsp.getSpAdvertisements());
                    } else {
                        mOnComplete.onGetAdvertisementComplete(mOrderResult, null);
                    }
                }

            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetAdvertisementComplete(mOrderResult, null);
                Log.i(TAG,"code:"+code+"..errmsg"+msg);
            }
        });

        return 0;
    }

    @Override
    public int asyncGetItem() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        GetItemReq req = new GetItemReq();
        req.setUserName(this.mLoginInfo.getUserName());
        req.setPassword(this.mLoginInfo.getPassword());
        fillRegisterInfo(req);
        Observable<GetItemRsp> observable = orderInterface().post_GetItem(req);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetItemRsp>() {
            @Override
            public void onSuccess(GetItemRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null.");
                    mOnComplete.onGetItemComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());

                    if (null != rsp.getItems()) {
                        mOnComplete.onGetItemComplete(mOrderResult, rsp.getItems());
                    } else {
                        mOnComplete.onGetItemComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetItemComplete(mOrderResult, null);
            }
        });

        return 0;
    }

    @Override
    public int asyncGetCategory() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        GetCategoryReq req = new GetCategoryReq();
        req.setUserName(this.mLoginInfo.getUserName());
        req.setPassword(this.mLoginInfo.getPassword());

        fillRegisterInfo(req);
//        Observable<GetCategoryRsp> observable = orderInterface().post_GetCategory(req);
//        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetCategoryRsp>() {
//            @Override
//            public void onSuccess(GetCategoryRsp rsp) {
//                if (null == rsp) {
//                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
//                    mOrderResult.setResultMessage("Response is null!");
//                    mOnComplete.onGetCategoryComplete(mOrderResult, null);
//                } else {
//                    mOrderResult.setResultCode(rsp.getResultCode());
//                    mOrderResult.setResultMessage(rsp.getResultMessage());
//
//                    if (null != rsp.getCategories()) {
//                        mOnComplete.onGetCategoryComplete(mOrderResult, rsp.getCategories());
//                    } else {
//                        mOnComplete.onGetCategoryComplete(mOrderResult, null);
//                    }
//                }
//            }
//
//            @Override
//            public void onFault(String code, String msg) {
//                mOrderResult.setResultCode(code);
//                mOrderResult.setResultMessage(msg);
//                mOnComplete.onGetCategoryComplete(mOrderResult, null);
//            }
//        });

        return 0;
    }

    @Override
    public int asyncOpenTicket(OpenTicket req) {

        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        final OpenTicket mOpenTicket = req;
        OpenTicketReq myReq = new OpenTicketReq();
        myReq.setUserName(this.mLoginInfo.getUserName());
        myReq.setPassword(this.mLoginInfo.getPassword());

        fillRegisterInfo(myReq);
//        myReq.setEmployeeId(-1 == req.getEmployeeId() ? "" : String.format("%d", req.getEmployeeId()));
        //        myReq.setGuestCount(String.format("%d", req.getGuestCount()));
        myReq.setOrderType(req.getOrederType());
        myReq.setName(req.getName());
        myReq.setSerialNumber(req.getSerialNumber());
        myReq.setDiscountAmt(req.getDiscountAmt());

        myReq.setTableId(this.mLoginInfo.getTableID()); // there is also a tableId in req
        myReq.setIfCustomerView(req.isIfCustomerView(false) ? "true" : "false");
        myReq.setSendFCMAfterPay(req.isSendFCMAfterPay() ? "true" : "false");
        myReq.setAllowMultiOrders(req.isAllowMultiOrders() ? "true" : "false");

        if (null == req.getSelectList()) {
            return -1;
        }

        List<ItemInTicket> itemInTicketList = new ArrayList<>();
        for (int i = 0; i < req.getSelectList().size(); i++) {
            ItemInTicket itemInTicket = new ItemInTicket();
            itemInTicket.setId(String.format("%d", req.getSelectList()
                    .get(i)
                    .getId()));
            itemInTicket.setQuantity(String.format("%d", req.getSelectList()
                    .get(i)
                    .getQuantity()));
            itemInTicket.setAttributeId(req.getSelectList()
                    .get(i)
                    .getAttributeId());
            itemInTicket.setDiscountAmt(Double.toString(req.getSelectList()
                    .get(i)
                    .getDiscountAmt()));
            itemInTicketList.add(itemInTicket);
        }
        myReq.setItems(itemInTicketList);

//        Observable<OpenTicketRsp> observable = orderInterface().post_OpenTicket(myReq);
//        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<OpenTicketRsp>() {
//            @Override
//            public void onSuccess(OpenTicketRsp rsp) {
//                if (null == rsp) {
//                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
//                    mOrderResult.setResultMessage("Response is null!");
//                    mOnComplete.onOpenTicketComplete(mOrderResult, mOpenTicket, null);
//                } else {
//                    mOrderResult.setResultCode(rsp.getResultCode());
//                    mOrderResult.setResultMessage(rsp.getResultMessage());
//                    mOnComplete.onOpenTicketComplete(mOrderResult, mOpenTicket, rsp);
//                }
//            }
//
//            @Override
//            public void onFault(String code, String msg) {
//                mOrderResult.setResultCode(code);
//                mOrderResult.setResultMessage(msg);
//                mOnComplete.onOpenTicketComplete(mOrderResult, mOpenTicket, null);
//            }
//        });

        return 0;
    }

    @Override
    public int asyncUploadTrans(Transaction req, String serialNumber) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        final Transaction mTransaction = req;
        UploadTransReq myReq = new UploadTransReq();
        myReq.setUserName(this.mLoginInfo.getUserName());
        myReq.setPassword(this.mLoginInfo.getPassword());
        myReq.setTerminalID(this.mLoginInfo.getTerminalID());
        myReq.setTerminalSN(this.mLoginInfo.getTerminalSN());
        fillRegisterInfo(myReq);
        myReq.setTraceNum(req.getTraceNum());
        myReq.setTotalAmt(req.getTotalAmt());
        myReq.setApprovedAmt(req.getApprovedAmt());
        myReq.setTipAmt(req.getTipAmt());
        myReq.setTaxAmt(req.getTaxAmt());
        myReq.setCardBin(req.getmCardBin());
        myReq.setMaskedPan(req.getMaskedPan());
        myReq.setCardType(req.getmCardType());
        myReq.setCardHolderName(req.getmCardHolderName());
        myReq.setTransType(req.getTransType());
//        myReq.setEmployeeID(req.getmEmployeeId());
        myReq.setEntryMode(req.getEntryMode());
        myReq.setBaseAmt(req.getBaseAmt());
        myReq.setTipType(req.getmTipType());
        myReq.setTerminalRefNum(req.getmTerminalRefNum());
        myReq.setECRRefNum(req.getmEcrRefNum());
        myReq.setBatchNum(req.getmBatchNum());
        myReq.setAuthCode(req.getAuthCode());
        myReq.setTenderType(req.getmTenderType());


        Log.i(TAG,"CardBin:"+req.getmCardBin()+"..cardType:"+req.getmCardType()
        +"..name:"+req.getmCardHolderName()+"..transtype:"+req.getTransType());


        if (null != serialNumber) {
            myReq.setSerialNumber(serialNumber);
        }

//        Observable<UploadTransRsp> observable = orderInterface().post_UploadTrans(myReq);
//        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<UploadTransRsp>() {
//            @Override
//            public void onSuccess(UploadTransRsp rsp) {
//                mOrderResult.setResultCode(rsp.getResultCode());
//                mOrderResult.setResultMessage(rsp.getResultMessage());
//                mOnComplete.onUploadTransComplete(mOrderResult, mTransaction);
//            }
//
//            @Override
//            public void onFault(String code, String msg) {
//                mOrderResult.setResultCode(code);
//                mOrderResult.setResultMessage(msg);
//                mOnComplete.onUploadTransComplete(mOrderResult, null);
//            }
//        });

        return 0;
    }

    @Override
    public int asyncUploadMultiTrans(List<Transaction> req, String serialNumber) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        final List<Transaction> mTransList = req;
        UploadMultiTransReq myReq = new UploadMultiTransReq();
        List<Trans> transList = new ArrayList<>();

        fillRegisterInfo(myReq);
        if (null != serialNumber) {
            myReq.setSerialNumber(serialNumber);
        }

        for (int i = 0; i < req.size(); i++) {
            Trans trans = new Trans();

            trans.setTraceNum(req.get(i).getTraceNum());
            trans.setTotalAmt(req.get(i).getTotalAmt());
            trans.setApprovedAmt(req.get(i).getApprovedAmt());
            trans.setTipAmt(req.get(i).getTipAmt());
            trans.setTaxAmt(req.get(i).getTaxAmt());
            trans.setCardBin(req.get(i).getmCardBin());
            trans.setTransType(req.get(i).getTransType());
            trans.setCardHolderName(req.get(i).getmCardHolderName());
            trans.setCardType(req.get(i).getmCardType());
            trans.setMaskedPan(req.get(i).getMaskedPan());
//            trans.setEmployeeID(req.get(i).getmEmployeeId());
            trans.setTenderType(req.get(i).getmTenderType());
            trans.setEntryMode(req.get(i).getEntryMode());
            trans.setBaseAmt(req.get(i).getBaseAmt());
            trans.setTipType(req.get(i).getmTipType());
            trans.setTerminalRefNum(req.get(i).getmTerminalRefNum());
            trans.setECRRefNum(req.get(i).getmEcrRefNum());
            trans.setBatchNum(req.get(i).getmBatchNum());
            trans.setAuthCode(req.get(i).getAuthCode());
            transList.add(trans);
        }

        myReq.setMultiTrans(transList);

        Observable<UploadMultiTransRsp> observable = orderInterface().post_UploadMultiTrans(myReq);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<UploadMultiTransRsp>() {
            @Override
            public void onSuccess(UploadMultiTransRsp rsp) {
                Log.i(TAG,"suc code:"+rsp.getResultCode()+"msg:"+rsp.getResultMessage());
                mOrderResult.setResultCode(rsp.getResultCode());
                mOrderResult.setResultMessage(rsp.getResultMessage());
                mOnComplete.onUploadMultiTransComplete(mOrderResult, mTransList);
            }

            @Override
            public void onFault(String code, String msg) {
                Log.i(TAG,"onFault code:"+code+"msg:"+msg);
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onUploadMultiTransComplete(mOrderResult, mTransList);
            }
        });

        return 0;
    }

    @Override
    public int asyncGetAllTableOrders(boolean isOnlyShowOpened, boolean isByTableID) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetTableOrdersReq getTableOrdersReq = new GetTableOrdersReq();

        getTableOrdersReq.setTableID(isByTableID ? this.mLoginInfo.getTableID() : "");
        getTableOrdersReq.setArea("");
        getTableOrdersReq.setIsOnlyShowOpened(isOnlyShowOpened ? "1" : "0");
        fillRegisterInfo(getTableOrdersReq);
        Observable<GetTableOrdersRsp> observable = orderInterface().post_GetTableOrders(getTableOrdersReq); //在HttpServer中
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetTableOrdersRsp>() {
            @Override
            public void onSuccess(GetTableOrdersRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onGetAllTableOrdersComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());

                    if (null != rsp.getTables()) {
                        mOnComplete.onGetAllTableOrdersComplete(mOrderResult, rsp.getTables());
                    } else {
                        mOnComplete.onGetAllTableOrdersComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String errorMsg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(errorMsg);
                mOnComplete.onGetAllTableOrdersComplete(mOrderResult, null);
            }
        });

        return 0;
    }

    @Override
    public int asyncGetUnpaidOrders() {
        return 0;
    }

    @Override
    public int asyncGetOrderAmount(String traceNum) {
        return 0;
    }

    @Override
    public int asyncGetOrderDetail(String traceNum) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetOrderDetailReq getOrderDetailReq = new GetOrderDetailReq();
//        getOrderDetailReq.setUserName(this.mLoginInfo.getUserName());
//        getOrderDetailReq.setPassword(this.mLoginInfo.getPassword());
//        getOrderDetailReq.setToken(this.mLoginInfo.getToken());
        fillRegisterInfo(getOrderDetailReq);
//        getOrderDetailReq.setTerminalID(this.mLoginInfo.getTerminalID());
//        getOrderDetailReq.setTerminalSN(this.mLoginInfo.getTerminalSN());
        getOrderDetailReq.setTraceNum(traceNum);
        Log.i(TAG,"TablePro ID:"+this.mLoginInfo.getTableID());
        getOrderDetailReq.setTableID(this.mLoginInfo.getTableID());

        Observable<GetOrderDetailRsp> observable = orderInterface().post_GetOrderDetail(getOrderDetailReq);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetOrderDetailRsp>() {
            @Override
            public void onSuccess(GetOrderDetailRsp rsp) {

                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onGetOrderDetailComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    Log.i(TAG,"suc OrderDetail code:"+rsp.getResultCode()+"...msg:"+rsp.getResultMessage());
                    if (null != rsp.getOrders()) {
                        mOnComplete.onGetOrderDetailComplete(mOrderResult, rsp.getOrders());
                    } else {
                        mOnComplete.onGetOrderDetailComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetOrderDetailComplete(mOrderResult, null);
                Log.i(TAG,"onfault OrderDetail code:"+code+"...msg:"+msg);
            }
        });

        return 0;
    }

    @Override
    public int asyncGetEmployee() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        GetEmployeeReq req = new GetEmployeeReq();
        fillRegisterInfo(req);
        req.setUserName(this.mLoginInfo.getUserName());
        req.setPassword(this.mLoginInfo.getPassword());
//        req.setToken(this.mLoginInfo.getToken());
        final Observable<GetEmployeeRsp> observable = orderInterface().post_GetEmployee(req);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetEmployeeRsp>() {
            @Override
            public void onSuccess(GetEmployeeRsp rsp) {
                synchronized (GetEmployeeRsp.class) {
                    List<Employee> employeeList = new ArrayList<>();

                    if (null == rsp) {
                        mOrderResult.setResultCode(TM_NULL_RESPONSE);
                        mOrderResult.setResultMessage("Response is null!");
                    } else {
                        mOrderResult.setResultCode(null == rsp.getResultCode() ? "" : rsp.getResultCode());
                        mOrderResult.setResultMessage(null == rsp.getResultMessage() ? "" : rsp.getResultMessage());

                        if (null != rsp.getEmployees()) {
                            for (int i = 0; i < rsp.getEmployees().size(); i++) {
                                Employee employee = rsp.getEmployees().get(i);
                                employeeList.add(employee);
                            }
                        }
                    }

                    mOnComplete.onGetEmployeeComplete(mOrderResult, employeeList);
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetEmployeeComplete(mOrderResult, null);
            }
        });

        return 0;
    }


    @Override
    public int asyncGetAllTableInfo() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        GetAllTableInfoReq req = new GetAllTableInfoReq();
        fillRegisterInfo(req);
        req.setUserName(this.mLoginInfo.getUserName());
        req.setPassword(this.mLoginInfo.getPassword());
//        req.setToken(this.mLoginInfo.getToken());
        final Observable<GetAllTableInfoRsp> observable = orderInterface().post_GetAllTableInfo(req);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<GetAllTableInfoRsp>() {
            @Override
            public void onSuccess(GetAllTableInfoRsp rsp) {
                synchronized (GetAllTableInfoRsp.class) {
                    List<TableInfo> tableInfoList = new ArrayList<>();

                    if (null == rsp) {
                        mOrderResult.setResultCode(TM_NULL_RESPONSE);
                        mOrderResult.setResultMessage("Response is null!");
                    } else {
                        mOrderResult.setResultCode(null == rsp.getResultCode() ? "" : rsp.getResultCode());
                        mOrderResult.setResultMessage(null == rsp.getResultMessage() ? "" : rsp.getResultMessage());

                        if (null != rsp.getTables() && !rsp.getTables().isEmpty()) {
                            tableInfoList.addAll(rsp.getTables());
                        }
                    }

                    mOnComplete.onGetAllTableInfoComplete(mOrderResult, tableInfoList);
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetAllTableInfoComplete(mOrderResult, null);
            }
        });

        return 0;
    }

    @Override
    public int asyncSendNotification(String sendType) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());

        SendNotificationReq req = new SendNotificationReq();
        fillRegisterInfo(req);
        req.setUserName(this.mLoginInfo.getUserName());
        req.setPassword(this.mLoginInfo.getPassword());
//        req.setToken(this.mLoginInfo.getToken());
        req.setSendType(sendType);
        req.setTableID(this.mLoginInfo.getTableID());
        final Observable<SendNotificationRsp> observable = orderInterface().post_SendNotification(req);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<SendNotificationRsp>() {
            @Override
            public void onSuccess(SendNotificationRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                } else {
                    mOrderResult.setResultCode(null == rsp.getResultCode() ? "" : rsp.getResultCode());
                    mOrderResult.setResultMessage(null == rsp.getResultMessage() ? "" : rsp.getResultMessage());
                }

                mOnComplete.onSendNotificationComplete(mOrderResult);
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onSendNotificationComplete(mOrderResult);
            }
        });

        return 0;
    }


    @Override
    public int asyncModifyTicket(String TraceNum, String ModifyType, String discountAmt,
                                 String serialNumber,
                                 List<ModifyItemReq> modifyItemList,
                                 List<OpenItemReq> openItemList) {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        ModifyTicketReq modifyTicketReq = new ModifyTicketReq();

        fillRegisterInfo(modifyTicketReq);
//        modifyTicketReq.setUserName(this.mLoginInfo.getUserName());
//        modifyTicketReq.setPassword(this.mLoginInfo.getPassword());
//        modifyTicketReq.setToken(this.mLoginInfo.getToken());
        modifyTicketReq.setTraceNum(TraceNum);
        modifyTicketReq.setSerialNumber(serialNumber);
        modifyTicketReq.setModifyType(ModifyType);
//        modifyTicketReq.setGuestCount();
//        modifyTicketReq.setEmployeeId();
        modifyTicketReq.setDiscountAmt(discountAmt);
        if (null != modifyItemList) {
            modifyTicketReq.setModifyItemList(modifyItemList);
        }

        modifyTicketReq.setOpenItemList(openItemList);

        Observable<ModifyTicketRsp> observable = orderInterface().post_ModifyTicket(modifyTicketReq);
        HttpServiceManager.getInstance().toSubscribe(observable, new OnSuccessAndFaultSub<ModifyTicketRsp>() {
            @Override
            public void onSuccess(ModifyTicketRsp rsp) {

                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onModifyTicketComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    Log.i(TAG,"ModifyTicket Rsp code:"+rsp.getResultCode()+"...message:"+rsp.getResultMessage() );
                    if (null != rsp.getExtDataList()) {
                        mOnComplete.onModifyTicketComplete(mOrderResult, rsp.getExtDataList());
                    } else {
                        mOnComplete.onModifyTicketComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onModifyTicketComplete(mOrderResult, null);
            }
        });

        return 0;
    }

    @Override
    public int asyncRegisterDevice() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetRegisterReq getRegisterReq = new GetRegisterReq();

        getRegisterReq.setDeviceSn(RegisterInfoInstance.getInstance().getDeviceSn());
        getRegisterReq.setAppkeyId(RegisterInfoInstance.getInstance().getAppKeyId());
        getRegisterReq.setDeviceId(RegisterInfoInstance.getInstance().getDeviceId());
        getRegisterReq.setTimeStamp(RegisterInfoInstance.getInstance().getTimeStamp());
        getRegisterReq.setSigntureData(RegisterInfoInstance.getInstance().getSignData());

        // todo: hardcode set info
//        getRegisterReq.setDeviceSn("19108522502183");
//        getRegisterReq.setAppkeyId("TestForPAX");
//        getRegisterReq.setDeviceId("");
//        getRegisterReq.setTimeStamp(RegisterInfoInstance.getInstance().getTimeStamp());
//        // getRegisterReq.setSigntureData("hZHMGuW/MzLjzE86vphmlBG+ziw=");

        System.out.println("The device SN:" + RegisterInfoInstance.getInstance().getDeviceSn());
        System.out.println("The device getAppKeyId:" + RegisterInfoInstance.getInstance().getAppKeyId());
        System.out.println("The device getDeviceId:" + RegisterInfoInstance.getInstance().getDeviceId());
        System.out.println("The device getSignData ----:" + RegisterInfoInstance.getInstance().getSignData());


        Observable<GetRegisterRsp> observable = orderInterface().post_RegisterDevice(getRegisterReq);
        HttpServiceManager.getInstance().toSubscribe(observable,new OnSuccessAndFaultSub<GetRegisterRsp>(){
            @Override
            public void onSuccess(GetRegisterRsp rsp) {
                Log.i(TAG,"REGISTER RSP SUC");
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onRegisterComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    mOnComplete.onRegisterComplete(mOrderResult,rsp);
                }
            }

            @Override
            public void onFault(String code, String msg) {

                AppLog.i(TAG,"ON FAULT!!!!---");
                AppLog.i(TAG,"CODE:"+code+"MSG:"+msg);
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                ProcessMessage processMessage = new ProcessMessage();
                processMessage.setMessageCode(MessgeCode.REGISTER_DEVICE_FAIL);
                EventBus.getDefault().post(processMessage);
               // mOnComplete.onModifyTicketComplete(mOrderResult, null);
            }
        });


        return 0;
    }

    @Override
    public int asyncUnregisterDevice() {
        return 0;
    }

    private String fillSignData(){
        encryptKey = FinancialApplication.getApp().getReqEncryptKey();
        if(null != encryptKey){
           // Log.i(TAG,encryptKey);
            return SecurityUtils.genHMAC(deviceId+tokenId+timeStamp,encryptKey);
        }else{
            Log.e(TAG,"SIGNATURE DATEA ERR!!!!!----no key");
        }
        return null;
    }
    private void fillRegisterInfo(BaseReqModel req){
        deviceId = FinancialApplication.getApp().getDeviceId();
        tokenId = FinancialApplication.getApp().getToken();
        timeStamp = RegisterInfoInstance.getInstance().getTimeStamp();

        req.setDeviceID(deviceId);
        req.setTimeStamp(timeStamp);
        req.setToken(tokenId);
        req.setSignatureData(fillSignData());
        Log.i(TAG,"async get :deviceid:"+deviceId+"..token:"+tokenId+"..time:"
                +timeStamp+"..signdata:"+fillSignData());
    }

    @Override
    public int asyncGetSettings() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetSettingReq getSettingReq = new GetSettingReq();
        fillRegisterInfo(getSettingReq);
        Observable<GetSettingRsp> observable = orderInterface().post_GetSetting(getSettingReq);
        HttpServiceManager.getInstance().toSubscribe(observable,new OnSuccessAndFaultSub<GetSettingRsp>(){
            @Override
            public void onSuccess(GetSettingRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onGetSettingComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    if (null != rsp.getSettings()) {
                        mOnComplete.onGetSettingComplete(mOrderResult, rsp.getSettings());
                    } else {
                        mOnComplete.onGetSettingComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String msg) {
                mOrderResult.setResultCode(code);
                mOrderResult.setResultMessage(msg);
                mOnComplete.onGetSettingComplete(mOrderResult, null);
            }
        });
        return 0;
    }

    @Override
    public int asyncGetTable() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetTableReq getTableReq = new GetTableReq();
        fillRegisterInfo(getTableReq);
        Observable<GetTableRsp> observable = orderInterface().post_GetTable(getTableReq);
        HttpServiceManager.getInstance().toSubscribe(observable,new OnSuccessAndFaultSub<GetTableRsp>(){
            @Override
            public void onSuccess(GetTableRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onGetTableComplete(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    if (null != rsp.getTablePros()) {
                        mOnComplete.onGetTableComplete(mOrderResult, rsp.getTablePros());
                    } else {
                        mOnComplete.onGetTableComplete(mOrderResult, null);
                    }
                }
            }

            @Override
            public void onFault(String code, String msg) {

            }
        });
        return 0;
    }

    @Override
    public int asyncGetStoreInfo() {
        Log.i(TAG, Tools._FILE_FUNC_LINE_());
        GetStoreInfoReq getStoreInfoReq = new GetStoreInfoReq();
        fillRegisterInfo(getStoreInfoReq);



        Observable<GetStoreInfoRsp> observable = orderInterface().post_GetStoreInfo(getStoreInfoReq);
        HttpServiceManager.getInstance().toSubscribe(observable,new OnSuccessAndFaultSub<GetStoreInfoRsp>(){
            @Override
            public void onSuccess(GetStoreInfoRsp rsp) {
                if (null == rsp) {
                    mOrderResult.setResultCode(TM_NULL_RESPONSE);
                    mOrderResult.setResultMessage("Response is null!");
                    mOnComplete.onGetStoreInfo(mOrderResult, null);
                } else {
                    mOrderResult.setResultCode(rsp.getResultCode());
                    mOrderResult.setResultMessage(rsp.getResultMessage());
                    mOnComplete.onGetStoreInfo(mOrderResult, rsp);
                }
            }

            @Override
            public void onFault(String code, String msg) {

            }
        });
        return 0;
    }
}
