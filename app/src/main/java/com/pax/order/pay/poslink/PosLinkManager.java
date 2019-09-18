package com.pax.order.pay.poslink;

import com.pax.order.logger.AppLog;
import com.pax.poslink.CommSetting;
import com.pax.poslink.LogSetting;
import com.pax.poslink.PosLink;
import com.pax.poslink.poslink.POSLinkCreator;

import android.content.Context;

/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * yyyymmdd  	         xiazp           	Create/Add/Modify/Delete
 * ===========================================================================================
 */
public class PosLinkManager {
    private final String TAG = PosLinkManager.class.getSimpleName();
    private PosLink poslink;
    private Context mContext;
    private static PosLinkManager instance;

    private PosLinkManager(Context context){
        mContext = context;
        initPOSLink();
        initLog(context);
    }

    public static PosLinkManager getInstance(Context context) {
        //if(instance == null)
        {
            instance = new PosLinkManager(context);
        }
        return instance;
    }

    public PosLink getPoslink() {
        return poslink;
    }

    private void initPOSLink() {
        AppLog.i(TAG, "initPOSLink: ");

        poslink = POSLinkCreator.createPoslink(mContext);
        initLog(mContext);

        CommSetting commSetting = new CommSetting();
        commSetting.setType(CommSetting.USB);
        commSetting.setTimeOut("60000");
        poslink.SetCommSetting(commSetting);
        commSetting.setEnableProxy(true);

    }

    public  void initLog(Context context){
        //String LogOutputFile = getApplicationContext().getFilesDir().getAbsolutePath() + "/POSLog.txt";
        String LogOutputFile = context.getExternalFilesDir(null).getPath();
        LogSetting.setLogMode(true);
        LogSetting.setLevel(LogSetting.LOGLEVEL.DEBUG);
        LogSetting.setOutputPath(LogOutputFile);
    }


}
