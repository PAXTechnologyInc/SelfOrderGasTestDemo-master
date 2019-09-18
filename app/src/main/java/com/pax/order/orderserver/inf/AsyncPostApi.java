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
import com.pax.order.entity.Transaction;
import com.pax.order.orderserver.entity.LoginInfo;
import com.pax.order.orderserver.entity.modifyTicket.ModifyItemReq;
import com.pax.order.orderserver.entity.modifyTicket.OpenItemReq;

import java.util.List;

/**
 * interface of async post request
 */
public interface AsyncPostApi {

    /**
     * verify login information
     *
     * @param userName user name in cloud server
     * @param password user's password
     * @param token    user's token
     * @return 0: send async request success; other: failure
     */
    int asyncVerifyLoginInfo(String userName, String password, String token);

    /**
     * Init user and terminal information parameters, other interfaces will use theses information
     *
     * @param loginInfo login information including usr name, password and token.
     * @return 0: success; other: failure
     */
    int initLoginInfo(LoginInfo loginInfo);

//    /**
//     * set picture download mode in asyncGetAllCategoryInfo, asyncGetAdvertisement, asyncGetItem and asyncGetCategory
//     *
//     * @param isKeepExistFiles true: keep exist files in download jobs; false: delete exist files and download again
//     *                         if this function is never implemented, run as false.
//     * @return 0: success; other: failure
//     */
//    int setIfKeepExistFiles(boolean isKeepExistFiles);
//
//    /**
//     * Get advertisement, item, and category information, and download advertisement, item and category pictures.
//     *
//     * @param advertisementPath local path to store advertisement pictures.
//     * @param itemPath          local path to store item pictures.
//     * @param categoryPath      local path to store category pictures.
//     * @return 0: send async request success; other: failure
//     */
//    int asyncGetAllCategoryInfo(String advertisementPath, String itemPath, String categoryPath);

    /**
     * Get advertisements information and save pictures to specified path,
     * file name will the same with it's url suffix name
     *
     * @return 0: send async request success; other: failure
     */
    int asyncGetAdvertisement();

    /**
     * Get items information and save pictures to specified path, file name will the same with it's url suffix name
     *
     * @return 0: send async request success; other: failure
     */
    int asyncGetItem();

    /**
     * Get categories information and save pictures to specified path, file name will the same with it's url suffix name
     *
     * @return 0: send async request success; other: failure
     */
    int asyncGetCategory();

    /**
     * Make an order
     *
     * @param req order request information
     * @return 0: send async request success; other: failure
     */
    int asyncOpenTicket(OpenTicket req);

    /**
     * Upload paid transaction to server
     *
     * @param req transaction details
     * @return 0: send async request success; other: failure
     */
    int asyncUploadTrans(Transaction req, String serialNumber);

    /**
     * Upload paid transactions to server
     *
     * @param req multi transactions details
     * @return 0: send async request success; other: failure
     */
    int asyncUploadMultiTrans(List<Transaction> req, String serialNumber);

    /**
     * Get all table's orders in restaurant
     *
     * @param isOnlyShowOpened true: only return the tables which have opening order(s) false:
     *                         return all table information
     * @param isByTableID      true: only return the table order(s)
     *                         false: return all table order(s)
     * @return 0: send async request success; other: failure
     */
    int asyncGetAllTableOrders(boolean isOnlyShowOpened, boolean isByTableID);

    /**
     * Get this terminal/table's opening orders (unpaid orders)
     *
     * @return 0: send async request success; other: failure
     */
    int asyncGetUnpaidOrders();

    /**
     * Get specified order's amount information of this table/terminal
     *
     * @param traceNum null: return latest unpaid record of this table/terminal
     * @return 0: send async request success; other: failure
     */
    int asyncGetOrderAmount(String traceNum);

    /**
     * Get specified order's detail information of this table/terminal
     *
     * @param traceNum null: return latest unpaid record of this table/terminal
     * @return 0: send async request success; other: failure
     */
    int asyncGetOrderDetail(String traceNum);

    int asyncGetEmployee();

    int asyncGetAllTableInfo();

    /**
     * Send notification to cloud server
     *
     * @param sendType 1: call waiter
     * @return 0: send async request success; other: failure
     */
    int asyncSendNotification(String sendType);

    int asyncModifyTicket(String TraceNum, String ModifyType, String discountAmt,
                          String serialNumber,
                          List<ModifyItemReq> modifyItemList,
                          List<OpenItemReq> openItemList);

    int  asyncRegisterDevice();
    int  asyncUnregisterDevice();
    int asyncGetSettings();
    int asyncGetTable();
    int asyncGetStoreInfo();
}
