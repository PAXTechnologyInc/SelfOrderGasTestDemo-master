package com.pax.order.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

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
 * 2018/10/23 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public class InternetCheckUtils {
    private static boolean isConnectingToInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE); //获取系统服务
        if (manager != null) {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (info[i].isConnected()) {  //判断网络是否已连接
                            return true;  //网络已连接
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    private static boolean isNetworkOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("ping -c 1 114.114.114.114");//8.8.8.8
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isWifiConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected() && mWifi.isAvailable();
    }

    public static boolean checkWifiConnect(Context context) {
        boolean isConnected;
        isConnected = isWifiConnect(context);
        return isConnected;
    }

    public static boolean checkConnectingConnect(Context context) {
        return true;
//        if (isConnectingToInternet(context) && isNetworkOnline()) {
//            return true;
//        }
//        ToastUtils.showMessage(context, "Internet is NOT connected yet");
//        return false;
    }
}
