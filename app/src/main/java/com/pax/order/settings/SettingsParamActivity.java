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

import java.util.List;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsParamActivity extends SettingsHeaderPrefActivity implements OnClickListener {
    private ImageView mBackBtn;
    private TextView mHeaderView;
    private static PreferenceActivity.Header sHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        initViews();
        setListeners();
    }

    @Override
    public void onHeaderClick(PreferenceActivity.Header header, int position) {
        super.onHeaderClick(header, position);
        SettingsEditTextPreference.setPositionOfPrefActHeader(position);
    }

    private boolean initCustomActionBar() {
        ActionBar mActionbar = getActionBar();
        if (mActionbar == null) {
            return false;
        }
        mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionbar.setDisplayShowCustomEnabled(true);
        mActionbar.setCustomView(R.layout.settings_header_layout);
        return true;
    }

    private void initViews() {
        mBackBtn = (ImageView) findViewById(R.id.settingheader_back);
        mHeaderView = (TextView) findViewById(R.id.header_title);
        mHeaderView.setText(R.string.settings_header_title);
    }

    public void setHeaderTitle(String title){
        mHeaderView.setText(title);
    }

    protected void setListeners() {
        if (null == mBackBtn) {
            return;
        }
        mBackBtn.setOnClickListener(this);
    }

    // 添加这个方法，以使2.x~4.3的代码在4.4上可以正常运行
    @SuppressLint("NewApi")
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return true;
    }

    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.settings_headers, target);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.settingheader_back) {
            appFinnish();
        }
    }

    public static boolean isSinglePane() {
        return isIsSinglePane();
    }

    public void setSelectItemOfListView(int position) {
        if (isIsSinglePane()) { // 手机设备不执行该方法
            return;
        }
        getListView().setSelection(position);
    }

    private void appFinnish() {
        FinancialApplication.setLoginInfo(); //退出参数设置时更新LoginInfo
        Intent intent = new Intent();

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp());
        boolean state = pref.getBoolean(ParamConstants.ONCE_DOWNLOAD_STATE, false);
        if (state) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(ParamConstants.ONCE_DOWNLOAD_STATE, false);
            editor.apply();
            intent.putExtra(ParamConstants.IF_DOWNLOAD_GOOGS, true);
        } else {
            intent.putExtra(ParamConstants.IF_DOWNLOAD_GOOGS, false);
        }
        setResult(RESULT_OK, intent);
        finish();
    }
}
