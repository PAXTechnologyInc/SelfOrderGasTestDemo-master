/*
 * ============================================================================
 * = COPYRIGHT
 *     PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or
 *     nondisclosure agreement with PAX  Computer Technology(Shenzhen) CO., LTD.
 *     and may not be copied or disclosed except in accordance with the terms
 *     in that agreement.
 *          Copyright (C) 2018 -? PAX Computer Technology(Shenzhen) CO., LTD.
 *          All rights reserved.Revision History:
 * Date                      Author                        Action
 * 18-10-9 下午3:20           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;

import java.io.File;
import java.math.BigDecimal;

public class ClearCacheFragment extends SettingsBaseFragment implements View.OnClickListener {
    private Button mClearButtion;
    private TextView mCacheSize;

    public ClearCacheFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View contentView = View.inflate(getActivity(), R.layout.fragment_clear_cache, null);
        return contentView;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    private String getCacheSize() {
        try {
            return getFormatSize(getFolderSize(new File(
                    FinancialApplication.getApp().getCacheDir().getPath() + "/image_manager_disk_cache/")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClearButtion = (Button) view.findViewById(R.id.clear_cache);
        mClearButtion.setOnClickListener(this);

        mCacheSize = (TextView) view.findViewById(R.id.cache_size);
        mCacheSize.setText(getCacheSize());
        SettingsParamActivity mActivity;
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.clear_cache));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_cache:
                FinancialApplication.getApp().runInBackground(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getActivity()).clearDiskCache();
                    }
                });
                MProgressDialog.showProgress(getActivity(), "clear...", new MDialogConfig.Builder().setOnDialogDismissListener(
                        new OnDialogDismissListener() {
                            @Override
                            public void onDismiss() {
                                getActivity().onBackPressed();
                            }
                        }
                ).build(), 1000);
                break;
        }
    }
}
