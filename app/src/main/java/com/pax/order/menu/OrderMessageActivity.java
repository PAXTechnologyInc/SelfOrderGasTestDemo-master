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
 * 2018/8/7 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.pax.order.MainActivity;
import com.pax.order.R;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.util.BaseActivity;
import com.pax.order.util.BasePresenter;

public class OrderMessageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView headerBack;
    private GlobalVariable global_var;
//    private Button btBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_message);
        global_var = ((GlobalVariable)  this.getApplicationContext());

        initView();
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    public void initView() {

        headerBack = (ImageView) findViewById(R.id.header_back);
//        btBack = (Button) findViewById(R.id.order_ok);
//        btBack.setOnClickListener(this);
        headerBack.setOnClickListener(this);

        // change text lift nozzle
        Handler h2=new Handler();
        h2.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(OrderMessageActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }, 4000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.header_back:

            default:
                break;
        }
    }
}
