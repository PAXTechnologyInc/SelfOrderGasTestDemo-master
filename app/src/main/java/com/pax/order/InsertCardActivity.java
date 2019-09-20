package com.pax.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.menu.MenuActivity;
import com.pax.order.menu.ShoppingCartActivity;
import com.pax.order.pay.controller.SearchCardHelper;
import com.pax.order.payment.PaymentActivity;
import com.pax.order.payment.UseCardFragment;

public class InsertCardActivity extends Activity implements SearchCardHelper.SearchCardCallback {

    ImageView insertView;
    private GlobalVariable global_var;
    private SearchCardHelper searchCardController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_card);
        global_var = ((GlobalVariable)  this.getApplicationContext());

        ImageView Insert_View = (ImageView) findViewById(R.id.Insert_View);
        //Map<ETermInfoKey, String> eTermInfoKey = FinancialApplication.getDal().getSys().getTermInfo();
        //String model = eTermInfoKey.get(ETermInfoKey.MODEL);
        String model = "A920";

        Glide.with(InsertCardActivity.this).load(R.drawable.ar8)
                    .asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(Insert_View);

        startCheckCard();

    }

    @Override
    public void onReadCardError(int errorCode){

        System.out.println("This is errorCode:"+errorCode);

        if(errorCode == com.paxus.common.type.TransResult.ERR_TIMEOUT){
            Intent intent = new Intent(InsertCardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }


    @Override
    public void onReadCardOk(String track2){
        System.out.println("This is callback str in inserCardActivity:"+track2);
        String readerType = track2.split(":")[1];
        System.out.println("This is readerType:"+readerType);
        Intent intent;
        intent = new Intent(InsertCardActivity.this, RemoveCardActivity.class);
        if(readerType.equals("MSR")){
            // show dialog to reject card
            global_var.setReadCardType(readerType);
        }else if(readerType.equals("ICC")){
            global_var.setReadCardType(readerType);
        }else{
            global_var.setReadCardType(readerType);
        }
        startActivity(intent);
    }

    @Override
    public void onReadCardError(){


    }

    private void startCheckCard() {
        long timeout=20000L;
        searchCardController = new SearchCardHelper(InsertCardActivity.this,"Insert",timeout);
        //            stopQChipTimer();
        searchCardController.closePolling();
        searchCardController.start(this);
        global_var.setSetSearchCardObj( searchCardController);
    }

    private void stopQChipCheckCard() {
        if (searchCardController != null) {
            searchCardController.closePolling();
            searchCardController = null;
        }
    }


}
