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
 * 18-12-24 上午10:33           wangxf                 Create/Add/Modify/Delete
 * ============================================================================
 *
 *
 */

package com.pax.order.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.commonui.widget.MySpinner;
import com.pax.order.logger.AppLog;

import java.util.Set;

public class PayModuleSettingFragment extends SettingsBaseFragment {
    private static final String TAG = "PayModuleSettingFragment";
    MySpinner mSpinner;
    private String[] payPara =
            {
                    "POSDK", "POSLINK", "EDC"
            };
    private String[] payModule;
    private ArrayAdapter<String> mStringArrayAdapter;
    private boolean isFirstEnter = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Set<String> stringSet = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getStringSet(ParamConstants.PAY_MODULE_LIST, null);
        if ((stringSet != null) && (stringSet.size() > 0)) {
            payModule = new String[stringSet.size()];
            int i = 0;
            for (String moduleValue : stringSet) {
                payModule[i] = moduleValue;
                i++;
            }
        } else {
            payModule = new String[]{"POSDK"};
        }
        View contentView = View.inflate(getActivity(), R.layout.fragment_pay_setting, null);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        int selIndex = getSelectionIndex();
        super.onViewCreated(view, savedInstanceState);

        SettingsParamActivity mActivity;
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.pay_setting));
        mSpinner = (MySpinner) view.findViewById(R.id.paysetting_spinner);

        mStringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, payModule);

        mStringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mStringArrayAdapter);
        mSpinner.setSelection(selIndex, true);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AppLog.d(TAG, "onItemSelected: ");
                if (isFirstEnter) {
                    //do nothing
                    isFirstEnter = false;
                } else {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
                    SharedPreferences.Editor editor = pref.edit();

                    if (parent.getItemAtPosition(position).toString().toUpperCase().equals(payPara[0])) {
                        editor.putString(ParamConstants.PAY_MODULE, getString(R.string.posdk));
                    } else if (parent.getItemAtPosition(position).toString().toUpperCase().equals(payPara[1])) {
                        editor.putString(ParamConstants.PAY_MODULE, getString(R.string.poslink));
                    } else if (parent.getItemAtPosition(position).toString().toUpperCase().equals(payPara[2])) {
                        editor.putString(ParamConstants.PAY_MODULE, getString(R.string.easylink));
                    } else {
                        editor.putString(ParamConstants.PAY_MODULE, getString(R.string.posdk));
                    }
                    editor.apply();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        mSpinner.setVisibility(View.VISIBLE);
    }

    private int getSelectionIndex() {
        int ret = 0;
        String payModuleConfig = null;

        if (payModule.length > 1) {
            payModuleConfig = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                    .getString(ParamConstants.PAY_MODULE, null);
            if (payModuleConfig != null && !payModuleConfig.isEmpty()) {
                if (payModuleConfig.equals(getString(R.string.posdk))) {
                    payModuleConfig = payPara[0];
                } else if (payModuleConfig.equals(getString(R.string.poslink))) {
                    payModuleConfig = payPara[1];
                } else if (payModuleConfig.equals(getString(R.string.easylink))) {
                    payModuleConfig = payPara[2];
                }
                for (int i = 0; i < payModule.length; i++) {
                    if (payModuleConfig.equals(payModule[i])) {
                        ret = i;
                        break;
                    }
                }
            }
        }
        return ret;
    }

}
