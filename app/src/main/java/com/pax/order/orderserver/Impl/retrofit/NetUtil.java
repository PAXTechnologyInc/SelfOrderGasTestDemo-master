package com.pax.order.orderserver.Impl.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chenyr on 2018/12/17.
 */

public class NetUtil {

    /**
     * 判断是否有可用的网络连接
     */
    /*
     * 只打开3G连接的时候：
	 * 0===类型===MOBILE
	 * 0===状态===CONNECTED
	 * 打开wifi连接和3G连接的时候：
	 * 0===类型===MOBILE
	 * 0===状态===DISCONNECTED
	 * 1===类型===WIFI
	 * 1===状态===CONNECTED
	 * */
    public static boolean isNetworkConnected(Context context) {

        boolean netstate = false;

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {
            NetworkInfo[] networkInfo = manager.getAllNetworkInfo();
            if (networkInfo != null) {
                for (int i = 0; i < networkInfo.length; i++) {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }
}
