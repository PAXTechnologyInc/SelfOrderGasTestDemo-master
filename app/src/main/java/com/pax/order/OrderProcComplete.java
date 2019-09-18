package com.pax.order;


import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.pax.order.entity.Advertisement;
import com.pax.order.entity.ErrDataInOpenTicket;
import com.pax.order.entity.GoodsCategory;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.OrderAmount;
import com.pax.order.entity.OrderInfo;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.entity.RspMsg;
import com.pax.order.entity.TableOrderInfo;
import com.pax.order.entity.Transaction;
import com.pax.order.enums.DiscountType;
import com.pax.order.logger.AppLog;
import com.pax.order.menu.GetFixedTip;
import com.pax.order.orderserver.entity.OrderResult;
import com.pax.order.orderserver.entity.getAllTableInfo.TableInfo;
import com.pax.order.orderserver.entity.getEmployee.Employee;
import com.pax.order.orderserver.entity.getadvertisement.SpAdvertisement;
import com.pax.order.orderserver.entity.getcategory.Category;
import com.pax.order.orderserver.entity.getitem.Item;
import com.pax.order.orderserver.entity.getorderdetail.SPOrderDetail;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;
import com.pax.order.orderserver.entity.gettable.TablePro;
import com.pax.order.orderserver.entity.gettableorders.OrderInTable;
import com.pax.order.orderserver.entity.gettableorders.Table;
import com.pax.order.orderserver.entity.openticket.ErrInTicket;
import com.pax.order.orderserver.entity.openticket.OpenTicketRsp;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterRsp;
import com.pax.order.orderserver.entity.setting.SettingList;
import com.pax.order.orderserver.inf.OnComplete;
import com.pax.order.settings.GetStoreInfoInstance;
import com.pax.order.settings.GetTableInstance;
import com.pax.order.util.SecurityUtils;
import com.pax.order.util.ToastUtils;
import com.pax.order.util.Tools;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_ORDERDATA_ERROR;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_SUCCESS;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_TRACENUMBER_NOTFOUND;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_UNPAID_ORDERS;
import static com.pax.order.orderserver.constant.spRespCode.TM_INTERNAL_ERR;
import static com.pax.order.orderserver.constant.spRespCode.TM_NULL_RESPONSE;
import static com.pax.order.orderserver.constant.spRespCode.TM_ON_FAIL_THROW;
import static com.pax.order.orderserver.constant.spRespCode.TM_ORDER_NO_DETAIL;
import static com.pax.order.orderserver.constant.spRespCode.TM_PIC_DOWNFAIL;
import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_VERIFICATION_ERROR;


/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/8/14 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public class OrderProcComplete implements OnComplete {
    private static final int TICKET = 1;
    private static final int ADVER = 0;
    private static final int UPLOADTRANS = 2;
    private static final int  GETITEM = 3;
    private static final int GETORDERDETAIL = 4;
    private static final String TAG = "OrderProcComplete";
    private static OrderProcComplete instance = null;

    private OrderProcComplete() {

    }

    public static synchronized OrderProcComplete getInstance() {
        if (instance == null) {
            instance = new OrderProcComplete();
        }
        return instance;
    }

    private ProcessMessage dealProcessMessge(OrderResult result, int source) {
        ProcessMessage processMessage = new ProcessMessage();

        //通用返回码：
        if(SP_RESPONSE_VERIFICATION_ERROR.equals(result.getResultCode())){
            processMessage.setMessageCode(MessgeCode.REGISTER_DEVICE_RETRY);
            return processMessage;
        }
        switch (source) {
            case ADVER:
                switch (result.getResultCode()) {
                    case SP_RESPONSE_SUCCESS:
                        processMessage.setMessageCode(MessgeCode.DOWNALLINFOSUCC);
                        break;
                    case TM_INTERNAL_ERR:
                    case TM_NULL_RESPONSE:
                    case TM_PIC_DOWNFAIL:
                    case TM_ON_FAIL_THROW:
                        processMessage.setMessageCode(MessgeCode.DOWNALLINFOERR);
                        break;
                    default:
                        processMessage.setMessageCode(MessgeCode.DOWNALLINFOERR);
                        break;
                }
                break;
            case TICKET:
                switch (result.getResultCode()) {
                    case SP_RESPONSE_SUCCESS:
                        processMessage.setMessageCode(MessgeCode.OPENTICKETSUCC);
                        break;
                    case TM_INTERNAL_ERR:
                    case TM_NULL_RESPONSE:
                    case TM_PIC_DOWNFAIL:
                    case TM_ON_FAIL_THROW:
                        processMessage.setMessageCode(MessgeCode.OPENTICKETFAIL);
                        break;
                    case SP_RESPONSE_ORDERDATA_ERROR:
                    case SP_RESPONSE_TRACENUMBER_NOTFOUND:
                        processMessage.setMessageCode(MessgeCode.OPENTICKETNOPRODUCT);
                        break;
                    default:
                        processMessage.setMessageCode(MessgeCode.OPENTICKETFAIL);
                        break;
                }
                break;
            case UPLOADTRANS:
                switch (result.getResultCode()) {
                    case SP_RESPONSE_SUCCESS:
                        processMessage.setMessageCode(MessgeCode.UPLOADTRANSSUCC);
                        break;
                    case TM_INTERNAL_ERR:
                    case TM_NULL_RESPONSE:
                    case TM_PIC_DOWNFAIL:
                    case TM_ON_FAIL_THROW:
                        processMessage.setMessageCode(MessgeCode.UPLOADTRANSFAIL);
                        break;
                    default:
                        processMessage.setMessageCode(MessgeCode.UPLOADTRANSFAIL);
                        break;
                }
                break;
            case GETITEM:
                switch (result.getResultCode()){
                    case SP_RESPONSE_SUCCESS:
                        break;
                    default:
                        processMessage.setMessageCode(MessgeCode.DOWNITEMFAIL);
                        break;
                }
                break;
            default:
                break;
        }
        return processMessage;
    }

    @Override
    public int onGetItemComplete(OrderResult ret, List<Item> rsp) {
        if ((null == rsp) || (null == ret)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNITEMFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            new Thread(new parseSPItemTask(rsp)).start();
        } else {
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + ", retMessage:" + ret.getResultMessage());

            ProcessMessage processMessage = dealProcessMessge(ret, GETITEM);
//            ProcessMessage processMessage = new ProcessMessage();
//            processMessage.setMessageCode(MessgeCode.DOWNITEMFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        return 0;
    }

    @Override
    public int onVerifyLoginInfoComplete(OrderResult ret) {
        // [TO DO]
        AppLog.d(TAG, "------onVerifyLoginInfoComplete----");
        return 0;
    }

    @Override
    public int onGetAllCategoryInfoComplete(OrderResult ret, List<SpAdvertisement> adRsp, List<Item> itemRsp,
                                            List<Category> cateRsp) {
        return 0;
    }

    @Override
    public int onGetAdvertisementComplete(OrderResult ret, List<SpAdvertisement> rsp) {
        AppLog.d(TAG, "------onGetAdvertisementComplete----");

        if ((null == ret) || (null == rsp)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNADVERFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }
//        AppLog.d(TAG, "resultCode: " + ret.getResultCode());
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNADVERSUCC);

            List<Advertisement> mAdvertisementList = new ArrayList<>();
            if (null != rsp && !rsp.isEmpty()) {
                for (SpAdvertisement spAdvertisement : rsp) {
                    Advertisement advertisement = new Advertisement();
                    advertisement.setItemIds(spAdvertisement.getItemIds());
                    String picUrl = spAdvertisement.getPictureUrl();

                    if (null == picUrl || "".equals(picUrl)) {
                        advertisement.setPictureUrl("");
                    } else {
                        advertisement.setPictureUrl(picUrl);
                    }

                    advertisement.setDescription(null == spAdvertisement.getDescription() ? "" :
                            spAdvertisement.getDescription());
                    advertisement.setExtData(null == spAdvertisement.getExtData() ? "" : spAdvertisement
                            .getExtData());
                    mAdvertisementList.add(advertisement);
                } // end of for
            }

            processMessage.setAdvertisementList(mAdvertisementList);
            EventBus.getDefault().post(processMessage);
        } else if (ret.getResultCode().equals(TM_ON_FAIL_THROW)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNADVERTIMEOUT);
            EventBus.getDefault().post(processMessage);
            return -1;
        } else {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNADVERFAIL);
            EventBus.getDefault().post(processMessage);
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            return -1;
        }


        return 0;
    }

    @Override
    public int onGetCategoryComplete(OrderResult ret, List<Category> rsp) {
        AppLog.d(TAG, "------onGetCategoryComplete----");

        if ((null == ret) || (null == rsp)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNCATEGORYFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }


        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNCATEGORYSUCC);

            List<GoodsCategory> goodsCategoryList = new ArrayList<>();

            if (null != rsp && !rsp.isEmpty()) {
                for (Category category : rsp) {
                    GoodsCategory goodsCategory = new GoodsCategory();
                    goodsCategory.setTypeId(Integer.parseInt(category.getId()));
                    goodsCategory.setTypeName(category.getName());
                    int picUrl = category.getPicUrl();
                    if (0 == picUrl) {
                        goodsCategory.setPicUrl(0);
                    } else {
                        goodsCategory.setPicUrl(picUrl);
                    }

                    goodsCategoryList.add(goodsCategory);
                }
            }

            processMessage.setGoodsCategoryList(goodsCategoryList);
            EventBus.getDefault().post(processMessage);
            AppLog.d(TAG, "------onGetCategoryComplete----");
        } else {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.DOWNCATEGORYFAIL);
            EventBus.getDefault().post(processMessage);
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            return -1;
        }


        return 0;
    }

    @Override
    public int onOpenTicketComplete(OrderResult ret, OpenTicket openTicket, OpenTicketRsp rsp) {
        AppLog.d(TAG, "------onOpenTicketComplete----");

        if (null != rsp) {
            String strDateTime = Tools.dateTimeFormat(rsp.getTransDate(), rsp.getTransTime());
            openTicket.setOrderTime(strDateTime);
            openTicket.setTraceNum(rsp.getTraceNum());
            if (null != rsp.getDiscountType()) {
                openTicket.setDiscountType(rsp.getDiscountType()
                        .equals("Amount") ? DiscountType.Amount : DiscountType.Percentage);
            }
            openTicket.setDiscountValue(rsp.getDiscountValue());
            openTicket.setDueAmt(rsp.getDueAmt());
            openTicket.setTotalAmt(rsp.getTotalAmt());
            openTicket.setTaxAmt(rsp.getTaxAmt());
            openTicket.setSubTotalAmt(rsp.getSubTotal());
            List<ErrDataInOpenTicket> errDataInOpenTicketsList = new ArrayList<>();
            if (null != rsp.getExtDataList()) {
                for (ErrInTicket errInTicket : rsp.getExtDataList()) {
                    ErrDataInOpenTicket errDataInOpenTicket = new ErrDataInOpenTicket();
                    errDataInOpenTicket.setType(errInTicket.getType());
                    errDataInOpenTicket.setItemID(errInTicket.getItemID());
                    errDataInOpenTicket.setAttributeId(errInTicket.getAttributeId());
                    errDataInOpenTicket.setStock(errInTicket.getStock());
                    errDataInOpenTicket.setQuantity(errInTicket.getQuantity());
                    errDataInOpenTicketsList.add(errDataInOpenTicket);
                }
            }
            openTicket.setErrDataInOpenTickets(errDataInOpenTicketsList);
        }

        if ((null == ret) || (null == rsp) || (openTicket.getSelectList().size() == 0)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.OPENTICKETFAIL);
            if (null != ret) {
                processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
            }
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        ProcessMessage processMessage = dealProcessMessge(ret, TICKET);

        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            FinancialApplication.getController().setIsOpen(true);
            FinancialApplication.getController().setTraceNum(rsp.getTraceNum());
        } else if ((ret.getResultCode()
                .equals(SP_RESPONSE_ORDERDATA_ERROR)) || (ret.getResultCode()
                .equals(SP_RESPONSE_TRACENUMBER_NOTFOUND))) {
            List<ErrDataInOpenTicket> errDataInOpenTickets = openTicket.getErrDataInOpenTickets();
            processMessage.setErrDataInOpenTickets(errDataInOpenTickets);
            EventBus.getDefault().post(processMessage);
            return -1;
        } else if (ret.getResultCode().equals(SP_RESPONSE_UNPAID_ORDERS)) {
            ProcessMessage processMessage2 = new ProcessMessage();
            processMessage2.setMessageCode(MessgeCode.OPENTICKETALREADYEXIST);
            EventBus.getDefault().post(processMessage2);
            return -1;
        } else {
            processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
            EventBus.getDefault().post(processMessage);
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            return -1;
        }
        EventBus.getDefault().post(processMessage);
        return 0;
    }

    @Override
    public int onUploadTransComplete(OrderResult ret, Transaction rsp) {
        AppLog.d(TAG, "-----onUploadTransComplete-----");
        if ((null == ret) || (null == rsp)) {
            return -1;
        }
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            AppLog.d(TAG, "-----onUploadTransComplete-SP_RESPONSE_SUCCESS----");
            //删除未支付订单
            //单笔上送在上送完之后删除db数据
//            FinancialApplication.getOpenTicketDbHelper().deleteOpenTicketByTraceNum(rsp.getTraceNum());
//            FinancialApplication.getPayDataDb().clearPayData();
            //存储支付结果数据？
        } else {
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            return -1;
        }

        return 0;
    }

    @Override
    public int onUploadMultiTransComplete(OrderResult ret, List<Transaction> rsp) {
        //不管后台状态如何，都需要删除本地数据库
        FinancialApplication.getOpenTicketDbHelper().deleteAllOpenTicket();
        FinancialApplication.getOpenTicketDbHelper().deleteAllStorageGoods();
        FinancialApplication.getPayDataDb().clearPayData();
        FinancialApplication.getTransactionDb().clearTransaction();
        AppLog.d(TAG, "-----onUploadMultiTransComplete-----");
        if ((null == ret) || (null == rsp)) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.UPLOADTRANSFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }
        ProcessMessage processMessage = dealProcessMessge(ret, UPLOADTRANS);
        EventBus.getDefault().post(processMessage);
        return 0;
    }

    @Override
    public int onGetAllTableOrdersComplete(OrderResult ret, List<Table> tableList) {
        AppLog.d(TAG, "-------onGetAllTableOrdersComplete-----");
        ProcessMessage processMessage = new ProcessMessage();

        if (null == ret) {
            AppLog.d(TAG, "onGetAllTableOrdersComplete failed!");
            processMessage.setMessageCode(MessgeCode.GET_TABLE_ORDER_FAIL);
            processMessage.setRspMsg(new RspMsg(TM_ORDER_NO_DETAIL, "Unknown Error"));
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            processMessage.setMessageCode(MessgeCode.GET_TABLE_ORDER_SUCC);

            List<TableOrderInfo> tableOrderInfoList = new ArrayList<>();
            if (null != tableList && !tableList.isEmpty()) {
                for (Table table : tableList) {
                    TableOrderInfo tableOrderInfo = new TableOrderInfo();
                    tableOrderInfo.setTableID(table.getTableID());
                    tableOrderInfo.setArea((table.getArea()));
//                    tableOrderInfo.setOpened("1".equals(table.getIsOpened()) ? true : false);
                    tableOrderInfo.setOpened(table.isHaveUnpaidOrde());
                    List<OrderInfo> orderInfoList = new ArrayList<>();
                    int orderSize = null == table.getOrders() ? 0 : table.getOrders().size();
                    if (orderSize > 0) {
                        for (OrderInTable orderInTable : table.getOrders()) {
                            OrderInfo orderInfo = new OrderInfo();
                            orderInfo.setCustomerName(orderInTable.getCustomerName());
                            orderInfo.setTraceNum(orderInTable.getTraceNum());
                            orderInfo.setOrderType(orderInTable.getOrderType());
                            orderInfo.setSeatNum(orderInTable.getSeatNum());
                            orderInfo.setOrderStatus(orderInTable.getOrderStatus());

                            //isOpened 状态为true，还需要判断orderstatus，只有状态为Processing时，才认为最终状态为true
//                            String orderStatus = orderInTable.getOrderStatus();
//                            if (orderStatus != null && !orderStatus.isEmpty()) {
//                                if (orderStatus.equals("Processing")) {
//                                    tableOrderInfo.setOpened(true);
//                                }
//                            }

                            orderInfoList.add(orderInfo);
                        }
                    }

                    tableOrderInfo.setOrderInfoList(orderInfoList);
                    tableOrderInfoList.add(tableOrderInfo);
                }
            }

            processMessage.setTableOrderInfoList(tableOrderInfoList);
            processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
            EventBus.getDefault().post(processMessage);
        } else {
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            processMessage.setMessageCode(MessgeCode.GET_TABLE_ORDER_FAIL);
            processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        return 0;
    }

    @Override
    public int onGetUnpaidOrdersComplete(OrderResult ret, List<OrderInfo> orderInfoList) {
        return 0;
    }

    @Override
    public int onGetOrderAmountComplete(OrderResult ret, OrderAmount rsp) {
        return 0;
    }

    @Override
    public int onGetOrderDetailComplete(OrderResult ret, List<SPOrderDetail> rsp) {
        AppLog.d(TAG, "-----onGetOrderDetailComplete-----");
        if (null == ret) {
            return -1;
        }

        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setMessageCode(MessgeCode.ORDER_DETAIL_MSG);

        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {

            FinancialApplication.getOpenTicketDbHelper().deleteAllOpenTicket();
            FinancialApplication.getOpenTicketDbHelper().deleteAllStorageGoods();
            FinancialApplication.getPayDataDb().clearPayData();
            FinancialApplication.getTransactionDb().clearTransaction();
            FinancialApplication.getOrderDetailDb().deleteOrderDetail();
            FinancialApplication.getController().clearOrderDetailList();

            if (null != rsp && !rsp.isEmpty()) {
//                new Thread(new RecoverOrderTask(rsp)).start();
                FinancialApplication.getApp().runInBackground(new RecoverOrderTask(rsp));
                return 0;
            } else {
                processMessage.setRspMsg(new RspMsg(TM_ORDER_NO_DETAIL, "have no order detail"));
            }
        } else {
            processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
        }

        EventBus.getDefault().post(processMessage);
        return 0;
    }

    @Override
    public void onProgress(int msgID, int succeedCount, int totalCount) {
    }

    @Override
    public int onGetEmployeeComplete(OrderResult ret, List<Employee> rsp) {
        return 0;
    }

    @Override
    public int onGetAllTableInfoComplete(OrderResult ret, List<TableInfo> rsp) {
        return 0;
    }

    @Override
    public int onSendNotificationComplete(OrderResult ret) {
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            ToastUtils.showMessage(FinancialApplication.getApp(), "CallWaiter Complete!");
        }
        return 0;
    }

    @Override
    public int onModifyTicketComplete(OrderResult ret, List<ErrInTicket> errInTicketList) {
        AppLog.d(TAG, "------onModifyTicketComplete------");
        if (null == ret) {
            ProcessMessage processMessage = new ProcessMessage();
            processMessage.setMessageCode(MessgeCode.OPENTICKETFAIL);
            EventBus.getDefault().post(processMessage);
            return -1;
        }

        ProcessMessage processMessage = dealProcessMessge(ret, TICKET);

        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {

        } else if ((ret.getResultCode().equals(SP_RESPONSE_ORDERDATA_ERROR)) || (ret.getResultCode().equals(SP_RESPONSE_TRACENUMBER_NOTFOUND))) {

            List<ErrDataInOpenTicket> errDataInOpenTicketsList = new ArrayList<>();
            if (null != errInTicketList) {
                for (ErrInTicket errInTicket : errInTicketList) {
                    ErrDataInOpenTicket errDataInOpenTicket = new ErrDataInOpenTicket();
                    errDataInOpenTicket.setType(errInTicket.getType());
                    errDataInOpenTicket.setItemID(errInTicket.getItemID());
                    errDataInOpenTicket.setAttributeId(errInTicket.getAttributeId());
                    errDataInOpenTicket.setStock(errInTicket.getStock());
                    errDataInOpenTicket.setQuantity(errInTicket.getQuantity());
                    errDataInOpenTicketsList.add(errDataInOpenTicket);
                }
            }

            processMessage.setErrDataInOpenTickets(errDataInOpenTicketsList);
            EventBus.getDefault().post(processMessage);
            return -1;
        } else {
            processMessage.setRspMsg(new RspMsg(ret.getResultCode(), ret.getResultMessage()));
            EventBus.getDefault().post(processMessage);
            AppLog.d(TAG, "retCode:" + ret.getResultCode() + "retMessage:" + ret.getResultMessage());
            return -1;
        }
        EventBus.getDefault().post(processMessage);
        return 0;
    }

    @Override
    public int onRegisterComplete(OrderResult ret, GetRegisterRsp rsp) {
        ProcessMessage processMessage = new ProcessMessage();
        FinancialApplication.getApp().setToken(rsp.getToken());
        FinancialApplication.getApp().setDeviceId(rsp.getDeviceId());
        if(rsp == null){
            processMessage.setMessageCode(MessgeCode.REGISTER_DEVICE_FAIL);
        }
        if(null != rsp.getReqEncryKey()) {
            try {
                Log.i(TAG, "req key:" + rsp.getReqEncryKey());

                String reqKey = SecurityUtils.decryptAESwithFixedKey(rsp.getReqEncryKey());
                if (reqKey != null) {
                    FinancialApplication.getApp().setReqEncryptKey(reqKey);
                    Log.i(TAG, "REQ KEY == " + reqKey);
                    processMessage.setMessageCode(MessgeCode.REGISTER_DEVICE_SUC);
                }else{
                    processMessage.setMessageCode(MessgeCode.REGISTER_DEVICE_FAIL);
                }
            }catch (Exception e){
                AppLog.e(TAG,"AES DECRYPT EXCEPTION:"+e);
            }
        }
        EventBus.getDefault().post(processMessage);
        Log.i(TAG,"DEVICE ID:"+ rsp.getDeviceId()+"..token:"+rsp.getToken());
        Log.i(TAG,"REQ KEY="+rsp.getReqEncryKey());

        return 0;
    }

    @Override
    public int onGetSettingComplete(OrderResult ret, List<SettingList> settingLists) {
        if(ret == null) return (-1);
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)) {
            if (null != settingLists && !settingLists.isEmpty()){
                GetFixedTip.getInstance().parseSettingParam(settingLists);
            }

        }
        return 0;
    }

    @Override
    public int onGetTableComplete(OrderResult ret, List<TablePro> tableLists) {
        AppLog.d(TAG, "-----onGetTableComplete-----");
        ProcessMessage processMessage = new ProcessMessage();
        processMessage.setMessageCode(MessgeCode.GET_TABLE_ID_FAIL);
        if(ret == null || tableLists == null ) return (-1);
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)){
            GetTableInstance.getInstance().setTableProList(tableLists);
            processMessage.setMessageCode(MessgeCode.GET_TABLE_ID_SUC);
        }
        EventBus.getDefault().post(processMessage);
        return 0;
    }

    @Override
    public int onGetStoreInfo(OrderResult ret, GetStoreInfoRsp rsp) {
        if(ret == null || rsp == null ) return (-1);
        if (ret.getResultCode().equals(SP_RESPONSE_SUCCESS)){
            GetStoreInfoInstance.getInstance().setStoreInfo(rsp);
            String storeName = rsp.getName();
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
            SharedPreferences.Editor editor = pref.edit();
            if (storeName != null ) {
                editor.putString(ParamConstants.STORE_NAME, storeName);
            }
            editor.apply();
        }
        return 0;
    }
}
