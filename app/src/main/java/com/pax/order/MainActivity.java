package com.pax.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.menu.MenuActivity;

public class MainActivity extends Activity implements View.OnClickListener{


    private Button credit;
    private Button debit;
    private GlobalVariable global_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        global_var = ((GlobalVariable) this.getApplicationContext());
        credit = (Button)findViewById(R.id.credit_confirm);
        debit = (Button)findViewById(R.id.debit_confirm);
        credit.setOnClickListener(MainActivity.this);
        debit.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

        // Credit
        if(v.getId()==R.id.credit_confirm){
            global_var.setEDCType(APPConstants.CREDIT);
        }else{
            global_var.setEDCType(APPConstants.DEBIT);
        }

        // direct to insert card activity
        Intent intent = new Intent(MainActivity.this, InsertCardActivity.class);
//        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
