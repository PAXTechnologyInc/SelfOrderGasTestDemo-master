package com.pax.order;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Terry on 1/16/2018.
 */

public abstract class BaseActivity extends Activity{

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        System.out.println("base activity:" +this);
    }
}
