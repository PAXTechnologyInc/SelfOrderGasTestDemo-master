package com.pax.order.pay.poslink;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.pax.order.R;


/**
 * Created by Leon on 2017/11/24.
 */

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
//        StatusBarCompat.setRootViewProperty((ViewGroup) getWindow().getDecorView());
    }

}
