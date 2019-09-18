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
package com.pax.order.pay.poslink;

import com.pax.order.R;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.paydata.RequestVar;
import com.pax.poslink.PosLink;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PosLinkPaymentFragment  extends Fragment{

    private static final String TAG = PosLinkPaymentFragment.class.getSimpleName();

    private PosLink posLink;
    private Pay.PayListener mListener;
    private RequestVar mRequestVar;

    public void setParam(PosLink posLink, Pay.PayListener mListener, RequestVar mRequestVar){
        this.posLink =posLink;
        this.mListener = mListener;
        this.mRequestVar = mRequestVar;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_batch, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppLog.i(TAG, "onViewCreated: ");
        posLink= PosLinkManager.getInstance(getActivity()).getPoslink();
        PoslinkPaymentTask poslinkPaymentTask = new PoslinkPaymentTask(getActivity(),
                posLink, mListener);
        poslinkPaymentTask.execute(mRequestVar);

    }




}
