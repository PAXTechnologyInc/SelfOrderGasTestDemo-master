package com.pax.order.pay.poslink;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.pay.paydata.PayResponseVar;

/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * yyyymmdd  	         xiazp           	Create/Add/Modify/Delete
 * ===========================================================================================
 */
public class ViewUtil {

    private Activity mActivity;

    public ViewUtil(Activity activity) {
        mActivity = activity;
    }

    public void showTransProcsess() {
        MProgressDialog.showProgress(mActivity, "Procsssing, please wait");
    }

    public void dismissTransProgress() {
        MProgressDialog.dismissProgress();
    }

    public void showTransResult(PayResponseVar payResponseVar, OnDialogDismissListener listener) {
        MProgressDialog.dismissProgress();
        String msg = "";
        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.mn_icon_dialog_error);
        if (payResponseVar.getResult().equals(PayResponseVar.TRANS_SUCESS)) {
            msg = "TransOK!";
            drawable = mActivity.getResources().getDrawable(R.drawable.mn_icon_dialog_ok);
        } else if (payResponseVar.getResult().equals(PayResponseVar.TRANS_FAIL)) {
            msg = "TransFailed!";
        } else if (payResponseVar.getResult().equals(PayResponseVar.TRANS_TIMEOUT)) {
            msg = "TransTimeout!";
        } else if (payResponseVar.getResult().equals(PayResponseVar.TRANS_CANCEL)) {
            msg = "UserCancel!";
            drawable = mActivity.getResources().getDrawable(R.drawable.mn_icon_dialog_warn);
        } else if (payResponseVar.getResult().equals(PayResponseVar.TRANS_NOLOG)) {
            msg = "NoTransLog!";
            drawable = mActivity.getResources().getDrawable(R.drawable.mn_icon_dialog_warn);
        }
        new MStatusDialog(mActivity,
                new MDialogConfig.Builder()
                        .setOnDialogDismissListener(listener)
                        .isCanceledOnTouchOutside(true)
                        .build()).show(msg, drawable, (long) 3000);
    }


}
