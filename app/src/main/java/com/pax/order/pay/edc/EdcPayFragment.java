package com.pax.order.pay.edc;


import com.pax.order.R;
import com.pax.order.logger.AppLog;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Activities that contain this fragment must involke the
 * onActivityResult of the fragment to handle the trrans result.
 */
public class EdcPayFragment extends Fragment {

    private final String TAG = EdcPayFragment.class.getSimpleName();
    private static final int EDC_CODE = 100;
    EdcPaymentImpl edcPayment;

    public void setEdcPayment(EdcPaymentImpl edcPayment) {
        this.edcPayment = edcPayment;
    }

    public EdcPayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppLog.i(TAG, "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edc_pay, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edcPayment.doTrans(EdcPayFragment.this.getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.i(TAG, "onActivityResult: " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDC_CODE:
                edcPayment.dealEDCPayResult(requestCode, resultCode, data);
                break;
            default:
                break;
        }
    }
}
