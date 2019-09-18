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
package com.pax.order.orderserver.inf;

import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.OrderAmount;
import com.pax.order.entity.OrderInfo;
import com.pax.order.entity.Transaction;
import com.pax.order.orderserver.entity.OrderResult;
import com.pax.order.orderserver.entity.getAllTableInfo.TableInfo;
import com.pax.order.orderserver.entity.getEmployee.Employee;
import com.pax.order.orderserver.entity.getadvertisement.SpAdvertisement;
import com.pax.order.orderserver.entity.getcategory.Category;
import com.pax.order.orderserver.entity.getitem.Item;
import com.pax.order.orderserver.entity.getorderdetail.SPOrderDetail;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;
import com.pax.order.orderserver.entity.gettable.TablePro;
import com.pax.order.orderserver.entity.gettableorders.Table;
import com.pax.order.orderserver.entity.openticket.ErrInTicket;
import com.pax.order.orderserver.entity.openticket.OpenTicketRsp;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterRsp;
import com.pax.order.orderserver.entity.setting.SettingList;

import java.util.List;

/**
 * async result
 */
public interface OnComplete {

    int onVerifyLoginInfoComplete(OrderResult ret);

    int onGetAllCategoryInfoComplete(
            OrderResult ret, List<SpAdvertisement> adRsp, List<Item> itemRsp, List<Category> cateRsp);

    int onGetAdvertisementComplete(OrderResult ret, List<SpAdvertisement> rsp);

    int onGetItemComplete(OrderResult ret, List<Item> rsp);

    int onGetCategoryComplete(OrderResult ret, List<Category> rsp);

    int onOpenTicketComplete(OrderResult ret, OpenTicket openTicket, OpenTicketRsp rsp);

    int onUploadTransComplete(OrderResult ret, Transaction rsp);

    int onUploadMultiTransComplete(OrderResult ret, List<Transaction> rsp);

    int onGetAllTableOrdersComplete(OrderResult ret, List<Table> tableList);

    int onGetUnpaidOrdersComplete(OrderResult ret, List<OrderInfo> orderInfoList);

    int onGetOrderAmountComplete(OrderResult ret, OrderAmount rsp);

    int onGetOrderDetailComplete(OrderResult ret, List<SPOrderDetail> rsp);

    void onProgress(int msgID, int succeedCount, int totalCount);

    int onGetEmployeeComplete(OrderResult ret, List<Employee> rsp);

    int onGetAllTableInfoComplete(OrderResult ret, List<TableInfo> rsp);

    int onSendNotificationComplete(OrderResult ret);

    int onModifyTicketComplete(OrderResult ret, List<ErrInTicket> errInTicketList);

    int onRegisterComplete(OrderResult ret, GetRegisterRsp rsp);

    int onGetSettingComplete(OrderResult ret, List<SettingList> settingLists);

    int onGetTableComplete(OrderResult ret, List<TablePro> settingLists);

    int onGetStoreInfo(OrderResult ret, GetStoreInfoRsp rsp);
}
