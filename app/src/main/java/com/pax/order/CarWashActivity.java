package com.pax.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pax.order.constant.APPConstants;

public class CarWashActivity extends Activity  implements View.OnClickListener{

    private Button yes_confirm, no_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_wash);

        yes_confirm = (Button) findViewById(R.id.yes_confirm);
        no_confirm = (Button) findViewById(R.id.no_confirm);

        yes_confirm.setOnClickListener(CarWashActivity.this);
        no_confirm.setOnClickListener(CarWashActivity.this);
    }

    @Override
    public void onClick(View v) {

        // direct to insert card activity
        Intent intent = new Intent(CarWashActivity.this, InsertCardActivity.class);
        startActivity(intent);
    }
}
