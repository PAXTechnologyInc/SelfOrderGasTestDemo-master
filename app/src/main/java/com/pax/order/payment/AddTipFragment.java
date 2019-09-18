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
 * 2018/8/15 	         zenglc           	Create/Add/Modify/Delete
 * ============================================================================
 */
package com.pax.order.payment;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.contrarywind.adapter.WheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.pax.order.Finish;
import com.pax.order.FinishActivity;
import com.pax.order.PinActivity;
import com.pax.order.R;
import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.constant.SystemVariable;
import com.pax.order.constant.TransDataTag;
import com.pax.order.logger.AppLog;
import com.pax.order.pay.Pay;
import com.pax.order.pay.payInterfaceFactory.PayFactory;
import com.pax.order.util.AmountUtils;
import com.pax.order.util.BaseFragment;
import com.pax.order.util.BasePresenter;
import com.pax.order.util.CardCheck;
import com.pax.order.util.Convert;
import com.pax.order.util.CustomEventBus;
import com.pax.order.util.DialogClass;
import com.pax.order.util.EventBusResponseResult;
import com.pax.order.util.IView;
import com.paxsz.easylink.api.EasyLinkSdkManager;
import com.paxsz.easylink.api.ResponseCode;
import com.paxsz.easylink.model.DataModel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class AddTipFragment extends BaseFragment implements AddTipContract.View, View.OnClickListener {

    private AddTipPresenter mAddTipPresenter;
    private Button mBtConfirm;
    private TextView mTvSubTotal;
    private TextView mTvTax;
//    private TextView mTvTip;
    private TextView mTvNeedPay;
//    private WheelView mWheelView;
    private PaymentActivity mPaymentActivity;

    private BluetoothGatt gatt;
    private BluetoothGattCharacteristic characteristic;

    private BleDevice bleDevice;
    private static final UUID SERVICE_UUID =
            UUID.fromString("49535343-FE7D-4AE5-8FA9-9FAFD205E455");
    private static final UUID CHARACTERISTIC_UUID =
            UUID.fromString("49535343-1E4D-4BD9-BA61-23C647249616");
    private static final UUID WRITE_UUID =
            UUID.fromString("49535343-8841-43F4-A8D4-ECBE34729BB3");
    private String cmd = "start";

    private EasyLinkSdkManager easyLink;
    private TransDataTag transDataTag = new TransDataTag();
    private EventBusResponseResult eventBusResponseResult;
    private HashMap<String, String> transData = new HashMap<>();

    // Strings
    String card_number="none";
    String card_expired_date = null;
    String card_mag_data = "none";
    String card_string = null;

    private GlobalVariable global_var;
    SystemVariable sys_var;
    Intent intent;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventBusResponseResult = new EventBusResponseResult();

        mPaymentActivity = (PaymentActivity) mActivity;
        global_var = ((GlobalVariable) mPaymentActivity.getApplicationContext());
        easyLink = global_var.getEasylink();
        sys_var = new SystemVariable();
        commSet();

        mAddTipPresenter.start();
    }

    private void commSet() {
        Pay.Payment sale;

        sale = PayFactory.getPaymentInstance();

        sale.doCommSetting(mPaymentActivity, true);
    }

    @Override
    protected int getViewID() {
        return R.layout.fragment_add_tip;
    }

    @Override
    protected void initView(View view) {
        mBtConfirm = (Button) view.findViewById(R.id.confirm_tip);
//        mWheelView = (WheelView) view.findViewById(R.id.wheelview);
        mTvSubTotal = (TextView) view.findViewById(R.id.sub_total);
        mTvTax = (TextView) view.findViewById(R.id.tax);
//        mTvTip = (TextView) view.findViewById(R.id.tip);
        mTvNeedPay = (TextView) view.findViewById(R.id.need_pay);
    }

    @Override
    protected void bindEvent() {
        mBtConfirm.setOnClickListener(this);
    }

    @Override
    protected BasePresenter<IView> createPresenter() {

        mAddTipPresenter = new AddTipPresenter((AddTipContract.View) this);
        return mAddTipPresenter;
    }

    @Override
    public void initView(final List<String> strTip, double subTotal, double tax, double tip, double needPay) {

//        mWheelView.setCyclic(false);
//        mWheelView.setAdapter(new WheelAdapter() {
//            @Override
//            public int getItemsCount() {
//                return strTip.size();
//            }
//
//            @Override
//            public Object getItem(int index) {
//                return strTip.get(index);
//            }
//
//            @Override
//            public int indexOf(Object o) {
//                return 0;
//            }
//        });
//
//        mWheelView.setTextSize(20);
////        mWheelView.setCurrentItem(AddTipPresenter.DEFAULT_TIP_INDEX);
//        mWheelView.setCurrentItem(mAddTipPresenter.getTipIndex());
//        mWheelView.setLineSpacingMultiplier(2.0F);
//        mWheelView.setDividerColor(getResources().getColor(R.color.primary_yellow_color));
//        mWheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int index) {
//                AppLog.i("Add Tip", "index:" + index);
//                mAddTipPresenter.setTipSelected(index);
//            }
//        });

        mTvSubTotal.setText(AmountUtils.amountFormat(subTotal));
        mTvTax.setText(AmountUtils.amountFormat(tax));
//        mTvTip.setText(AmountUtils.amountFormat(tip));
        mTvNeedPay.setText(AmountUtils.amountFormat(needPay));
    }

    @Override
    public void updateView(double tip, double needPay) {
//        mTvTip.setText(AmountUtils.amountFormat(tip));
        mTvNeedPay.setText(AmountUtils.amountFormat(needPay));
    }

    @Override
    public void setPresenter(AddTipContract.Presenter presenter) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tip:
                mPaymentActivity.setTip(0);
                mPaymentActivity.setNeedPayAmoumt(mAddTipPresenter.getNeendPay());
                mPaymentActivity.setTax(mAddTipPresenter.getTax());
                mPaymentActivity.setmSubSplitPay(mAddTipPresenter.getSubPay());
                mPaymentActivity.setTabSelected(4);
                System.out.println("Total Amount: " + mAddTipPresenter.getNeendPay());
                global_var.setSumAmount(String.format("%.2f", mAddTipPresenter.getNeendPay()));

//                transProcess();
                initData();
                break;
            default:
                break;
        }
    }

    private void initData(){
        bleDevice = global_var.getBleDevice();

        transProcess();
        if (bleDevice == null){
            System.out.println("Bluetooth device not connected");

//            mPaymentActivity.finish();
        } else {
            gatt = BleManager.getInstance().getBluetoothGatt(bleDevice);
            System.out.println("This_name "+ bleDevice.getName());
            System.out.println("This_mac "+ bleDevice.getMac());
            System.out.println("This_key "+ bleDevice.getKey());
            ParcelUuid[] SERVICE_UUIDs = bleDevice.getDevice().getUuids();
            bleDevice.getDevice().getUuids();

//            characteristic = gatt.getService(SERVICE_UUID).getCharacteristic(CHARACTERISTIC_UUID);
//            System.out.println("notify_check "+ bleDevice.getKey());
//            BleManager.getInstance().notify(
//                    bleDevice,
//                    SERVICE_UUID.toString(),
//                    CHARACTERISTIC_UUID.toString(),
//                    new BleNotifyCallback() {
//
//                        @Override
//                        public void onNotifySuccess() {
//                            mPaymentActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    //addText(result, "notify success");
//                                    System.out.println("send success：");
//                                    sendData();
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onNotifyFailure(final BleException exception) {
//                            mPaymentActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    System.out.println("notify failure：" + exception.toString());
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onCharacteristicChanged(byte[] data) {
//
//                            System.out.println("data-----" + new String(data));
//                            mPaymentActivity.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                    String rec = new String(characteristic.getValue());
//                                    if(rec.equals("ready")){
//                                        System.out.println("接收到数据：" + rec+ ",请刷卡");
//
//                                    }else if(rec.equals("okay")){
//                                        System.out.println("接收到数据：" + rec+ ",刷卡成功");
//                                        Intent intent = new Intent(mPaymentActivity.getBaseContext(), PinActivity.class);
//                                        mPaymentActivity.startActivity(intent);
//
//                                    }else if(rec.equals("fail")){
//                                        System.out.println("接收到数据：" + rec+ ",刷卡失败");
//                                        System.out.println("接收到数据：" + rec+ ",刷卡成功");
//                                        Intent intent = new Intent(mPaymentActivity.getBaseContext(), PinActivity.class);
//                                        mPaymentActivity.startActivity(intent);
//                                    }else if(rec.equals("timeout")){
//                                        System.out.println("接收到数据：" + rec+ ",刷卡超时");
//                                    }
//                                }
//                            });
//                        }
//                    });
        }

    } // End initData

    private void sendData(){

        BleManager.getInstance().write(
                bleDevice,
                SERVICE_UUID.toString(),
                WRITE_UUID.toString(),
                cmd.getBytes(),
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        mPaymentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("数据发送成功");
                                System.out.println("Bluetooth write success.");

//

                            }
                        });
                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        mPaymentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("数据发送失败：" + exception.toString());
                                System.out.println("Bluetooth write failure."  + exception.toString());
                            }
                        });
                    }
                });

    } // End sendData

    private int transInsertActionPrepare( ){
        // insert action
        //  1. Credit refund need to do partial EMV for getting track2data.
        // 2. Debit EMV do MSR: if swipe card, then get data and pass; if insert card, then fail(may need show message to swipe card).

        String emv_insert_tag = null;
        int result = 1;

        ByteArrayOutputStream setTransDataTags = new ByteArrayOutputStream();
        ByteArrayOutputStream setParamDataTags = new ByteArrayOutputStream();
        global_var.setTransactionType(APPConstants.SALE);
        global_var.setEDCType(APPConstants.CREDIT);

        if(global_var.getEDCType().equals(APPConstants.DEBIT)){
            System.out.println("the debit process with insert card:" + "Debit");
            emv_insert_tag = APPConstants.NON;
            global_var.setEmvInsertTag(APPConstants.NON);
        }else if(global_var.getEDCType().equals(APPConstants.CREDIT)){
            if(global_var.getTransactionType().equals(APPConstants.SALE)){
                emv_insert_tag = APPConstants.FULL;
                global_var.setEmvInsertTag(APPConstants.FULL);
            }else if(global_var.getTransactionType().equals(APPConstants.REFUND)){
                emv_insert_tag = APPConstants.HALF;
                global_var.setEmvInsertTag(APPConstants.HALF);
            }
        }else if (global_var.getEDCType().equals(APPConstants.EBT)){
            emv_insert_tag = APPConstants.NON;
            global_var.setEmvInsertTag(APPConstants.NON);
        }

        // SET TAG
        System.out.println("the ememv_insert_taglt" + emv_insert_tag);
        System.out.println("EasyLink: " + easyLink);
        result = easyLink.setData(DataModel.DataType.CONFIGURATION_DATA, transDataTag.getConfigureDataTagSet(emv_insert_tag), setParamDataTags);
        System.out.println("the emv action result" + result);


        return result;
    }

    private int transPrepare() {
        //set trans data
        ByteArrayOutputStream failedTags = new ByteArrayOutputStream();

        StringBuilder sb = new StringBuilder("");
        byte[] preEMVData;
        int ret;
        final String zeroAmountStr = "000000000000";

        // Get total transaction amount
        if (global_var.getSumAmount() == null){
            global_var.setSumAmount( global_var.getBaseAmount());
            System.out.println("trans Amount: " + global_var.getBaseAmount());
            global_var.setTotalAmount(global_var.getSumAmount());
        }

        System.out.println("transPrepare sumAmount: " + global_var.getSumAmount());
        String sum_amount = global_var.getSumAmount().replace(".", "");
        System.out.print("Tglobal_var.getSumAmount() :" + sum_amount );
        if (Convert.IsNullString(sum_amount))
            sum_amount = zeroAmountStr;
        sum_amount = String.format(Locale.US, "%s%s", zeroAmountStr.substring(0, 12-sum_amount.length()), sum_amount);
        System.out.print("after format Tglobal_var.getSumAmount() :" + sum_amount);
        sb.append("9F02 06 ");
        sb.append(sum_amount);

        // Get cashback amount
        String cashbackAmt = global_var.getCashbackAmount().replace(".", "");
        if (Convert.IsNullString(cashbackAmt))
            cashbackAmt = zeroAmountStr;
        cashbackAmt = String.format(Locale.US, "%s%s", zeroAmountStr.substring(0, 12-cashbackAmt.length()), cashbackAmt);
        if (Integer.parseInt(cashbackAmt) != 0) {
            // !! do NOT set tag 9F03 if cashback amount is 0 !!
            sb.append(" 9F03 06 ");
            sb.append(cashbackAmt);
        }

        // Get Transaction Type
        String txnCode = APPConstants.EMV_TXN_TYPE.get(global_var.getTransactionType());
        if (Convert.IsNullString(txnCode))
            txnCode = "00";
        sb.append(" 9C 01 ");
        sb.append(txnCode);

        sb.append(" 5F2A 02 0840");    // 5F2A - transaction currency code ("0840" == USD)
        sb.append(" 5F36 01 02");      // 5F36 - transaction currency exponent
        sb.append(" 9A 03 161115");    // 9A   - transaction date
        sb.append(" 9F21 03 152659");  // 9F21 - transaction time

        System.out.println("sb-----:"+sb);
        Log.i(TAG, "emv TLV data to be set: " + sb.toString());
        preEMVData = Convert.HexString2Bytes(sb.toString());
        if (preEMVData == null)
            return -1;  // TODO: error code need to be changed
        System.out.println("preEMVData-----:"+preEMVData.toString());
        ret = easyLink.setData(DataModel.DataType.TRANSACTION_DATA, preEMVData, failedTags);
        Log.i(TAG, "failed tags: " + failedTags.toString());

        return ret;
    } // End transRepair

    private int transFlow() {
        //get card type
        ByteArrayOutputStream cardType = new ByteArrayOutputStream();
        int ret;
        String swipeFlag;
        byte[] swipeFlagTag = {0x03, 0x01}; // customized tag "0301" to get swipe method

        // SWIPE:       000403010101
        // FALLBACK:    000403010102
        // EMV:         000403010103
        // CTLSS:       000403010104
        ret = easyLink.getData(DataModel.DataType.CONFIGURATION_DATA, swipeFlagTag, cardType);
        Log.i(TAG, "getData - card type: " + Convert.Bytes2HexString(cardType.toByteArray()));
        Log.i(TAG, "gret: " + ret);
        if (ret != ResponseCode.EL_RET_OK) {
            Log.e(TAG, "get card type: ret3 = " + ret + "  " + cardType.toString());
            return ret;
        }
        System.out.println("the transData before putall:" + transData);

        transData.putAll(Convert.ParseTLVData(DataModel.DataType.CONFIGURATION_DATA, cardType.toByteArray()));
        System.out.println("thecl:" + transData);


        swipeFlag = transData.get("0301");
        if ((swipeFlag.equals("01")) || (swipeFlag.equals("02"))) {
            //MSR card
            if (swipeFlag.equals("01")) {
                global_var.setCardChooseOption(APPConstants.SWIPE);

            } else if (swipeFlag.equals("02")) {
                global_var.setCardChooseOption(APPConstants.FALLBACK);
            }
            return goMSRBranch();
        } else {
            //not MSR card
            // 1. Credit refund need to do partial EMV for getting track2data.
            // 2. Debit EMV do MSR: if swipe card, then get data and pass; if insert card, then fail(may need show message to swipe card).
            if (swipeFlag.equals("03")) {
                System.out.println("thewith insert card:" + "insert");
                global_var.setCardChooseOption(APPConstants.INSERT);
//                if(global_var.getEDCType().equals(APPConstants.DEBIT)){
//                    System.out.println("the debit process with insert card:" + "Debit");
//                    transInsertActionPrepare(APPConstants.NON);
//                    return goMSRBranch();
//                }else if(global_var.getEDCType().equals(APPConstants.CREDIT)){
//                    if(global_var.getTransactionType().equals(APPConstants.SALE)){
//                        transInsertActionPrepare(APPConstants.FULL);
//                    }else if(global_var.getTransactionType().equals(APPConstants.REFUND)){
//                        transInsertActionPrepare(APPConstants.HALF);
//                    }
//                }


            } else if (swipeFlag.equals("04")) {
                global_var.setCardChooseOption(APPConstants.TAP);
            }

            System.out.println("global_var.getEBTType() choosecard:" + global_var.getCardChooseOption());
            return goNotMSRBranch();
        }
    } // End transFlow


    private void transProcess() {
        System.out.println("TransProcess");
        new Thread(new Runnable() {
            @Override
            public void run() {

                // emv insert card action
                int ret1 = transInsertActionPrepare();
                System.out.println("ret1"+ret1);
                System.out.println(" ResponseCode.EL_RET_OK"+ ResponseCode.EL_RET_OK);
                if (ret1 != ResponseCode.EL_RET_OK) {
                    Log.e(TAG, "start trans: ret1 = " + ret1);
                    endTransProcess(ret1);
                    return;
                }


                // pre-emv
                int ret = transPrepare();
                if (ret != ResponseCode.EL_RET_OK) {
                    Log.e(TAG, "start trans: ret = " + ret);
                    endTransProcess(ret);
                    return;
                }

                //start read card processing
                ret = easyLink.startTransaction();
                System.out.println("thes");
                Log.e(TAG, "start trans: ret2 = " + ret);
                if (ret != ResponseCode.EL_RET_OK) {
                    Log.e(TAG, "start trans: ret2 = " + ret);
                    endTransProcess(ret);
                    threadMsg("Cancel_card_swipe");
                    return;
                }

                ret = transFlow();
                if (ret != ResponseCode.EL_RET_OK) {
                    Log.e(TAG, "start trans: ret4 = " + ret);
                    endTransProcess(ret);
                    return;
                }

                // save transData into global
                global_var.setTransData(transData);

                Log.i("TEST INFO", "EMV transaction, AC Type at 1st GAC = " + transData.get("9F27"));
                Log.i("transData detail:" , transData.toString());
                if ( global_var.checkCardType(APPConstants.SWIPE) ||
                        global_var.checkCardType(APPConstants.FALLBACK) ||
                        (global_var.checkCardType(APPConstants.INSERT) && transData.get("9F27").equals("80")) ) { // Swipe or Fallback, or EMV online
                    // return data through threadMsg to create a queue string
                    HashMap<String, String> swipe_data = parseEasylinkSwipeData();
                    Log.i("hash map data:", swipe_data.toString());
                    if (!swipe_data.isEmpty()) {
                        card_number = swipe_data.get(APPConstants.CARDNUM);
                        card_expired_date = swipe_data.get(APPConstants.EXPDATE);
                        card_mag_data = swipe_data.get(APPConstants.MAGDATA);
                        threadMsg(swipe_data.get(APPConstants.CARDNUM));

                        global_var.setCardNumber(card_number.toString());
                        Log.i("card_number detail:" , card_number.toString());
                        Log.i("card_expired_date det:" , card_expired_date.toString());
                        Log.i("card_mag_data detail:" , card_mag_data.toString());

                        // card_number_text.setText(swipe_data.get(APPConstants.CARDNUM));
                        //complete transaction success
                        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);
                    } else {
                        threadMsg("Card_number_error");
                        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.FAILED);
                    }
                } else if (global_var.checkCardType(APPConstants.INSERT)){   // EMV offline approved or declined
                    if (transData.get("9F27").equals("00")){
                        threadMsg("Transaction Declined");
                        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.FAILED);
                    } else if (transData.get("9F27").equals("40")){
                        // TODO: offline approved transaction processing, need to discuss future for offline approved case 01/31/2018
                        // TODO:  now we do online approve
                        HashMap<String, String> swipe_data = parseEasylinkSwipeData();
                        card_number = swipe_data.get(APPConstants.CARDNUM);
                        card_expired_date = swipe_data.get(APPConstants.EXPDATE);
                        card_mag_data = swipe_data.get(APPConstants.MAGDATA);

                        threadMsg("Transaction Success");
                        Log.i("cathis si 9F27:" , "9f27 == 0");
                        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);

                    }else if(transData.get("9F27").equals("")){
                        // DEMO version for EMV

                        HashMap<String, String> swipe_data = parseEasylinkSwipeData();
                        card_number = swipe_data.get(APPConstants.CARDNUM);
                        card_expired_date = swipe_data.get(APPConstants.EXPDATE);
                        card_mag_data = swipe_data.get(APPConstants.MAGDATA);
                        threadMsg("Transaction Success");
                        Log.i("cathis si 9F27:" , "9f27 == 0");
                        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);

                    }
                } else if (global_var.checkCardType(APPConstants.TAP)){   // Contactless EMV
                    HashMap<String, String> swipe_data = parseEasylinkSwipeData();
                    card_number = swipe_data.get(APPConstants.CARDNUM);
                    card_expired_date = swipe_data.get(APPConstants.EXPDATE);
                    card_mag_data = swipe_data.get(APPConstants.MAGDATA);
                    System.out.println("card_mag_data:" + card_mag_data);
                    threadMsg("Transaction Success");
                    CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.SUCCESS);
                }

            } // End of run


            private void threadMsg(String msg) {
                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            @SuppressLint("HandlerLeak")
            private final Handler handler = new Handler() {

                public void handleMessage(Message msg) {
                    String aResponse = msg.getData().getString("message");
                    System.out.println("aResponse:" + aResponse);
                    if (aResponse.equals("Card_number_error")){

                    } else if (aResponse.equals("Cancel_card_swipe")) {
                        System.out.println("the cancel swipe");
                    }else{
                        boolean setCardNumber;

                        setCardNumber = checkCard();
                        if(setCardNumber){
                            global_var.setCardNumber(card_number);
                            global_var.setExpiredDate(card_expired_date);
                            global_var.setMagData(card_mag_data);

                            System.out.print("This is t global_var.getCardNumber:" +  global_var.getCardNumber());
                            System.out.print("This is t card_expired_date:" +  global_var.getExpiredDate());
                            intent = new Intent(mPaymentActivity.getBaseContext(), PinActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            System.out.print("This is transData in ChooseCardActivity:" + transData);
                            // finish this activity

                            intent.putExtra("transData", transData);
                            mPaymentActivity.startActivity(intent);
                            // after get data ,then finish this activity  very important to avoid "swipe/insert/tap" in D180S
                            mPaymentActivity.finish();
                        }
                    }
                }
            };

        }).start();
    } // End Transprocess

    private void endTransProcess(int errorCode){
        if (mPaymentActivity != null){
            mPaymentActivity.finish();
        }
        disTransFailed(errorCode);
        return;
    } // End endTransProcess

    private void disTransFailed(int result) {
        eventBusResponseResult.setResultMsg(ResponseCode.getRespCodeMsg(result));
        CustomEventBus.doEvent(EventBusResponseResult.ResultEvent.FAILED);
    } // End disTransFailed

    private int goMSRBranch() {
        // customized tag "0305" -- track2 data
        ByteArrayOutputStream trackData = new ByteArrayOutputStream();
        int result = easyLink.getData(DataModel.DataType.CONFIGURATION_DATA, transDataTag.trackDataTagList(), trackData);
        Log.i(TAG, "get track2: ret = " + result + "  " + Convert.Bytes2HexString(trackData.toByteArray()));
        String track2_row = trackData.toString();
        System.out.println("card_track2_row:"+track2_row);
        System.out.println("card_string after track2:"+card_string);
        if (result != ResponseCode.EL_RET_OK) {
            return result;
        }

        transData.putAll(Convert.ParseTLVData(DataModel.DataType.CONFIGURATION_DATA, trackData.toByteArray()));

        String track2 = CardCheck.parseTrack2Number(track2_row);

        System.out.println("cintrack2_num:"+track2);

        String track_data_row = Convert.Bytes2HexString(trackData.toByteArray());
        Log.i("TEST INFO", "trackData.toByteArray():" + trackData.toByteArray());
        Log.i("TEST INFO", "track_data_row:" + track_data_row);

        Log.i("TEST INFO", "track 2: " + track_data_row);
        transData.put("0305", track2);


        if(global_var.getEDCType().equals(APPConstants.DEBIT) || global_var.getEDCType().equals(APPConstants.EBT)){
            int pin_ret = inputPinInfo();
        }
        return 0;
    } // End goMSRBranch

    public HashMap<String, String> parseEasylinkSwipeData(){

        HashMap<String, String> cardDict=new HashMap<>();
        String track2Data;

        if ( global_var.checkCardType(APPConstants.SWIPE) ||
                global_var.checkCardType(APPConstants.FALLBACK) ||
                global_var.checkCardType(APPConstants.TAP)){
            // track2Data = new String(Convert.HexString2Bytes(transData.get("0305")));
            track2Data =transData.get("0305");
        } else if (global_var.checkCardType(APPConstants.INSERT)){
            track2Data = transData.get("57").replace('D', '=');
            track2Data = track2Data.replace("F","");
        } else {
            return cardDict;
        }

        if (track2Data != null && !track2Data.isEmpty() && !track2Data.equals("null")){
            try{
                Log.i(TAG, "#########################Track 2 Data = " + track2Data);
                global_var.setTrack2data(track2Data);
                String str2[] = track2Data.split("=");
                Log.i(" str2[]", str2.toString());
                Log.i("str2[1].substring(0,4):", str2[1].substring(0, 4));
                Log.i(" str2[0]", str2[0]);
                String expire_str = str2[1].substring(0, 4);
                String expire_date =  expire_str.substring(2, 4) + expire_str.substring(0, 2);

                Log.i("expire_date:", expire_date);
                String card_number = str2[0].replaceAll("[^0-9]", "");
                cardDict.put(APPConstants.CARDNUM, card_number);
                cardDict.put(APPConstants.EXPDATE, expire_date);
                cardDict.put(APPConstants.MAGDATA, card_number + "=" + str2[1].substring(0, 4));
            }catch(java.lang.ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else {
            // promp error
        }

        return cardDict;
    } // parseEasylinkSwipeData

    private int goNotMSRBranch() {

        global_var.setEMVStatus(true);
        //get trans result (including all EMV tags)
        ByteArrayOutputStream emvTLV = new ByteArrayOutputStream();
        int result = easyLink.getData(DataModel.DataType.TRANSACTION_DATA, transDataTag.EMVTagList(), emvTLV);
        Log.i("TEST INFO", "tag list = " + Convert.Bytes2HexString(transDataTag.EMVTagList()));
        Log.i("TEST INFO", "EMV get trans result: ret = " + result);
        Log.i("TEST INFO", "Output Stream Length = " + emvTLV.toByteArray().length + ", context = " + Convert.Bytes2HexString(emvTLV.toByteArray()));
        if (result != ResponseCode.EL_RET_OK &&
                (result != ResponseCode.EL_PARAM_RET_PARTIAL_FAILED || emvTLV.toByteArray().length == 0)) {
            Log.e(TAG, "Get EMV Data Failed!");
            return result;
        }
        String EMV_data_row = Convert.Bytes2HexString(emvTLV.toByteArray()).substring(4);
        Log.i("TEST INFO", "mvTLV.toByteArray()).substring(4):" + emvTLV.toByteArray());
        Log.i("TEST INFO", "EMV_data_row:" + EMV_data_row);

        transData.putAll(Convert.ParseTLVData(DataModel.DataType.TRANSACTION_DATA, emvTLV.toByteArray()));

        Log.i("TEST INFO", "transData:" + transData);

        // default value 9F39 value is 05
        // todo this is hard code, need to change it later, 9F39, 9F03 is zero,
//        StringBuilder sb = new StringBuilder(transData.get("9F39"));
        EMV_data_row = EMV_data_row.replace("9F3900", "9F390105");
        // default value 9f03 value is 00000000
//        StringBuilder sb1 = new StringBuilder(transData.get("9F03"))
        EMV_data_row = EMV_data_row.replace("9F0300", "9F0306000000000000");
        // delete 5F34
        StringBuilder pan_sequence = new StringBuilder(transData.get("5F34"));
        EMV_data_row = EMV_data_row.replace("5F3400"+pan_sequence.toString(), "");

        StringBuilder sb = new StringBuilder(transData.get("57"));
        Log.i("TEST INFO", "track 2 equivalent data: " + sb.toString());

        // delete track2 data in EMV_DATA
        String emv_data = EMV_data_row.replace("5711"+sb.toString(), "");

        transData.put("EMV_DATA", emv_data);
        System.out.println("The transData:" + transData);


        sb.setCharAt(sb.indexOf("D"), '=');
        while (sb.lastIndexOf("F") == sb.length()-1){
            sb.deleteCharAt(sb.length()-1);
        }
        Log.i("TEST INFO", "track 2: " + sb.toString());
        transData.put("0305", sb.toString());
        Log.i("TEST INFO", "transData 2: " + transData);
        if(global_var.getEDCType().equals(APPConstants.DEBIT) || global_var.getEDCType().equals(APPConstants.EBT)){
            int pin_ret = inputPinInfo();
        }

        return 0;
    } // End of goNotMSRBranch

    private int inputPinInfo(){
        // 0203 -- PIN Encryption Key Index
        ByteArrayOutputStream getParamDataTags = new ByteArrayOutputStream();
        System.out.println("transDataTag.PINEntryParams-----:"+transDataTag.PINEntryParams().toString());
        int result = easyLink.setData(DataModel.DataType.CONFIGURATION_DATA, transDataTag.PINEntryParams(), getParamDataTags);
        if (result != ResponseCode.EL_RET_OK) {
            return result;
        }
        // get pin block
        ByteArrayOutputStream pinBlock = new ByteArrayOutputStream();
        ByteArrayOutputStream ksn = new ByteArrayOutputStream();

        // set pan
        Log.i("TEST INFO", "transData 2 in pin: " + transData);
        // String track2Data = new String(Convert.HexString2Bytes(transData.get("0305")));
        String track2Data =transData.get("0305");
        String str2[] = track2Data.split("=");
        String card_number = str2[0].replaceAll("[^0-9]", "");
        Log.i(TAG, "get card_number  : = " + card_number);

        // pin block getting
        int pin_ret = easyLink.getPinBlock(card_number, pinBlock, ksn);

        System.out.println("the pin_ret:" + pin_ret);
        Log.i(TAG, "get pinBlock raw : = " + pinBlock.toByteArray());
        Log.i(TAG, "get pinBlock raw toString: = " + pinBlock.toString());
        Log.i(TAG, "get pinBlock: = " + Convert.Bytes2HexString(pinBlock.toByteArray()));
        Log.i(TAG, "get ksn raw toByteArray:  = " + ksn.toByteArray());
        Log.i(TAG, "get ksn raw toString:  = " + ksn.toString());
        Log.i(TAG, "get ksn:  = " + Convert.Bytes2HexString(ksn.toByteArray()));

        global_var.setPinBlock( Convert.Bytes2HexString(pinBlock.toByteArray()));
        global_var.setKSN(Convert.Bytes2HexString(ksn.toByteArray()));

        return pin_ret;

    } // End of inputPinInfo


    public boolean checkCard(){

        boolean setCardNumber = true;

        // 1. check expiration date
        CardCheck card_check_obj = new CardCheck();
        System.out.println("card_expired_date:"+card_expired_date);
        if (!card_check_obj.checkExpireationTime(card_expired_date)){
            if(!APPConstants.INSERT.equalsIgnoreCase(global_var.getCardChooseOption())){
                // todo: if insert card for EMV purpose, we not check expiration date  for collis AMX case010
               // DialogClass.showPromptDialog(mPaymentActivity.getBaseContext(),getString(R.string.prompt_expiration_date), intentInput);
                setCardNumber = false;
            }

        }

        System.out.println("setCardNumber after expiration time:" + setCardNumber);

        // 2. check card type abd trans typesave it into card type global
        String card_type_info = card_check_obj.getCardBrandType(card_number);
        String edc_type = global_var.getEDCType();
        System.out.println("card_type_info):"+ card_type_info);
        System.out.println("sys_var.map.entrySet():"+ sys_var.map.entrySet());
        for(HashMap.Entry<String, String> entry : sys_var.map.entrySet()){
            String key = entry.getKey();
            System.out.println("key):"+ key);

            // card type
            if(key.equals(card_type_info)){
                System.out.println("card_type_info:"+ card_type_info);
                String status_info = entry.getValue();
                System.out.println("status_info:++++:"+status_info);
                System.out.println("status_info.equals(APPConstants.disable)" + status_info.equals(APPConstants.disable));
                // card type
                if (status_info.equals(APPConstants.disable)){
                    setCardNumber = false;
                    System.out.println("thisis----------");
                   // DialogClass.showPromptDialog(ChooseCardActivity.this, card_type_info + "  Card is Not Allowed ", intentInput);

                }
            }

            // EDC tyoe
            if(key.equals(edc_type)){
                String status = entry.getValue();
                System.out.println("status:++++:"+status);
                System.out.println("status.equals(APPConstants.disable)" + status.equals(APPConstants.disable));

                if(status.equals(APPConstants.disable)){
                    setCardNumber = false;
                    System.out.println("thisis----------");
                   // DialogClass.showPromptDialog(ChooseCardActivity.this, edc_type.toUpperCase() + " is Not Support", intentInput);
                }
            }
        }

        global_var.SPMap.put(APPConstants.CardType, card_type_info);
        global_var.setCardType(card_type_info);

        // 3. check track1, track2 checksum bin range
        System.out.println("The card_number :" + card_number);
        if(!card_check_obj.checkSumCardInfo(card_number)){

            //DialogClass.showPromptDialog(ChooseCardActivity.this, getString(R.string.prompt_card_number_not_invalid));
            setCardNumber = false;
        }
        System.out.println("setCardNumber after bing range:" + setCardNumber);



        return setCardNumber;
    } // End checkCard
}

