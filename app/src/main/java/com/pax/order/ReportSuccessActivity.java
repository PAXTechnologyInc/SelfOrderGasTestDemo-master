package com.pax.order;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.order.R;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.db.OpenTicketDb;
import com.pax.order.entity.OpenTicket;
import com.pax.order.menu.MenuActivity;

import butterknife.ButterKnife;

public class ReportSuccessActivity extends BaseActivity {

    private Button success_button;
    private ImageView responseImage;
    private TextView resultText;

    private OpenTicketDb cart;
    private GlobalVariable global_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_report_success);
        global_var = ((GlobalVariable) this.getApplicationContext());

        ButterKnife.bind(this);

        success_button = findViewById(R.id.success_button);

        cart = FinancialApplication.getOpenTicketDbHelper();

        // tip click event
        success_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // set cursor in the right end text input
                Intent intent = new Intent(ReportSuccessActivity.this, MenuActivity.class);

                // TODO:: Clear Cart
                for(OpenTicket item: cart.findAllOpenTicketData()){
                    System.out.println("Ticket: " + item.getName());
                }
                global_var.setOrderTicket(null);
                String tableNum = PreferenceManager.getDefaultSharedPreferences(FinancialApplication.getApp())
                        .getString(ParamConstants.TABLE_NUM, null);
                global_var.setPaid(tableNum);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
    }







}

