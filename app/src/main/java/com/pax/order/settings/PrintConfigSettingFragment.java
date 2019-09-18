/*
 *
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
 * 18-12-27 下午3:14           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;

public class PrintConfigSettingFragment extends UnityStyleSettingBaseFragment {
    private static final int MAX_CNT = 1; // 当前页面上需要设置监听器的preference总个数
    private static int sCnt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsParamActivity mActivity;
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.prn_config));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.xml.setting_print_config;
    }

    @Override
    public void setupSimplePreferencesScreen() {
//        bindPreferenceSummaryToValue(findPreference(ParamConstants.STORE_NAME));
        bindPreferenceSummaryToValue(findPreference(ParamConstants.PRT_REMARK));
//        bindPreferenceSummaryToValue(findPreference(ParamConstants.PRT_SWITCH));
    }

    public void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        if (preference instanceof SwitchPreference) {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext()).getBoolean(preference.getKey(), true));
        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
                    .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
        }
    }

    private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference
            .OnPreferenceChangeListener() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            if (preference.getKey().equals(ParamConstants.PRT_SWITCH)) {
                PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp()).edit()
                        .putBoolean(ParamConstants.PRT_SWITCH, (Boolean) value);
                return true;
            } else {
                String stringValue = value.toString();
                //            sNewValue = stringValue; // 保存值
                if (sCnt >= MAX_CNT) {
                    //                String pref_key = preference.getKey();
                    int maxLen = 0;
                    boolean flag = false;

                    if (flag) {
                        //                    final String oldValue = ((SettingsEditTextPreference) sPref).getText();
                        // 先检测输入数据长度的有效性
                        if (null == stringValue || stringValue.isEmpty()) {
                            Toast.makeText(preference.getContext(), R.string.input_len_err, Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        // 如果未修改按确定则直接退出
                        if (stringValue.equals(PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                .getString(preference.getKey(), null))) {
                            return false;
                        }
                    } else {
                        if (null == stringValue) {
                            Toast.makeText(preference.getContext(), R.string.input_len_err, Toast.LENGTH_SHORT).show();
                            return false;
                        }

                        preference.setSummary(stringValue);
                    }

                } else {
                    // 页面加载时，会自动调用每个preference的监听，这里只设置summary,不做其他处理
                    sCnt++;
                    preference.setSummary(stringValue);
                }
                return true;
            }
        }
    };

}
