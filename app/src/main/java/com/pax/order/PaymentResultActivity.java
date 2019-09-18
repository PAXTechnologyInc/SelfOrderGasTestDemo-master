package com.pax.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pax.order.menu.MenuActivity;
import com.pax.order.pay.Pay;

import java.util.Timer;
import java.util.TimerTask;

public class PaymentResultActivity extends Activity {

    private TextView result_text;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_result);
        result_text = (TextView)findViewById(R.id.result_text);

        progressDialog = new ProgressDialog(PaymentResultActivity.this);
//        progressDialog.setMax(100);
        progressDialog.setMessage("Processing....");
//        progressDoalog.setTitle("");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();


        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                progressDialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000); //

        // change text approved
        Handler h1=new Handler();
        h1.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is approval");
                result_text.setText(R.string.approved);
            }
        }, 3000);


        // change text lift nozzle
        Handler h2=new Handler();
        h2.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is lift nozzle");
                result_text.setText(R.string.lift_nozzle);
            }
        }, 4000);

        // change text begin fulling
        Handler h3=new Handler();
        h3.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is begin fuel");
                result_text.setText(R.string.begin_fuel);
            }
        }, 7000);

        // change text  fullingme
        Handler h4=new Handler();
        h4.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is fueling");
                result_text.setText(R.string.fueling);
            }
        }, 9000);

        // change text after certain time
        Handler h5=new Handler();
        h5.postDelayed(new Runnable(){
            public void run(){
                System.out.println("This is jump");
                Intent intent = new Intent(PaymentResultActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        }, 10000);





    }
}
