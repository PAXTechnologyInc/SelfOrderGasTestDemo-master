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
import com.pax.order.entity.OrderAmount;
import com.pax.order.entity.OrderInfo;
import com.pax.order.entity.Transaction;
import com.pax.order.orderserver.entity.DownloadJob;
import com.pax.order.orderserver.entity.LoginInfo;
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
import com.pax.order.orderserver.inf.DLPictureListener;
import com.pax.order.orderserver.inf.OnComplete;

import java.util.ArrayList;
import java.util.List;

import static com.pax.order.orderserver.constant.spRespCode.SP_RESPONSE_SUCCESS;
import static com.pax.order.orderserver.constant.spRespCode.TM_PIC_DOWNFAIL;



/*
* This file is not being used for the self order demo because it makes API calls.
*
*
* */


/**
 * Get different combination category manager
 */
public class GetAllCategoryManager {
    private static final String TAG = "GetAllCategoryManager";
//    private AsyncPostApiImpl mAsyncPostApiImpl;
    private OnComplete mOutComplete;
    private List<SpAdvertisement> mAdRsp;
    private List<Item> mItemRsp;
    private List<Category> mCateRsp;
    private int mProgressCount;
    private int mTotalJobCount;
    private boolean bAllDownIsSuccess;

    private List<DownloadJob> mDownloadJobList;
    private String mAdvertisementPath;
    private String mItemPath;
    private String mCategoryPath;

    public GetAllCategoryManager() {
        this.mProgressCount = 0;
        this.mTotalJobCount = 3;
//        this.mAsyncPostApiImpl = new AsyncPostApiImpl();
        mAdRsp = new ArrayList<>();
        mItemRsp = new ArrayList<>();
        mCateRsp = new ArrayList<>();
        bAllDownIsSuccess = true;
        mDownloadJobList = new ArrayList<>();
    }

    private synchronized int progressPlusPlus() {
        return ++mProgressCount;
    }

    private void runAdvertisement(String advertisementPath) {
        this.mTotalJobCount = 1;
        this.mAdvertisementPath = advertisementPath;
//        new AsyncThread(1).start();
    }

    private void runItem(String itemPath) {
        this.mTotalJobCount = 1;
        this.mItemPath = itemPath;
//        new AsyncThread(2).start();
    }

    private void runCategory(String categoryPath) {
        this.mTotalJobCount = 1;
        this.mCategoryPath = categoryPath;
//        new AsyncThread(3).start();
    }

    private void runALL(String advertisementPath, String itemPath, String categoryPath) {//waiter不需要广告，所以不下载了
        this.mTotalJobCount = 3;
        this.mAdvertisementPath = advertisementPath;
        this.mItemPath = itemPath;
        this.mCategoryPath = categoryPath;
//        new AsyncThread(4).start();
    }

//    private void dowmloadImage() {
//        progressPlusPlus();
//        mOutComplete.onProgress(0, mProgressCount, mTotalJobCount);
//        if (mProgressCount == mTotalJobCount) {
//            if ((mDownloadJobList != null) && !mDownloadJobList.isEmpty()) {
//                mPictureDLManager.init(mDLPictureListener);
//                mPictureDLManager.setDownloadJobList(mDownloadJobList);
//                mPictureDLManager.runAsyncDownload();
//            } else {
//                returnEnd();
//            }
//        }
//    }

//    class AsyncThread extends Thread {
//
//        private int mAsyncThreadType = 0;
//
//        public AsyncThread(int asyncThreadType) {
//            this.mAsyncThreadType = asyncThreadType;
//        }
//
//        @Override
//        public void run() {
//            bAllDownIsSuccess = true;
//            mAsyncPostApiImpl.setCallback(mInnerComplete);
//            mOutComplete.onProgress(0, mProgressCount, mTotalJobCount);
//            switch (mAsyncThreadType) {
//                case 1: {
//                    mAsyncPostApiImpl.asyncGetAdvertisement();
//                }
//                break;
//                case 2: {
//                    mAsyncPostApiImpl.asyncGetItem();
//                }
//                break;
//                case 3: {
//                    mAsyncPostApiImpl.asyncGetCategory();
//                }
//                break;
//                case 4: {
//                    //                    mAsyncPostApiImpl.asyncGetAdvertisement();
//                    mAsyncPostApiImpl.asyncGetItem();
//                    mAsyncPostApiImpl.asyncGetCategory();
//                }
//                break;
//                default:
//                    break;
//            }
//
//        }
//    }

    private void returnEnd() {
        if (mTotalJobCount == 3) {
            mOutComplete.onGetAllCategoryInfoComplete(mOrderResult, mAdRsp, mItemRsp, mCateRsp);
        } else if (mTotalJobCount == 1) {
            if (null != mAdRsp && !mAdRsp.isEmpty()) {
                mOutComplete.onGetAdvertisementComplete(mOrderResult, mAdRsp);
            }
            if (null != mItemRsp && !mItemRsp.isEmpty()) {
                mOutComplete.onGetItemComplete(mOrderResult, mItemRsp);
            }
            if (null != mCateRsp && !mCateRsp.isEmpty()) {
                mOutComplete.onGetCategoryComplete(mOrderResult, mCateRsp);
            }
        }
    }

    private OrderResult mOrderResult;
//    private PictureDLManager mPictureDLManager = PictureDLManager.getInstance();
    private DLPictureListener mDLPictureListener = new DLPictureListener() {
        @Override
        public void onDownloadSuccess() {
            returnEnd();
        }

        @Override
        public void onDownloadFailed(String errMsg) {
            mOrderResult.setResultCode(TM_PIC_DOWNFAIL);
            mOrderResult.setResultMessage("Download pictures failed:" + errMsg);
            returnEnd();
        }

        @Override
        public void onProgress(int succeedCount, int totalCount) {
            mOutComplete.onProgress(1, succeedCount, totalCount);
        }
    };

    private OnComplete mInnerComplete = new OnComplete() {
        @Override
        public int onVerifyLoginInfoComplete(OrderResult ret) {
            return 0;
        }

        @Override
        public int onGetAllCategoryInfoComplete(OrderResult ret, List<SpAdvertisement> adRsp, List<Item> itemRsp,
                                                List<Category> cateRsp) {
            return 0;
        }

        @Override
        public int onGetAdvertisementComplete(OrderResult ret, List<SpAdvertisement> rsp) {
            if (!bAllDownIsSuccess) {
                return 0;
            }

            if (!SP_RESPONSE_SUCCESS.equals(ret.getResultCode())) {
                if (mTotalJobCount == 1) {
                    mOutComplete.onGetAdvertisementComplete(ret, rsp);
                } else {
                    bAllDownIsSuccess = false;
                    mOutComplete.onGetAllCategoryInfoComplete(ret, rsp, mItemRsp, mCateRsp);
                }

                return 0;
            }

            mOrderResult = ret;
            mAdRsp = rsp;

            if (null != rsp && !rsp.isEmpty()) {
                for (SpAdvertisement spAdvertisement : rsp) {
                    String picUrl = spAdvertisement.getPictureUrl();
                    if (null != picUrl && !picUrl.isEmpty()) {
                        int lastIndex = picUrl.lastIndexOf("/");
                        if (null != mAdvertisementPath && !mAdvertisementPath.isEmpty() && -1 !=
                                lastIndex) {
                            String imageName = picUrl.substring(lastIndex + 1);
                            mDownloadJobList.add(new DownloadJob(picUrl, mAdvertisementPath, imageName));
                        }
                    }
                }
            }

//            dowmloadImage();
            return 0;
        }

        @Override
        public int onGetItemComplete(OrderResult ret, List<Item> rsp) {
            if (!bAllDownIsSuccess) {
                return 0;
            }

            if (!SP_RESPONSE_SUCCESS.equals(ret.getResultCode())) {
                if (mTotalJobCount == 1) {
                    mOutComplete.onGetItemComplete(ret, rsp);
                } else {
                    bAllDownIsSuccess = false;
                    mOutComplete.onGetAllCategoryInfoComplete(ret, mAdRsp, rsp, mCateRsp);
                }
                return 0;
            }

            mOrderResult = ret;
            mItemRsp = rsp;

            if (null != rsp && !rsp.isEmpty()) {
                for (Item item : rsp) {
                    String picUrl = item.getPicUrl();
                    if (null != picUrl && !picUrl.isEmpty()) {
                        int lastIndex = picUrl.lastIndexOf("/");
                        if (null != mItemPath && !mItemPath.isEmpty() && -1 !=
                                lastIndex) {
                            String imageName = picUrl.substring(lastIndex + 1);
                            mDownloadJobList.add(new DownloadJob(picUrl, mItemPath, imageName));
                        }
                    }
                }
            }

//            dowmloadImage();
            return 0;
        }

        @Override
        public int onGetCategoryComplete(OrderResult ret, List<Category> rsp) {
            if (!bAllDownIsSuccess) {
                return 0;
            }

            if (!SP_RESPONSE_SUCCESS.equals(ret.getResultCode())) {
                if (mTotalJobCount == 1) {
                    mOutComplete.onGetCategoryComplete(ret, rsp);
                } else {
                    bAllDownIsSuccess = false;
                    mOutComplete.onGetAllCategoryInfoComplete(ret, mAdRsp, mItemRsp, rsp);
                }
                return 0;
            }

            mOrderResult = ret;
            mCateRsp = rsp;

            if (null != rsp && !rsp.isEmpty()) {
                for (Category category : rsp) {
                    int picUrl = category.getPicUrl();
//                    if (null != picUrl && !picUrl.isEmpty()) {
//                        int lastIndex = picUrl.lastIndexOf("/");
//                        if (null != mCategoryPath && !mCategoryPath.isEmpty() && -1 !=
//                                lastIndex) {
//                            String imageName = picUrl.substring(lastIndex + 1);
//                            mDownloadJobList.add(new DownloadJob(picUrl, mCategoryPath, imageName));
//                        }
//                    }
                }
            }

//            dowmloadImage();
            return 0;
        }

        @Override
        public int onOpenTicketComplete(OrderResult ret, OpenTicket openTicket, OpenTicketRsp rsp) {
            return 0;
        }

        @Override
        public int onUploadTransComplete(OrderResult ret, Transaction rsp) {
            return 0;
        }

        @Override
        public int onUploadMultiTransComplete(OrderResult ret, List<Transaction> rsp) {
            return 0;
        }

        @Override
        public int onGetAllTableOrdersComplete(OrderResult ret, List<Table> tableList) {
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
            return 0;
        }

        @Override
        public int onModifyTicketComplete(OrderResult ret, List<ErrInTicket> errInTicketList) {
            return 0;
        }

        @Override
        public int onRegisterComplete(OrderResult ret, GetRegisterRsp rsp) {
            return 0;
        }

        @Override
        public int onGetSettingComplete(OrderResult ret, List<SettingList> settingLists) {
            return 0;
        }

        @Override
        public int onGetStoreInfo(OrderResult ret, GetStoreInfoRsp rsp) {
            return 0;
        }

        @Override
        public int onGetTableComplete(OrderResult ret, List<TablePro> settingLists) {
            return 0;
        }
    };


    public void initLoginInfo(LoginInfo loginInfo, OnComplete onComplete) {
//        this.mAsyncPostApiImpl.initLoginInfo(loginInfo);
        this.mOutComplete = onComplete;
    }

    public void asyncGetAllCategoryInfo(String advertisementPath, String itemPath, String categoryPath) {
        runALL(advertisementPath, itemPath, categoryPath);
    }

    public void asyncGetAdvertisement(String advertisementPath) {
        runAdvertisement(advertisementPath);
    }

    public void asyncGetItem(String itemPath) {
        runItem(itemPath);
    }

    public void asyncGetCategory(String categoryPath) {
        runCategory(categoryPath);
    }
}
