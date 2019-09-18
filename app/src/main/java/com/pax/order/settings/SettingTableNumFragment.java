package com.pax.order.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pax.order.FinancialApplication;
import com.pax.order.ParamConstants;
import com.pax.order.R;
import com.pax.order.orderserver.entity.gettable.TablePro;

import java.util.ArrayList;
import java.util.List;


public class SettingTableNumFragment extends UnityStyleSettingBaseFragment {

    ListPreference  listPreference;



    @Override
    protected void setupSimplePreferencesScreen() {
        String selectNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                .getString(ParamConstants.TABLE_NUM, null);
        System.out.println("SettingTableNumFragment setupSimplePreferencesScreen");
        System.out.println("GetTableInstance table num: " + GetTableInstance.getInstance().getTableNum());
        TablePro tableOne = new TablePro("1","Table1", "1", "3", "0", "East", "1" );
//        TablePro tableTwo = new TablePro("2","Table2", "2", "3", "0", "East", "1" );
        List<TablePro> tables = new ArrayList<TablePro>();
        tables.add(tableOne);
//        tables.add(tableTwo);
        GetTableInstance.getInstance().setTableProList(tables);

        if(GetTableInstance.getInstance().getTableNum()!= null) {
            String[] numEntri = GetTableInstance.getInstance().getTableName();
            String[] numValue = GetTableInstance.getInstance().getTableNum();

//            String[] numEntri = {"Table1", "Table2"};
//            String[] numValue = {"1","2"};

            listPreference = (ListPreference) findPreference(getString(R.string.table_num));
            listPreference.setEntries(numEntri);
            listPreference.setEntryValues(numValue);
            listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary((String)newValue);
                    return true;
                }
            });
            if (selectNum != null && !selectNum.isEmpty()) {
                listPreference.setSummary(selectNum);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        addPreferencesFromResource(R.xml.setting_table_config);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SettingsParamActivity mActivity;

        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.settings_menu_merchant_parameter));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getViewId() {
        return R.xml.setting_table_config;

    }
}
