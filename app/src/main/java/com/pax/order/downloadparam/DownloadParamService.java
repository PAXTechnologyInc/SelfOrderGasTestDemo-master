package com.pax.order.downloadparam;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.pax.dal.entity.ETermInfoKey;
import com.pax.market.android.app.sdk.StoreSdk;
import com.pax.market.api.sdk.java.base.constant.ResultCode;
import com.pax.market.api.sdk.java.base.dto.DownloadResultObject;
import com.pax.market.api.sdk.java.base.exception.NotInitException;
import com.pax.market.api.sdk.java.base.exception.ParseXMLException;
import com.pax.order.BuildConfig;
import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.logger.AppLog;
import com.pax.order.util.ToastUtils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DownloadParamService extends Service {

    private static final String TAG = DownloadParamService.class.getSimpleName();
    public static String saveFilePath;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DownloadParamService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        saveFilePath = getFilesDir() + "/YourPath/";

        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showMessage(FinancialApplication.getApp(), "Param Update Start");
            }
        });
        FinancialApplication.getApp().runInBackground(new Runnable() {
            @Override
            public void run() {
                DownloadResultObject downloadResult = null;
                try {
                    AppLog.i(TAG, "call sdk API to download parameter");
                    downloadResult = StoreSdk.getInstance().paramApi().downloadParamToPath(getApplication()
                            .getPackageName(), BuildConfig.VERSION_CODE, saveFilePath);
                    AppLog.i(TAG, downloadResult.toString());
                } catch (NotInitException e) {
                    AppLog.e(TAG, "e:" + e);
                    return;
                }

                if (downloadResult.getBusinessCode() == ResultCode.SUCCESS) {
                    AppLog.i(TAG, "download successful.");
                    handleSuccess();
                } else {
                    AppLog.e(TAG, "ErrorCode: " + downloadResult.getBusinessCode() + "ErrorMessage: " + downloadResult.getMessage());
                }

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void parseParamFile(File parameterFile) {
        try {
            //parse the download parameter xml file for display.
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
            SharedPreferences.Editor editor = pref.edit();
            HashMap<String, String> resultMap = StoreSdk.getInstance().paramApi().parseDownloadParamXml(parameterFile);

            if (resultMap != null && resultMap.size() > 0) {
                //convert result map to list for ListView display.
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    AppLog.i(TAG, " KEY: " + entry.getKey() + "  VALUE: " + entry.getValue());
                    if (entry.getKey().equals(ParamConstants.STORE_NAME)) {
                        if (entry.getValue().isEmpty()) {
                            AppLog.i(TAG, entry.getKey() + "...empty");
                            continue;
                        }
                    }
                    if (entry.getKey().equals(ParamConstants.STANDBY_TIME)) {
                        if ((entry.getValue().isEmpty()) || (Integer.parseInt(entry.getValue()) < 20)) {
                            AppLog.i(TAG, entry.getKey() + "...empty");
                            continue;
                        }
                    }
                    if (entry.getKey().equals(ParamConstants.PRT_SWITCH)) {
                        if (entry.getValue().equals("N")) {
                            editor.putBoolean(entry.getKey(), false);
                        } else {
                            editor.putBoolean(entry.getKey(), true);
                        }
                        continue;
                    }
                    editor.putString(entry.getKey(), entry.getValue());
                }
                editor.apply();
                FinancialApplication.setLoginInfo();
                // parameterFile.delete();
            }

            //save result for demo display
        } catch (NotInitException e) {
            AppLog.e(TAG, "e:" + e);
        } catch (ParseXMLException e) {
            AppLog.e(TAG, "parse xml failed: " + e.getMessage());
        }

    }

    public void parsePayModuleXml(File file) throws ParseXMLException {
        final String keyType = "type";
        final String keySelect = "select";
        Set<String> hsList = new HashSet<String>();

        Map<ETermInfoKey, String> eTermInfoKey = FinancialApplication.getDal().getSys().getTermInfo();
        String model = eTermInfoKey.get(ETermInfoKey.MODEL);
        AppLog.i(TAG, "termType：   " + model);
        if (file != null) {
            try {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(file);
                Element root = document.getRootElement();
                Element memberElm = root.element(model);
                if (memberElm == null) {
                    AppLog.i(TAG, model + "...XML no element.....");
                    memberElm = root.element("default");
                    if (memberElm == null) {
                        AppLog.i(TAG, "XML no element.....");
                        return;
                    }
                }
                List<Element> lst = memberElm.elements("Paymodule");
                SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
                SharedPreferences.Editor edt = spf.edit();
                for (Element et : lst) {
                    AppLog.i(TAG, et.getName());
                    for (Element emt : (List<Element>) et.elements()) {
                        AppLog.i(TAG, emt.getName() + "..." + emt.getText() + "  ");
                        if (emt.getName().equals(keyType)) {
                            hsList.add(emt.getTextTrim());
                        }
                        if (emt.getName().equals(keySelect)) {
                            final String[] posPayTable = {"POSDK", "POSLINK", "EDC"};
                            if (emt.getTextTrim().equals(posPayTable[2])) {
                                edt.putString(ParamConstants.PAY_MODULE, getString(R.string.easylink));
                            } else if (emt.getTextTrim().equals(posPayTable[1])) {
                                edt.putString(ParamConstants.PAY_MODULE, getString(R.string.poslink));
                            } else {
                                edt.putString(ParamConstants.PAY_MODULE, getString(R.string.posdk));
                            }
                        }
                    }
                }
                edt.putStringSet(ParamConstants.PAY_MODULE_LIST, hsList);
                edt.apply();
            } catch (Exception e) {
                AppLog.i(TAG, "XML EXCEPTION" + e);
                throw new ParseXMLException(e);
            }
        } else {
            AppLog.i(TAG, "parseDownloadParamXml: file is null, please make sure the file is correct.");
        }
    }

    private void parseResFile(File resFile) {
        String fileName = null;
        try {
            HashMap<String, String> resultMap = StoreSdk.getInstance().paramApi().parseDownloadParamXml(resFile);

            if (resultMap != null && resultMap.size() > 0) {
                for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                    AppLog.i(TAG, " resKEY: " + entry.getKey() + "  resVALUE: " + entry.getValue());
                    if (entry.getKey().equals(ParamConstants.PRTLOGO_FILENAME)) {
                        fileName = entry.getValue();

                        if (!fileName.isEmpty()) {
                            // String imageDir = getCacheDir() + "/pax_logo_normal.png";
                            File cachedImage = new File(getCacheDir(), "prnImage");
                            if (!cachedImage.isDirectory()) {
                                cachedImage.mkdirs();
                            }
                            File imageDir = new File(cachedImage, "pax_logo_normal.png");
                            String srcDir = saveFilePath + fileName;
                            FileInputStream fis = null;
                            FileOutputStream fos = null;

                            AppLog.i(TAG, "PRT LOG FILE NAME..." + fileName);
                            try {
                                byte datas[] = new byte[1024 * 4];
                                int len = 0;
                                fis = new FileInputStream(srcDir);
                                fos = new FileOutputStream(imageDir);
                                while ((len = fis.read(datas)) != -1) {
                                    fos.write(datas, 0, len);
                                }
                            } catch (Exception e) {
                                AppLog.i(TAG, "prt image file exception....");
                            } finally {
                                try {
                                    if (fis != null) fis.close();
                                    if (fos != null) fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                    if (entry.getKey().equals(ParamConstants.PAY_MODULE)) {
                        fileName = entry.getValue();
                        if (!fileName.isEmpty()) {
                            AppLog.i(TAG, "PAY MODULE FILE NAME   " + fileName);
                            File moduleFile = new File(saveFilePath + fileName);
                            parsePayModuleXml(moduleFile);
                        }
                    }
                }
            }
            //save result for demo display
        } catch (NotInitException e) {
            AppLog.e(TAG, "e:" + e);
        } catch (ParseXMLException e) {
            AppLog.e(TAG, "parse xml failed: " + e.getMessage());
        }

    }

    private void handleSuccess() {
        //file download to saveFilePath above.
        File parameterFile = null;
        File resFile = null;
        File[] filelist = new File(saveFilePath).listFiles();
        if (filelist != null && filelist.length > 0) {
            for (File f : filelist) {
                if (DownConstants.DOWNLOAD_PARAM_FILE_NAME.equals(f.getName())) {
                    parameterFile = f;
                } else if (DownConstants.DOWNLOAD_RES_FILE_NAME.equals(f.getName())) {
                    resFile = f;
                }
            }
            if (parameterFile != null) {
                parseParamFile(parameterFile);
            } else {
                AppLog.i(TAG, "parameterFile is null ");
            }
            if (resFile != null) {
                parseResFile(resFile);
            } else {
                AppLog.i(TAG, "resFile is null ");
            }

        }
        FinancialApplication.getApp().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AppLog.i(TAG, "Param Update Compelete");
                ToastUtils.showMessage(FinancialApplication.getApp(), "Param Update Compelete");
            }
        });
    }

}
