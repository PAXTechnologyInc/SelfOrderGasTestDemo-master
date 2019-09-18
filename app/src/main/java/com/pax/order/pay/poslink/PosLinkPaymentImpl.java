

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
 * 20180927  	         xiazp           	Create/Add/Modify/Delete
 * ===========================================================================================
 */
package com.pax.order.pay.poslink;

import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.poslink.PosLink;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;

public class PosLinkPaymentImpl implements Pay.Payment {
    private static final String TAG = PosLinkPaymentImpl.class.getSimpleName();
    private PosLink posLink;
    private Pay.PayListener mPayListener = null;

    @Override
    public void setPayListener(Pay.PayListener payListener) {
        mPayListener = payListener;
    }

    @Override
    public void abortTrans() {
        AppLog.i(TAG, "abortTrans: ");
        if (posLink != null) {
            posLink.CancelTrans();
        }
    }

    @Override
    public boolean doPayment(Context context, RequestVar requestVar) {

        posLink = PosLinkManager.getInstance(context).getPoslink();
        PoslinkPaymentTask poslinkPaymentTask = new PoslinkPaymentTask(context,
                posLink, mPayListener);
        poslinkPaymentTask.setViewUtil(new ViewUtil((Activity) context));
        poslinkPaymentTask.execute(requestVar);

        return true;
    }

    @Override
    public void doCommSetting(Context context, boolean isUSB) {
        return;
    }

    @Override
    public boolean doBatch(Context context) {
        posLink = PosLinkManager.getInstance(context).getPoslink();
        PoslinkBatchTask task = new PoslinkBatchTask(context, posLink, mPayListener);
        task.setViewUtil(new ViewUtil((Activity) context));
        task.execute();

        return false;
    }


    public boolean doBatchWithUI(Context context) {

//        Fragment newFragment = new PosLinkPaymentFragment();
//
//        // Add the fragment to the activity, pushing this transaction
//        // on to the back stack.
//        FragmentTransaction ft = FinancialApplication.getTopActivity()
//                .getFragmentManager().beginTransaction();
//        ft.replace(R.id.fragment_container, newFragment);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.addToBackStack(null);
//        ft.commit();

        return false;
    }

    @Override
    public void setFragmentMannager(FragmentManager fragmentMannager) {

    }
}
