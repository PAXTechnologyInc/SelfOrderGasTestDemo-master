package com.pax.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.menu.MenuActivity;

//import static com.pax.order.FinancialApplication.TAG;

public class ZipcodePinInput extends Activity {

    private EditText code_input;
    private TextView info_input;
    private GlobalVariable global_var;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zipcode_pin_input);
        global_var = ((GlobalVariable) this.getApplicationContext());

        code_input = (EditText) findViewById(R.id.edit_input);
        info_input = (TextView) findViewById(R.id.info_input);

        if(global_var.getEDCType().equals(APPConstants.CREDIT)){
            info_input.setText(R.string.credit_desc);
//            info_input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_CLASS_PHONE);
            info_input.setInputType(InputType.TYPE_CLASS_PHONE);
        }else{
            info_input.setText(R.string.debit_desc);
            code_input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }

        code_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
//                    Log.i(TAG,"Enter pressed");
                    String input_value = code_input.getText().toString();
                    System.out.println("This is result"+code_input.getText());

                    if(input_value.length()!=0 && input_value.length()<7){
                        Intent intent = new Intent(ZipcodePinInput.this, CarWashActivity.class);
//                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        startActivity(intent);
                    }else{
                        //Displaying Toast with Hello Javatpoint message
                        String mes = "";
                        if(global_var.getEDCType().equals(APPConstants.CREDIT)){
                            mes = getString(R.string.credit_alert);
                        }else{
                            mes = getString(R.string.debit_alert);
                        }
                        Toast.makeText(getApplicationContext(),mes,Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });
    }



}
