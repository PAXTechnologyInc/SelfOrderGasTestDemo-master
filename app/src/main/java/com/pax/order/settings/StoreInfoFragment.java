package com.pax.order.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.view.View;

import com.pax.order.R;
import com.pax.order.orderserver.entity.getstoreinfo.GetStoreInfoRsp;

public class StoreInfoFragment extends SettingsBaseFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_store_info);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SettingsParamActivity mActivity;
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.store_info));
        fillStoreInfo();

    }

    private void fillStoreInfo(){
        GetStoreInfoRsp storeInfo = GetStoreInfoInstance.getInstance().getStoreInfo();
        if(storeInfo == null) return;

        Preference pre;
        if(storeInfo.getName() != null){
             pre = findPreference(getString(R.string.store_name));
             pre.setSummary(storeInfo.getName());
        }

        if(storeInfo.getPhone() != null){
             pre = findPreference(getString(R.string.store_phonenum));
             pre.setSummary(storeInfo.getPhone());
        }
        if(storeInfo.getCountry() != null){
             pre = findPreference(getString(R.string.store_country));
             pre.setSummary(storeInfo.getCountry());
        }
        if(storeInfo.getState() != null){
             pre = findPreference(getString(R.string.store_state));
             pre.setSummary(storeInfo.getState());
        }
        if(storeInfo.getCity() != null){
             pre = findPreference(getString(R.string.store_city));
             pre.setSummary(storeInfo.getCity());
        }
        if(storeInfo.getAddress1() != null){
             pre = findPreference(getString(R.string.store_address1));
             pre.setSummary(storeInfo.getAddress1());
        }
        if(storeInfo.getAddress2() != null){
             pre = findPreference(getString(R.string.store_address2));
             pre.setSummary(storeInfo.getAddress2());
        }
        if(storeInfo.getZipCode() != null){
             pre = findPreference(getString(R.string.store_zip));
             pre.setSummary(storeInfo.getZipCode());
        }
        if(storeInfo.getStatus() != null){
             pre = findPreference(getString(R.string.store_status));
             pre.setSummary(storeInfo.getStatus());
        }
    }

}
