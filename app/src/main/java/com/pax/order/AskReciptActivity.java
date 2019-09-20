package com.pax.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pax.order.constant.APPConstants;
import com.pax.order.menu.OrderMessageActivity;

public class AskReciptActivity extends Activity implements View.OnClickListener {

    private Button yes, no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_recipt);

        yes = (Button) findViewById(R.id.yes_button);
        no = (Button) findViewById(R.id.no_button);
        yes.setOnClickListener(AskReciptActivity.this);
        no.setOnClickListener(AskReciptActivity.this);
    }

    @Override
    public void onClick(View v) {


        // direct to insert card activity
        Intent intent = new Intent(AskReciptActivity.this, OrderMessageActivity.class);
//        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }
}
