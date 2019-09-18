package com.pax.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.easylink.R;
import com.pax.order.menu.MenuActivity;


// public class Finish extends TerminalInformationActivity.BaseActivity
public class Finish extends BaseActivity {

    private TextView card;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        card = (TextView) findViewById(R.id.card_num);

        //confirm = (Button) findViewById(R.id.finish);

        card.setText("5128 5701 2953 5397");


        // if click, then force cusor in the end of text
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(Finish.this, MenuActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


    }
}
