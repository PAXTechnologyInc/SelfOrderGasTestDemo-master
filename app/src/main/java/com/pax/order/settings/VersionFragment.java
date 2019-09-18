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
 * 2018/9/25 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.order.BuildConfig;
import com.pax.order.R;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;

public class VersionFragment extends SettingsBaseFragment {

    private TextView mTvVersion;
    private TextView mTvBuildTime;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View contentView = View.inflate(getActivity(), R.layout.settings_version_layout, null);
        mTvVersion = (TextView) contentView.findViewById(R.id.tv_version);
        //    mTvVersion.setText("VER：" + FinancialApplication.getVersion());
        mTvVersion.setText("VER：" + BuildConfig.VERSION_NAME);

        mTvBuildTime = (TextView) contentView.findViewById(R.id.tv_updatetime);
        mTvBuildTime.setText(BuildConfig.BUILD_TIME);

        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsParamActivity mActivity;
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.settings_version));
    }
}
