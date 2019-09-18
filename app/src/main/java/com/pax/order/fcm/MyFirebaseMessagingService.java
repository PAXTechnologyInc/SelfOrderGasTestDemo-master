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
 * 2018/10/10 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.fcm;

import java.util.Map;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pax.order.FinancialApplication;
import com.pax.order.adver.GuideActivity;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.entity.NotificationMsg;
import com.pax.order.eventbus.FCMMessageEvent;
import com.pax.order.logger.AppLog;
import com.pax.order.menu.MenuActivity;
import com.pax.order.orderserver.entity.registerDevice.RegisterInfoInstance;
import com.pax.order.util.ActivityStack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFCMService";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        AppLog.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            AppLog.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob(remoteMessage.getData());
            } else {
                // Handle message within 10 seconds
                handleNow(remoteMessage.getData());
            }

            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            AppLog.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /*
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob(Map<String, String> data) {
        NotificationMsg notificationMsg = new NotificationMsg();
        notificationMsg.setTableID(data.get("TableID"));
        notificationMsg.setMessageType(data.get("MessageType"));
        notificationMsg.setTraceNum(data.get("TraceNum"));
        notificationMsg.setMessageNum(data.get("MessageNum"));
//        notificationMsg.setTerminalID(data.get("TerminalID"));
        notificationMsg.setDeviceID(data.get("DeviceID"));
        notificationMsg.setOrderNumber(data.get("OrderNumber"));
//        notificationMsg.setIsOpen(data.get("IsOpen"));
        notificationMsg.setTableStauts(data.get("TableStauts"));
        notificationMsg.setTableNum(data.get("TableNum"));
        String haveUnpaidOrder = data.get("IsHaveUnpaidOrder");
        if(haveUnpaidOrder != null && haveUnpaidOrder.equals("true")){
            notificationMsg.setHaveUnpaidOrder(true);
        }else {
            notificationMsg.setHaveUnpaidOrder(false);
        }

        AppLog.d(TAG, "strTableID:" + notificationMsg.getTableID());
        AppLog.d(TAG, "strMessageType:" + notificationMsg.getMessageType());
        AppLog.d(TAG, "strTraceNum:" + notificationMsg.getTraceNum());
        AppLog.d(TAG, "strMessageNum:" + notificationMsg.getMessageNum());
        AppLog.d(TAG, "strTerminalID:" + notificationMsg.getTerminalID());
//        AppLog.d(TAG, "strIsOpen:" + notificationMsg.getIsOpen());
        AppLog.d(TAG,"Device id:" + notificationMsg.getDeviceID());
        AppLog.d(TAG,"OrderNumber:"+notificationMsg.getOrderNumber());
        AppLog.d(TAG,"str tablestatus"+notificationMsg.getTableStauts());

        if (notificationMsg.getTableID() == null || notificationMsg.getMessageType() == null ||
              /*  notificationMsg.getTraceNum() == null ||*/ notificationMsg.getMessageNum() == null ||
                notificationMsg.getDeviceID() == null || notificationMsg.getTableStauts() == null) {
            AppLog.d(TAG, "data  err !");
            return;
        }

        String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null);
//        String DeviceId = FinancialApplication.getApp().getTerminalSN();
        String DeviceId =RegisterInfoInstance.getInstance().getDeviceSn();

        if (!notificationMsg.getTableNum().equals(tableNum)) {
            AppLog.d(TAG, "TableID  different !");
            AppLog.d(TAG, "notificationMsg TableID:" + notificationMsg.getTableID() + " local TableID:" + tableNum);
            return;
        }

        AppLog.d(TAG, "notificationMsg DeviceID:" + notificationMsg.getDeviceID());
        if (notificationMsg.getDeviceID().equals(DeviceId)) {
            AppLog.d(TAG, "local DeviceID  message !");
            FinancialApplication.getApp().doEvent(new FCMMessageEvent(FCMMessageEvent.Status.MSG_LOCAL, notificationMsg));
            return;
        }

        // MenuActivity未启动时特殊处理
        if (!ActivityStack.getInstance().find(MenuActivity.class)) {

            if (ActivityStack.getInstance().top() == null) {
                return;
            }

            if (ActivityStack.getInstance().top().getClass().equals(GuideActivity.class)) {
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }
        }

        FinancialApplication.getApp().doEvent(new FCMMessageEvent(FCMMessageEvent.Status.MSG_SERVER, notificationMsg));
        showNotifictionIcon(this);
    }

    /*
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow(Map<String, String> data) {
        AppLog.d(TAG, "Short lived task is done.");
    }

    private void sendNotification(String messageBody) {

        // 解析消息 判断消息类型、是否是该桌TableID
        Gson gson = new Gson();
        NotificationMsg notificationMsg = gson.fromJson(messageBody, NotificationMsg.class);
        if (notificationMsg == null) {
            return;
        }

        if (notificationMsg.getTableID() == null || notificationMsg.getMessageType() == null ||
                 notificationMsg.getMessageNum() == null ||
                notificationMsg.getDeviceID() == null || notificationMsg.getTableStauts() == null) {
            AppLog.d(TAG, "data  err !");
            return;
        }

        String tableId = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_ID, null);
//        String terminalID = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
//                .getString(ParamConstants.TERMINAL_ID, null);
        String deviceId = FinancialApplication.getApp().getTerminalSN();

        if (!notificationMsg.getTableID().equals(tableId)) {
            AppLog.d(TAG, "TableID  different !");
            AppLog.d(TAG, "notificationMsg TableID:" + notificationMsg.getTableID() + " local TableID:" + tableId);
            return;
        }

        AppLog.d(TAG, "notificationMsg TerminalID:" + notificationMsg.getDeviceID());
        if (notificationMsg.getDeviceID().equals(deviceId)) {
            AppLog.d(TAG, "local terminalID  message !");
            FinancialApplication.getApp().doEvent(new FCMMessageEvent(FCMMessageEvent.Status.MSG_LOCAL, notificationMsg));
            return;
        }

        FinancialApplication.getApp().doEvent(new FCMMessageEvent(FCMMessageEvent.Status.MSG_SERVER, notificationMsg));
        showNotifictionIcon(this);
    }

    public static void showNotifictionIcon(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //        Intent intent = new Intent(context, XXXActivity.class);//将要跳转的界面
        Intent intent = new Intent();//只显示通知，无页面跳转
        builder.setAutoCancel(true);//点击后消失
        builder.setSmallIcon(R.mipmap.app_notifiction1_icon);//设置通知栏消息标题的头像
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);//设置通知铃声
        builder.setTicker("Modify Ticket");
        builder.setContentTitle("Modify Ticket");
        builder.setContentText("Modify Ticket");//通知内容
        //利用PendingIntent来包装我们的intent对象,使其延迟跳转
        PendingIntent intentPend = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(intentPend);
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
