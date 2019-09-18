package com.pax.order;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.easylink.R;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.menu.MenuActivity;


// public class Finish extends TerminalInformationActivity.BaseActivity
public class FinishActivity extends BaseActivity {

    private TextView card;
    private Button confirm;

    private GlobalVariable global_var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish2);
        global_var = ((GlobalVariable) this.getApplicationContext());
        card = (TextView) findViewById(R.id.card_num);

        confirm = (Button) findViewById(R.id.finish);
        String card_number = global_var.getCardNumber();
        String first_four_nums = card_number.substring(0,4);
        String last_four_nums = card_number.substring(card_number.length() - 4);

        card.setText(first_four_nums + " **** **** " + last_four_nums);


        // if click, then force cusor in the end of text
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

//                Intent intent = new Intent(FinishActivity.this, MenuActivity.class);
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startProgress();
                //TODO:: Scanning Please Wait
                //TODO:: Go to Transaction Success Activity
                //TODO:: Clear the order


//                startActivity(intent);
            }
        });


    } // End onCreate

    public ProgressDialog loadingdialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingdialog.dismiss();
            Intent intent = new Intent(FinishActivity.this, ReportSuccessActivity.class);
            startActivity(intent);

        }
    };

    public void startProgress() {
        loadingdialog = ProgressDialog.show(FinishActivity.this,
                "","Scanning Please Wait",true);
        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    handler.sendEmptyMessage(0);

                } catch(Exception e) {
                    Log.e("threadmessage",e.getMessage());
                }
            }
        }.start();
    }
}
