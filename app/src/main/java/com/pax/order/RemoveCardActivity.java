package com.pax.order;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pax.order.constant.GlobalVariable;
import com.pax.order.pay.controller.SearchCardHelper;

public class RemoveCardActivity extends Activity implements SearchCardHelper.SearchCardCallback {

    private SearchCardHelper searchCardHelper;
    private GlobalVariable global_var;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_card);
        global_var = ((GlobalVariable)  this.getApplicationContext());
        detectRemoveCard();
    }

    public void detectRemoveCard(){
        long timeout=10000L;
//        searchCardHelper = global_var.getSetSearchCardObj();
        searchCardHelper = new SearchCardHelper(RemoveCardActivity.this,"Insert",timeout);
        //            stopQChipTimer();
        searchCardHelper.closePolling();
//        searchCardHelper.isCardRemoved(this);
        searchCardHelper.warnRemove(this);
    }


    @Override
    public void onReadCardError(int errorCode){

        System.out.println("This is errorCode:"+errorCode);
        System.out.println("This is errorCode:"+errorCode);

        if(errorCode == com.paxus.common.type.TransResult.ERR_TIMEOUT){
            Intent intent = new Intent(RemoveCardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }

    }


    @Override
    public void onReadCardOk(String message){
        System.out.println("This is callback str in :"+message);
//        System.out.println("This is readerType:"+message);
        Intent intent;
        intent = new Intent(RemoveCardActivity.this, ZipcodePinInput.class);
        startActivity(intent);
    }

    @Override
    public void onReadCardError(){


    }



}
