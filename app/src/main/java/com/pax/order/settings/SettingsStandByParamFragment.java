/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2017 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 17-11-28 上午11:31  	     fengjl           	    Create/Add/Modify/Delete
 * ============================================================================
 *
 */

package com.pax.order.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.util.Tools;

/**
 * 商户参数
 *
 * @author Sim.G
 */
public class SettingsStandByParamFragment extends UnityStyleSettingBaseFragment {
    public static final String TAG = "SettingsStandByParamFragment";
    private static Activity sActivity;
    private static Preference sPref;
    //    private static String sNewValue = "";

    private static final int MAX_CNT = 1; // 当前页面上需要设置监听器的preference总个数
    private static int sCnt = 0; // 当页面启动之后，会调用每个preference的监听，设置cnt主要用来 屏蔽掉第一次进来的自动调用监听

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sActivity = getActivity();
        SettingsParamActivity mActivity = (SettingsParamActivity)sActivity;
        mActivity.setHeaderTitle(getResources().getString(R.string.settings_menu_standby_parameter));
    }

    @Override
    protected int getViewId() {
        return R.xml.settings_standby_para_pref;
    }

    @Override
    public void setupSimplePreferencesScreen() {
        bindPreferenceSummaryToValue(findPreference(ParamConstants.STANDBY_TIME));
    }


    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference
            .OnPreferenceChangeListener() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public boolean onPreferenceChange(final Preference preference, Object value) {
            String stringValue = value.toString();
            sPref = preference; // 保存句柄
            //            sNewValue = stringValue; // 保存值
            if (sCnt >= MAX_CNT) {
                //                String pref_key = preference.getKey();
                int maxLen = 0;
                boolean flag = false;

                if (flag) {
                    //                    final String oldValue = ((SettingsEditTextPreference) sPref).getText();
                    // 先检测输入数据长度的有效性
                    if (null == stringValue || stringValue.isEmpty() || !Tools.isNumeric(stringValue)) {
                        Toast.makeText(preference.getContext(), R.string.input_len_err, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    // 如果未修改按确定则直接退出
                    if (stringValue.equals(PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), null))) {
                        return false;
                    }
                } else {
                    if (null == stringValue || stringValue.isEmpty() || !Tools.isNumeric(stringValue)) {
                        Toast.makeText(preference.getContext(), R.string.input_len_err, Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    if (Integer.parseInt(stringValue) < 20) {
                        Toast.makeText(preference.getContext(), R.string.input_number_err, Toast.LENGTH_SHORT).show();
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
    };


    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), "30"));
    }

    @Override
    public void onResume() {
        super.onResume();
        sCnt = MAX_CNT; // onResume方法被调用之前，preference的监听器已经被调用了MAX_CNT次，所以以后的操作cnt不用再赋值
    }

    @Override
    public void onPause() {
        super.onPause();
        sCnt = 0; // 页面结束，将cnt置位
    }
}
