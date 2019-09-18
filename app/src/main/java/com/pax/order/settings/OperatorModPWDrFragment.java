package com.pax.order.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.util.ToastUtils;


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
 * 2018/8/30 	        wanglz           	Create/Add/Modify/Delete
 * ============================================================================
 */
public class OperatorModPWDrFragment extends SettingsBaseFragment implements View.OnClickListener {
    private SettingsParamActivity mActivity;

    private EditText edtOperId;
    private EditText edtPwd;
    private EditText edtPwdConfirm;
    private Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = View.inflate(getActivity(), R.layout.modify_pwd_layout, null);
        initView(view);
        bindEvent();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (SettingsParamActivity) getActivity();
        mActivity.setHeaderTitle(getResources().getString(R.string.modify_oper_pwd));
    }

    protected void initView(View view) {
        edtOperId = (EditText) view.findViewById(R.id.oper_id);
        edtPwd = (EditText) view.findViewById(R.id.new_pwd);
        edtPwdConfirm = (EditText) view.findViewById(R.id.re_new_pwd);

        btnConfirm = (Button) view.findViewById(R.id.oper_confirm);
    }

    protected void bindEvent() {
        btnConfirm.setOnClickListener(this);
        edtPwd.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oper_confirm:
                if (updatePwd()) {
                    ToastUtils.showMessage(getActivity(), getString(R.string.passwd_revise_succ), 0);
                    //finish();
                    return;
                }
                break;
            default:
                break;
        }
    }

    private boolean updatePwd() {
        if (!checkOperId()) {
            return false;
        }
        if (!checkPwd()) {
            return false;
        }

        FinancialApplication.setLogPwd(edtPwd.getText().toString());
        return true;

    }

    // 操作员号检查， 00， 99 不允许修改
    private boolean checkOperId() {
//        String operId = edtOperId.getText().toString();
//        if (operId == null || operId.equals("")) {
//            edtOperId.requestFocus();
//            edtOperId.setText("");
//            return false;
//        }
//
//        if (operId.length() != 2) {
//            ToastUtils.showMessage(OperChgPwdActivity.this, getString(R.string.oper_is_not_exist));
//            edtOperId.requestFocus();
//            edtOperId.setText("");
//            return false;
//        }
//
//        if (operId.equals(Operator.OPER_MANAGE)) {
//            edtOperId.requestFocus();
//            edtOperId.setText("");
//            edtPwd.setText("");
//            edtPwdConfirm.setText("");
//            return false;
//        }
//
//        if (FinancialApplication.getsOperatorDb().find(operId) == null) {
//            ToastUtils.showMessage(OperChgPwdActivity.this, getString(R.string.oper_is_not_exist));
//            edtOperId.requestFocus();
//            edtOperId.setText("");
//            edtPwd.setText("");
//            edtPwdConfirm.setText("");
//            return false;
//        }

        return true;

    }

    private boolean checkPwd() {

        String operPwd = edtPwd.getText().toString();
        if (operPwd == null || operPwd.equals("")) {
            edtPwd.requestFocus();
            edtPwd.setText("");
            return false;
        }

//        if (operPwd != null && operPwd.length() != 8) {
//            ToastUtils.showMessage(getActivity(), getString(R.string.please_enter_four_num), 0);
//            edtPwd.requestFocus();
//            edtPwd.setText("");
//            return false;
//        }

        String pwdConfirm = edtPwdConfirm.getText().toString();

        if (pwdConfirm == null || pwdConfirm.equals("")) {
            edtPwdConfirm.requestFocus();
            edtPwdConfirm.setText("");
            return false;
        }

        if (!pwdConfirm.equals(edtPwd.getText().toString())) {
            ToastUtils.showMessage(getActivity(), getString(R.string.psw_is_not_equal));
            edtPwdConfirm.requestFocus();
            edtPwdConfirm.setText("");
            return false;
        }
        return true;
    }
}