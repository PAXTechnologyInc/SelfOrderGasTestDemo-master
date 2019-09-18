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
package com.pax.order.orderserver.Impl.retrofit.api;

import com.pax.order.orderserver.entity.baseModel.BaseReqModel;
import com.pax.order.orderserver.entity.baseModel.BaseRspModel;
import com.pax.order.orderserver.entity.getAllTableInfo.GetAllTableInfoReq;
import com.pax.order.orderserver.entity.getAllTableInfo.GetAllTableInfoRsp;
import com.pax.order.orderserver.entity.getEmployee.GetEmployeeReq;
import com.pax.order.orderserver.entity.getEmployee.GetEmployeeRsp;
import com.pax.order.orderserver.entity.getadvertisement.GetAdvertisementReq;
import com.pax.order.orderserver.entity.getadvertisement.GetAdvertisementRsp;
import com.pax.order.orderserver.entity.getcategory.GetCategoryReq;
import com.pax.order.orderserver.entity.getcategory.GetCategoryRsp;
import com.pax.order.orderserver.entity.getitem.GetItemReq;
import com.pax.order.orderserver.entity.getitem.GetItemRsp;
import com.pax.order.orderserver.entity.getorderamount.GetOrderAmountReq;
import com.pax.order.orderserver.entity.getorderamount.GetOrderAmountRsp;
import com.pax.order.orderserver.entity.getorderdetail.GetOrderDetailReq;
import com.pax.order.orderserver.entity.getorderdetail.GetOrderDetailRsp;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoReq;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;
import com.pax.order.orderserver.entity.gettable.GetTableReq;
import com.pax.order.orderserver.entity.gettable.GetTableRsp;
import com.pax.order.orderserver.entity.gettableorders.GetTableOrdersReq;
import com.pax.order.orderserver.entity.gettableorders.GetTableOrdersRsp;
import com.pax.order.orderserver.entity.modifyTicket.ModifyTicketReq;
import com.pax.order.orderserver.entity.modifyTicket.ModifyTicketRsp;
import com.pax.order.orderserver.entity.openticket.OpenTicketReq;
import com.pax.order.orderserver.entity.openticket.OpenTicketRsp;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterReq;
import com.pax.order.orderserver.entity.registerDevice.GetRegisterRsp;
import com.pax.order.orderserver.entity.sendnotification.SendNotificationReq;
import com.pax.order.orderserver.entity.sendnotification.SendNotificationRsp;
import com.pax.order.orderserver.entity.setting.GetSettingReq;
import com.pax.order.orderserver.entity.setting.GetSettingRsp;
import com.pax.order.orderserver.entity.uploadmultitrans.UploadMultiTransReq;
import com.pax.order.orderserver.entity.uploadmultitrans.UploadMultiTransRsp;
import com.pax.order.orderserver.entity.uploadtrans.UploadTransReq;
import com.pax.order.orderserver.entity.uploadtrans.UploadTransRsp;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit request interface
 */
public interface HttpService {
    /**
     * post_GetAdvertisement
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/ProductAPI/GetAdvertisement")
    //@POST("api/CategoryApi/GetAdvertisement")
    Observable<GetAdvertisementRsp> post_GetAdvertisement(@Body GetAdvertisementReq req);

    /***
     * post_GetItem
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/ProductAPI/GetProduct")
    //@POST("api/CategoryApi/GetItem")
    Observable<GetItemRsp> post_GetItem(@Body GetItemReq req);

    /**
     * post_GetCategory
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/ProductAPI/GetCategory")
//    @POST("api/CategoryApi/GetCategory")
    Observable<GetCategoryRsp> post_GetCategory(@Body GetCategoryReq req);

    /**
     * post_OpenTicket
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/OrderAPI/OpenOrder")
    //@POST("api/PaymentApi/OpenTicket")
    Observable<OpenTicketRsp> post_OpenTicket(@Body OpenTicketReq req);

    /**
     * post_UploadTrans
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/PaymentAPI/UploadTrans")
//    @POST("api/PaymentApi/TerminalUploadTrans")
    Observable<UploadTransRsp> post_UploadTrans(@Body UploadTransReq req);

    /**
     * post_UploadMultiTrans
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/PaymentAPI/UploadMultiTrans")
    //@POST("api/PaymentApi/TerminalUploadMultiTrans")
    Observable<UploadMultiTransRsp> post_UploadMultiTrans(@Body UploadMultiTransReq req);

    /**
     * post_GetTableOrders
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/OrderAPI/GetTableOrders")
//    @POST("api/PaymentApi/GetTableOrders")
    Observable<GetTableOrdersRsp> post_GetTableOrders(@Body GetTableOrdersReq req);

    /**
     * post_GetOrderAmount
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/OrderAPI/GetOrderAmount")
//    @POST("api/PaymentApi/GetOrderAmount")
    Observable<GetOrderAmountRsp> post_GetOrderAmount(@Body GetOrderAmountReq req);

    /**
     * post_GetOrderDetail
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/OrderAPI/GetOrderDetail")
//    @POST("api/PaymentApi/GetOrderDetail")
    Observable<GetOrderDetailRsp> post_GetOrderDetail(@Body GetOrderDetailReq req);

    /**
     * downloadPicFromNet
     *
     * @param fileUrl request url
     * @return nothing
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadPicFromNet(@Url String fileUrl);

//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    @POST("{subURL}")
//    Call<ResponseBody> post_JsonRequest(@Path("subURL") String subURL, @Body RequestBody req);

    /**
     * post_GetEmployee
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/EmployeeAPI/GetEmployee")
//    @POST("api/CategoryApi/GetEmployee")
    Observable<GetEmployeeRsp> post_GetEmployee(@Body GetEmployeeReq req);

    /**
     * post_GetAllTableInfo
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/ POSAPI/GetTable")
//    @POST("api/CategoryApi/GetTable")
    Observable<GetAllTableInfoRsp> post_GetAllTableInfo(@Body GetAllTableInfoReq req);

    /**
     * SendNotification
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/POSAPI/SendNotification")
//    @POST("api/TableServiceApi/SendNotification")
    Observable<SendNotificationRsp> post_SendNotification(@Body SendNotificationReq req);

    /**
     * modify ticket
     *
     * @param req request body
     * @return nothing
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/OrderAPI/ModifyOrder")
//    @POST("api/PaymentApi/ModifyTicket")
    Observable<ModifyTicketRsp> post_ModifyTicket(@Body ModifyTicketReq req);

    /**
     * register
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/POSAPI/Register")
    Observable<GetRegisterRsp> post_RegisterDevice(@Body GetRegisterReq req);

    /**
     * unregister
     */
  //  @Headers({"Content-Type: application/json", "Accept: application/json"})
   // @POST("/V2/API/ Restaurant/POSAPI/UnRegister")
    //Observable<GetUnregisterRsp> post_RegisterDevice(@Body GetRegisterReq req);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/POSAPI/GetSettings")
    Observable<GetSettingRsp> post_GetSetting(@Body GetSettingReq req);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/POSAPI/GetTable")
    Observable<GetTableRsp> post_GetTable(@Body GetTableReq req);


    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("V2/API/Restaurant/POSAPI/GetStoreInformation")
    Observable<GetStoreInfoRsp> post_GetStoreInfo(@Body GetStoreInfoReq req );


}
