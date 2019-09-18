package com.pax.order;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.keyboard.OfoKeyboard;

import in.arjsna.passcodeview.PassCodeView;


public class PinActivity extends BaseActivity {

//    private EditText editText;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_pin);
//        editText=  findViewById(R.id.et_numberplate);
//        final OfoKeyboard keyboard = new OfoKeyboard(PinActivity.this);//获取到keyboard对象
//
//        keyboard.attachTo(editText,true);
//
//
//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                keyboard.attachTo(editText,true);//eiditext绑定keyboard，false表示普通数字键盘
//
//            }
//        });
//
//
//        keyboard.setOnOkClick(new OfoKeyboard.OnOkClick() {
//            @Override
//            public void onOkClick() {
//                Log.i(">>>>>>","Confirm");
//                Toast.makeText(PinActivity.this,editText.getText().toString(),Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(PinActivity.this, FinishActivity.class);
//                startActivity(intent);
//            }
//        });
//        //隐藏键盘按钮
//        keyboard.setOnCancelClick(new OfoKeyboard.OnCancelClcik() {
//            @Override
//            public void onCancelClick() {
//                Toast.makeText(PinActivity.this,"Hide Keyboard",Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private final String PASSCODE = "1234";
    private PassCodeView passCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passcode_layout);
        passCodeView = (PassCodeView) findViewById(R.id.pass_code_view);
        TextView promptView = (TextView) findViewById(R.id.promptview);
        Typeface typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/Font-Bold.ttf");
        passCodeView.setTypeFace(typeFace);
        passCodeView.setKeyTextColor(R.color.black_shade);
        passCodeView.setEmptyDrawable(R.drawable.empty_dot);
        passCodeView.setFilledDrawable(R.drawable.filled_dot);
        promptView.setTypeface(typeFace);
        bindEvents();
    }

    private void bindEvents() {
        passCodeView.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            @Override public void onTextChanged(String text) {
                if (text.length() == 4) {
//                    if (text.equals(PASSCODE)) {
                        Intent intent = new Intent(PinActivity.this, FinishActivity.class);
                        startActivity(intent);
//                        getActivity().finish();
//                    } else {
//                        passCodeView.setError(true);
//                    }
                }
            }
        });
    }
}
