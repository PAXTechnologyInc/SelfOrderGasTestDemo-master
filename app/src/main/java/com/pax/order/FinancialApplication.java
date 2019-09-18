package com.pax.order;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pax.dal.IDAL;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.ecrsdk.factory.ITransAPI;
import com.pax.ecrsdk.factory.TransAPIFactory;
import com.pax.gl.page.PaxGLPage;
import com.pax.market.android.app.sdk.BaseApiService;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.db.OpenTicketDb;
import com.pax.order.db.OrderDetailDb;
import com.pax.order.db.PayDataDb;
import com.pax.order.db.PrintDataDb;
import com.pax.order.db.TransactionDb;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.PayData;
import com.pax.order.eventbus.Event;
import com.pax.order.logger.AppLog;
import com.pax.order.orderserver.Impl.OrderInstance;
import com.pax.order.orderserver.Impl.PictureDLManager;
import com.pax.order.orderserver.entity.LoginInfo;
import com.pax.order.orderserver.entity.registerDevice.RegisterInfoInstance;
import com.pax.order.print.Constants;
import com.pax.order.util.ActivityStack;
import com.pax.order.util.SecurityUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


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
 * 2018/8/7 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public class FinancialApplication extends GlobalVariable {
    public static final String TAG = "FinancialApplication";
    private static final long sMaxRefNum = 99999;

    private static final String appkey = "DY8EJRBZ9WKT6DNTYPU1";
    private static final String appSecret = "TSQSBOUESVQAVLPMF5TOEDLCHGT5EGUBYV7OLTI6";
    private String SN = Build.SERIAL;

    private static FinancialApplication sApp;
    // 获取IPPI常用接口
    private static IDAL sDal;

    private static OpenTicketDb sOpenTicketDbHelper;
    private static PrintDataDb sPrintDataDb;
    private static PayDataDb sPayDataDb;
    private static TransactionDb sTransactionDb;
    private static OrderDetailDb sOrderDetailDb;

    private static ITransAPI sITransAPI;
    private Handler mHandler;
    private ExecutorService mBackgroundExecutor;
    private boolean isReadyToUpdate = true;

    private static Controller controller;
    //register
    private String deviceId;
    private String token;
    private String reqEncryptKey;


    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
                mBackgroundExecutor = Executors.newFixedThreadPool(10, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                Thread thread = new Thread(runnable, "Background executor service");
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.setDaemon(true);
                return thread;
            }
        });
        initPaxStoreSdk();
        FinancialApplication.sApp = this;
        System.out.println("Init Called");
        init();
        System.out.println("InitData Called");
        initData();
        System.out.println("iniParam Called");
        iniParam();
        System.out.println("Controller instantiated");
        controller = Controller.getInstance();
        mHandler = new Handler();

    }

    public static ITransAPI getITransAPI() {
        sITransAPI = TransAPIFactory.createTransAPI();
        return sITransAPI;
    }

    private void init() {

        // 初始化日志
        AppLog.init("ORDER", false, true, "/sdcard/LogUtil.txt", this);
        if (BuildConfig.DEBUG) {
            AppLog.debug(true);
        } else {
            AppLog.debug(false);
        }

        // 获取IPPI常用接口
        try {
            sDal = NeptuneLiteUser.getInstance().getDal(getApp());
        } catch (Exception e) {
            AppLog.w(TAG, e);
            System.exit(0);
        }

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        String fcmTopic = pref.getString(ParamConstants.FCM_TOPIC, "TestWatch_QA");
        if ((fcmTopic != null) && (fcmTopic.length() > 0)) {
            FirebaseMessaging.getInstance().subscribeToTopic(fcmTopic);
        }
        // [END subscribe_topics]
        sOpenTicketDbHelper = OpenTicketDb.getInstance();
        sPrintDataDb = PrintDataDb.getInstance();
        sPrintDataDb.deletePrintData();
        sPayDataDb = PayDataDb.getInstance();
        sTransactionDb = TransactionDb.getInstance();
        sOrderDetailDb = OrderDetailDb.getInstance();

        PayData payData = FinancialApplication.getPayDataDb().readPayData();
        if (null != payData) {
            payData.setPayData(payData);
        }
    }

    private void iniParam() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        boolean firstRun = pref.getBoolean(ParamConstants.FIRST_RUN, true);
        if (firstRun) {
            SharedPreferences.Editor editor = pref.edit();
            Set<String> paymoduleSet = new HashSet<String>();
            paymoduleSet.add("POSDK");

            editor.putString(ParamConstants.USER_NAME, "PAXTestTerminal01");
            editor.putString(ParamConstants.PASS_WORD, "PAXTestTerminal01");
            editor.putString(ParamConstants.TOKEN, "PAXTestTerminal01");
            editor.putString(ParamConstants.TABLE_ID, "PAXTestTable01");
            editor.putString(ParamConstants.TERMINAL_ID, "PAXTestTerminal01");
            editor.putBoolean(ParamConstants.FIRST_RUN, false);
            editor.putString(ParamConstants.STANDBY_TIME, "30");
            editor.putLong(ParamConstants.ECRREFNUM, 1);
            editor.putString(ParamConstants.LOG_PWD, "12345678");
            editor.putString(ParamConstants.PRT_REMARK, "welcome to our restaurant!");
            editor.putStringSet(ParamConstants.PAY_MODULE_LIST, paymoduleSet);
            editor.putBoolean(ParamConstants.PRT_SWITCH, true);
            Map<ETermInfoKey, String> eTermInfoKey = sDal.getSys().getTermInfo();
            String model = eTermInfoKey.get(ETermInfoKey.MODEL);
            // todo: jianping change model
            model = "A920";
            System.out.println("THe model:"+model);
            if (model.equals("A930") || model.equals("A920") || model.equals("A920C")) {
                editor.putString(ParamConstants.PAY_MODULE, getString(R.string.posdk));
            } else {
                editor.putString(ParamConstants.PAY_MODULE, getString(R.string.poslink));
            }
            editor.apply();
        }
        setLoginInfo();
    }

    public static String getLogPwd() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        String logPwd = pref.getString(ParamConstants.LOG_PWD, "12345678");
        return logPwd;
    }

    public static void setLogPwd(String pwd) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ParamConstants.LOG_PWD, pwd);
        editor.apply();
    }

    public static long getEcrRefNum() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        long ecrRefNum = pref.getLong(ParamConstants.ECRREFNUM, 1);
        return ecrRefNum;
    }

    public static void ecrRefNumAdd() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        long ecrRefNum = pref.getLong(ParamConstants.ECRREFNUM, 1);
        if (ecrRefNum >= sMaxRefNum) {
            ecrRefNum = 0;
        }
        ecrRefNum++;
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(ParamConstants.ECRREFNUM, ecrRefNum);
        editor.apply();
    }

    private void initData() {
        runInBackground(new Runnable() {

            @Override
            public void run() {
                // 拷贝打印字体
                try {
                    install(FinancialApplication.getApp(), Constants.FONT_NAME, Constants.FONT_PATH);
                } catch (IOException e) {
                    AppLog.w(TAG, "", e);
                }
            }
        });
    }

    private void install(Context context, String name, String path) throws IOException {
        // using a  try-with-resources for JAVA7
        try (InputStream in = context.getAssets().open(name);
             FileOutputStream out = new FileOutputStream(new File(path + name))) {
            int count;
            byte[] tmp = new byte[1024];
            while ((count = in.read(tmp)) != -1) {
                out.write(tmp, 0, count);
            }
            Runtime.getRuntime().exec("chmod 777 " + path + name);
            out.flush();
        } catch (Exception e) {
            AppLog.e(TAG, "", e);
        }
    }

    public static LoginInfo getLoginInfo() {
        String username = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.USER_NAME, null);

        String password = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.PASS_WORD, null);

        String token = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.TOKEN, null);

        String tableId = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.TABLE_ID, null);

        String terminalid = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.TERMINAL_ID, null);

        if ((null != username) && (null != password) && (null != token) && (null != tableId) && (null != terminalid)) {
            LoginInfo info = new LoginInfo();
            info.setUserName(username);
            info.setPassword(password);
            info.setToken(token);
            info.setTableID(tableId);
            info.setTerminalID(terminalid);

            // todo: jianping change SN
            info.setTerminalSN(RegisterInfoInstance.getInstance().getSN());
//            info.setTerminalSN(sDal.getSys().readTUSN());
            return info;
        }

        return null;
    }

    public static void setLoginInfo() {
//        OrderInstance orderInterface = OrderInstance.getInstance();
//        orderInterface.setCallback(OrderProcComplete.getInstance());
//        orderInterface.initLoginInfo(getLoginInfo()); //不设置则使用默认参数
        System.out.println("Making Call to get Login Info");
    }

    public static String getPayModule() {
        String payModuleConfig = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.PAY_MODULE, null);
        if ((payModuleConfig == null) || (payModuleConfig.length() == 0)) {
            payModuleConfig = sApp.getString(R.string.posdk);
        }
        return payModuleConfig;
    }
//
//    public static void downloadGoodsdata() {
//        setLoginInfo();
//        GetAllCategoryManager getAllCategoryManager = new GetAllCategoryManager();
//        getAllCategoryManager.initLoginInfo(getLoginInfo(), OrderProcComplete.getInstance());
//        getAllCategoryManager.asyncGetAllCategoryInfo(FinancialApplication.getApp()
//                .getFilesDir()
//                .getPath() + ADPICDIR, FinancialApplication.getApp()
//                .getFilesDir()
//                .getPath() + GOODSPICDIR, FinancialApplication.getApp()
//                .getFilesDir()
//                .getPath() + CATEPICDIR);
//    }

    public static FinancialApplication getApp() {
        return sApp;
    }

    public static IDAL getDal() {
        return sDal;
    }


    public static PaxGLPage getGlPage() {
        return PaxGLPage.getInstance(FinancialApplication.getApp());
    }


    public static OpenTicketDb getOpenTicketDbHelper() {
        return sOpenTicketDbHelper;
    }

    public static PrintDataDb getPrintDataDb() {
        return sPrintDataDb;
    }

    public static PayDataDb getPayDataDb() {
        return sPayDataDb;
    }

    public static TransactionDb getTransactionDb() {
        return sTransactionDb;
    }

    public static OrderDetailDb getOrderDetailDb() {
        return sOrderDetailDb;
    }

    public static String getTerminalSN() {
//        return sDal.getSys().readTUSN();
        return sDal.getSys().getTermInfo().get(ETermInfoKey.SN);
    }

    public void runInBackground(final Runnable runnable) {
        mBackgroundExecutor.submit(runnable);
    }

    public void shutdownBackground() {
        mBackgroundExecutor.shutdown();
    }

    public void runOnUiThread(final Runnable runnable) {
        mHandler.post(runnable);
    }

    public void runOnUiThreadDelay(final Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

    public void register(Object obj) {
        if (!EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().register(obj);
        }
    }

    public void unregister(Object obj) {
        if (EventBus.getDefault().isRegistered(obj)) {
            EventBus.getDefault().unregister(obj);
        }
    }

    public void doEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    public static void finishAll() {
        PictureDLManager.getInstance().closeAllDLRunnable();
        FinancialApplication.getApp().shutdownBackground();

        FinancialApplication.getDal().getSys().enableNavigationBar(true);
        FinancialApplication.getDal().getSys().enableStatusBar(true);
        FinancialApplication.getDal().getSys().showNavigationBar(true);
        FinancialApplication.getDal().getSys().showStatusBar(true);

        ActivityStack.getInstance().popAll();
        System.exit(0);
    }

    private void initPaxStoreSdk() {
        StoreSdk.getInstance().init(this, appkey, appSecret, SN, new BaseApiService.Callback() {
            @Override
            public void initSuccess() {
                AppLog.i(TAG, "initSuccess.");
                initInquirer();
            }

            @Override
            public void initFailed(RemoteException e) {
                AppLog.i(TAG, "initFailed: " + e.getMessage());
//                Toast.makeText(getApplicationContext(), "Cannot get API URL from PAXSTORE, Please install PAXSTORE first.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initInquirer() {
        System.out.println("InitInquirer called");
        StoreSdk.getInstance().initInquirer(new StoreSdk.Inquirer() {
            @Override
            public boolean isReadyUpdate() {
                AppLog.i(TAG, "call business function....isReadyUpdate = " + isReadyToUpdate);
                if (isReadyToUpdate()) {
                    FinancialApplication.getDal().getSys().enableNavigationBar(true);
                    FinancialApplication.getDal().getSys().enableStatusBar(true);
                    FinancialApplication.getDal().getSys().showNavigationBar(true);
                    FinancialApplication.getDal().getSys().showStatusBar(true);
                }
                return isReadyToUpdate();
            }
        });
    }

    public boolean isReadyToUpdate() {
        return isReadyToUpdate;
    }

    public void setReadyToUpdate(boolean readyToUpdate) {
        isReadyToUpdate = readyToUpdate;
    }

    public static Controller getController() {
        return controller;
    }


    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getAppProcessName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        SharedPreferences.Editor editor = pref.edit();
        if (deviceId != null && !deviceId.isEmpty()) {
            editor.putString(ParamConstants.Device_ID, deviceId);
        }
        editor.apply();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
//        SharedPreferences.Editor editor = pref.edit();
//        if (token != null && !token.isEmpty()) {
//            editor.putString(ParamConstants.Token_ID, token);
//        }
//        editor.apply();
    }

    public String getReqEncryptKey() {
        return reqEncryptKey;
    }

    public void setReqEncryptKey(String reqEncryptKey) {
        this.reqEncryptKey = reqEncryptKey;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        SharedPreferences.Editor editor = pref.edit();
        if (reqEncryptKey != null && !reqEncryptKey.isEmpty()) {
            editor.putString(ParamConstants.Req_EncryKey, reqEncryptKey);
        }
        editor.apply();
    }

    private  void registerDevice(){

        deviceId = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.Device_ID, null);
        token = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.Token_ID, null);
        reqEncryptKey = PreferenceManager.getDefaultSharedPreferences(sApp)
                .getString(ParamConstants.Req_EncryKey, null);

        if((null == deviceId || deviceId.isEmpty()) || (null == token || token.isEmpty()) ||
                (null == reqEncryptKey || reqEncryptKey.isEmpty()))
        {
            Log.i(TAG,"!!!!!!START Register device!!!!");
//            OrderInstance orderInterface = OrderInstance.getInstance();
//            orderInterface.asyncRegisterDevice();
            System.out.println("Making Call to async register Device");
        }
        Log.i(TAG,"DeviceId:"+ getDeviceId()+"....Token:"+getToken());
    }


}
