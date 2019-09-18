package com.pax.order.downloadparam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pax.order.logger.AppLog;

public class DownloadParmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AppLog.i("DownloadParamReceiver", "broadcast received");
        context.startService(new Intent(context, DownloadParamService.class));
    }
}
