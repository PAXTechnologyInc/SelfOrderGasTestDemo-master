/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2000-2018 PAX Technology, Inc. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2018/8/7 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.menu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.order.AskReciptActivity;
import com.pax.order.InsertCardActivity;
import com.pax.order.MainActivity;
import com.pax.order.R;
import com.pax.order.commonui.dialog.MDialogConfig;
import com.pax.order.commonui.dialog.MProgressDialog;
import com.pax.order.commonui.dialog.MStatusDialog;
import com.pax.order.commonui.dialog.OnDialogDismissListener;
import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.entity.CartData.EOrderStatus;
import com.pax.order.entity.MessgeCode;
import com.pax.order.entity.OpenTicket;
import com.pax.order.entity.ProcessMessage;
import com.pax.order.entity.SelectGoods;
import com.pax.order.logger.AppLog;
import com.pax.order.menu.adapter.SelectAdapter;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.InternetCheckUtils;
import com.pax.order.util.MsgProActivity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

public class ShoppingCartActivity extends MsgProActivity implements View.OnClickListener, ShoppingCartContract.View {
    private static final String TAG = ShoppingCartActivity.class.getSimpleName();
    public ShoppingCartPresenter mShoppingCartPresenter;

    private ImageView headerBack;
    private TextView tvClear;
    private TextView tvCount, tvCost,gasCost,gasDetail;
    private Button btBack, btOrder;
    private RecyclerView rvSelected;
    private SelectAdapter mSelectAdapter;
    private TextView failText;
    private LinearLayout failLayout;
    private final static int REQ_MESSAGE = 1;
    private double totalGasCost;

    private GlobalVariable global_var;


    @Override
    public void dealProcessMsg(ProcessMessage processMessage) {
        AppLog.d(TAG, "dealHandlerMsg: " + processMessage.getMessageCode());
        switch (processMessage.getMessageCode()) {
            case MessgeCode.OPENTICKETSUCC:
                startMessageView();
                break;
            case MessgeCode.OPENTICKETFAIL:
                MProgressDialog.dismissProgress();
                if (processMessage.getRspMsg() != null) {
                    failText.setText(processMessage.getRspMsg()
                            .getResultCode() + ", " + processMessage.getRspMsg()
                            .getResultMessage());
                } else {
                    failText.setText(R.string.order_fail);
                }
//                failLayout.setVisibility(View.VISIBLE);
                break;
            case MessgeCode.OPENTICKETALREADYEXIST:
                mShoppingCartPresenter.addOrder();
                break;
            case MessgeCode.OPENTICKETNOPRODUCT:
                mShoppingCartPresenter.updateUnderStockItem(processMessage.getErrDataInOpenTickets());
                MProgressDialog.dismissProgress();
                failText.setText(R.string.order_fail_noproduct);
//                failLayout.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        global_var = ((GlobalVariable)  this.getApplicationContext());

        // init gas detail view
        initGasDetalView();

        mShoppingCartPresenter.start();
    }

    @Override
    public void initView(List<SelectGoods> goodstList) {
        rvSelected = (RecyclerView) findViewById(R.id.selectRecyclerView);
        rvSelected.setLayoutManager(new LinearLayoutManager(this));
        mSelectAdapter = new SelectAdapter(this, goodstList);
        rvSelected.setAdapter(mSelectAdapter);

        headerBack = (ImageView) findViewById(R.id.header_back);
        //        tvClear = (TextView) findViewById(R.id.cart_clear);
        btBack = (Button) findViewById(R.id.cart_back);
        btOrder = (Button) findViewById(R.id.cart_order);
        tvCount = (TextView) findViewById(R.id.cart_sum);
        tvCost = (TextView) findViewById(R.id.cart_total);
        //        tvClear.setOnClickListener(this);

        btBack.setOnClickListener(this);
        btOrder.setOnClickListener(this);
        headerBack.setOnClickListener(this);

//        failText = (TextView) findView(R.id.err_msg);
//        failLayout = (LinearLayout) findView(R.id.err_layout);
//        failText.setTextColor(Color.parseColor("#eb6158"));
//        failLayout.setBackgroundColor(Color.parseColor("#ffe9e5"));
//        failLayout.setVisibility(View.GONE);

        updateView(goodstList);
    }

    public void initGasDetalView(){
        gasDetail = (TextView) findViewById(R.id.gas_detail);
        gasCost = (TextView) findViewById(R.id.gasCost);
        System.out.println("this is global_var.getGasDetail():" + global_var.getGasDetail());
        if(global_var.getGasDetail().equals("")){
            gasDetail = (TextView) findViewById(R.id.gas_detail);
            gasCost = (TextView) findViewById(R.id.gasCost);
            Random random = new Random();
            int pumpNum = random.nextInt(15);
            double gallons = 0.1 +(random.nextDouble()*(20-1));
            String gallonsStr = new DecimalFormat("##.###").format(gallons);
            double unitPrice = 2.398;
            totalGasCost = gallons *unitPrice;
            global_var.setGasCost(totalGasCost);
            String gasDetailStr = "Pump #: "+ pumpNum +";   Fuel Type: Unleaded;  Gallons: " +
                    ""+ gallonsStr +";  Price/Gal: "+ unitPrice;

            gasDetail.setText(gasDetailStr);
            global_var.setGasDetail(gasDetailStr);

            // total price UI
            String totalPriceStr = new DecimalFormat("##.##").format(totalGasCost);
            gasCost.setText("$"+totalPriceStr);

        }else{
            totalGasCost = global_var.getGasCost();
            gasDetail.setText(global_var.getGasDetail());
            // total price UI
            String totalPriceStr = new DecimalFormat("##.##").format(totalGasCost);
            gasCost.setText("$" +totalPriceStr );
        }
    }

    @Override
    public void updateView(List<SelectGoods> selectList) {
        int size = selectList.size();
        int count = 0;
        double cost = 0;
        boolean noStockFlag = false;

        for (SelectGoods selectGoods : selectList) {
            noStockFlag = selectGoods.isUnderStock();
            if (noStockFlag) {
                break;
            }
        }
        noStockFlag = false;
        if (noStockFlag) {
//            failText.setText(R.string.order_fail_noproduct);
//            failLayout.setVisibility(View.VISIBLE);
            btOrder.setEnabled(false);
            btOrder.setBackground(getResources().getDrawable(R.drawable.tv_order_noresponse));
//        } else if (selectList.size() == 0) {
////            failLayout.setVisibility(View.GONE);
//            btOrder.setEnabled(false);
//            btOrder.setBackground(getResources().getDrawable(R.drawable.tv_order_noresponse));
        } else {
//            failLayout.setVisibility(View.GONE);
            btOrder.setEnabled(true);
            btOrder.setBackground(getResources().getDrawable(R.drawable.tv_order_corners));
        }
        for (SelectGoods selectGoods : selectList) {
            count += selectGoods.getQuantity();
            cost += selectGoods.getQuantity() * selectGoods.getPrice() ;
        }
        cost += totalGasCost;

        tvCount.setText(String.valueOf(count));
        tvCost.setText(AmountUtils.amountFormat(cost));

        mSelectAdapter.notifyDataSetChanged();
    }

    @Override
    public void startMessageView() {
        Intent intent = new Intent(this, OrderMessageActivity.class);
        startActivityForResult(intent, REQ_MESSAGE);
        finshView(EOrderStatus.ORDER);
    }

    @Override
    public void finshView(EOrderStatus orderStatus) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderStatus", orderStatus);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void setPresenter(ShoppingCartContract.Presenter presenter) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cart_order:
                if(global_var.isFuelUp()){
//                    dialogShow(getString(R.string.fill_reciept));
                    Intent intent = new Intent(ShoppingCartActivity.this, AskReciptActivity.class);
                    startActivity(intent);
                    System.out.print("Fild up");
                }else{
                    Toast.makeText(ShoppingCartActivity.this,getString(R.string.fuel_still),
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.cart_back:
                mShoppingCartPresenter.clear();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
//                mShoppingCartPresenter.back();
                break;

            case R.id.header_back:
                mShoppingCartPresenter.back();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_MESSAGE:
                finshView(EOrderStatus.ORDER);
                break;

            default:
                break;
        }
    }

    private void dialogShow(String text){
        final AlertDialog alert = new AlertDialog.Builder(ShoppingCartActivity.this, AlertDialog.THEME_HOLO_DARK).create();
        alert.setCancelable(false);
        // alert.setContentView(R.layout.custom_dialog);

        alert.setTitle(text);
        alert.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                        OpenTicket ticket = mShoppingCartPresenter.order();
                        global_var.setOrderTicket(ticket);
                        startMessageView();

                    }
                });
        alert.setButton(DialogInterface.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        alert.dismiss();
                        OpenTicket ticket = mShoppingCartPresenter.order();
                        global_var.setOrderTicket(ticket);
                        startMessageView();
                    }
                });

        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MProgressDialog.dismissProgress();
    }

    @Override
    protected BasePresenter createPresenter() {
        // Create the presenter
        mShoppingCartPresenter = new ShoppingCartPresenter((ShoppingCartContract.View) this);
        return mShoppingCartPresenter;
    }
}
