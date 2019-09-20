package com.pax.order.pay.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.pax.dal.exceptions.IccDevException;
import com.pax.dal.exceptions.MagDevException;
import com.pax.order.FinancialApplication;
import com.pax.order.R;
import com.pax.order.adapter.CardReaderHelper;
import com.pax.order.adapter.IccReader;
import com.pax.order.adapter.MagReader;
import com.pax.order.adapter.PiccReader;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.entity.EReaderType;
import com.pax.dal.entity.PollingResult;
import com.pax.dal.entity.TrackData;
import com.pax.dal.exceptions.PiccDevException;
//import com.pax.device.Device;
//import com.pax.pay.action.SearchCardAction;
//import com.pax.pay.component.Component;
import com.pax.order.util.TransTimeout;
//import com.pax.order.eventbus.EventBusUtil;
import com.pax.order.eventbus.SearchCardEvent;
import com.paxus.common.type.TransResult;
//import com.pax.order.logger.AppLog;

import java.util.logging.Logger;

/**
 * Created by Leon on 2017/8/15.
 */

public class SearchCardHelper {
    private static final String TAG = "SearchCardHelper";
    private PollingResult pollingResult;
    private PollingResult result;
    private Thread searchCardThread;
    private String entryMode;
    private MagReader magReader;
    private IccReader iccReader;
    private PiccReader piccReader;
    private boolean isSupportInsert = true;
    private boolean isSupportTap = true;
    private boolean isSupportSwipe = true;
    private TransTimeout transTimeout;
    private boolean isStopPolling = false;
    private boolean isTrack2Mandatory = false;
    private boolean isIccDisplay = false;
    private boolean isEnableSwipe = false;
    private boolean isDetectCard = false;
    private boolean isRemoveCard = false;
    private Context context;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            // This is where you do your work in the UI thread.
            // Your worker tells you in the message what to do.
        }
    };

    public SearchCardHelper(Context ctext, String mode, long timeout) {
        context = ctext;
        setEntryMode(mode);
        magReader = MagReader.getInstance();
        iccReader = IccReader.getInstance();
        piccReader = PiccReader.getInstance(EPiccType.INTERNAL);
        transTimeout = new TransTimeout(timeout);
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
        isSupportSwipe = true;
        isSupportInsert = true;
        isSupportTap = true;
    }

    public void setTrack2Mandatory(boolean track2Mandatory) {
        isTrack2Mandatory = track2Mandatory;
    }

    public void closePolling() {
        closePolling(false);
    }

    public void closePolling(boolean keepPiccOpen) {
        closeIcc();

        closeMag();

        if (!keepPiccOpen) {
            closePicc();
        }
        isStopPolling = true;
        if (searchCardThread != null) {
            searchCardThread.interrupt();
        }
    }

    //If Never open a reader, do not close it.
    //Fix ANDROIDNAT-256
    //DetectCardService onStop is called after SearchCardActivity Show Up
    //When do BALANCE transaction and Go3Gift Activate transaction
    private void closeIcc() {
        if (isSupportInsert && iccReader != null) {
            iccReader.close((byte) 0);
        }
    }

    private void closeMag() {
        if (isSupportSwipe && magReader != null) {
            magReader.close();
        }
    }

    private void closePicc() {
        if (isSupportTap && piccReader != null) {
            piccReader.close();
        }
    }

    public void start(final SearchCardCallback cardCallback) {
        if (!isSupportSwipe && !isSupportInsert && !isSupportTap) {
//            AppLog.e("SearchMode = 0");
            return;
        }

        System.out.println("This is star to detect card");

        if (isSupportSwipe) {
            magReader.open();
            magReader.reset();
        }

        // 支持非接
        if (isSupportTap) {
            piccReader.open();
//            App.getApp().runOnUiThreadDelay(() -> EventBusUtil.doEvent(new SearchCardEvent(SearchCardEvent.Status.CLSS_LIGHT_STATUS_READY_FOR_TXN)), 200);
        }
        isStopPolling = false;
        searchCardThread = new Thread() {
            public void run() {
                pollingResult = new PollingResult();
                transTimeout.start();
                while (!isStopPolling && !searchCardThread.isInterrupted()) {
                    if (transTimeout.isTimeout()) {
                        System.out.println("this is time out");

                        closePolling();
                        cardCallback.onReadCardError(TransResult.ERR_TIMEOUT);
                        break;
                    }
                    // Arias 8 support one insert port with both icc and mrs
                    if (true) {
//                        System.out.println("This is iccdecte:" + iccReader.detect((byte) 0));
                        if (isSupportInsert && iccReader.detect((byte) 0)) {
                            byte[] ret = iccReader.init((byte) 0);
                            if (ret != null) {
                                pollingResult.setReaderType(EReaderType.ICC);
                                readCardOK(cardCallback);
                                break;
                            } else {
                                if (isSupportSwipe) {
                                    if (!isDetectCard) {
                                        isDetectCard = true;
                                        isIccDisplay = true;
                                    }
                                    readCardOK(cardCallback);
                                    break;
                                } else {
                                    pollingResult.setReaderType(EReaderType.ICC);
                                    readCardOK(cardCallback);
                                    break;
                                }
                            }
                        } else {
                            isDetectCard = false;
                        }
//                        System.out.println("This is magReader sw:" + magReader.isSwiped());
                        if (isSupportSwipe && magReader.isSwiped()) {
                            System.out.println("It appears after Looper.myLooper().quit()");

                            TrackData info = magReader.read();
                            if (info != null) {
                                magReader.reset();
                                if (isTrack2Mandatory && TextUtils.isEmpty(info.getTrack2())) {
                                    cardCallback.onReadCardError();
                                    continue;
                                }
                                pollingResult.setReaderType(EReaderType.MAG);
                                pollingResult.setTrack1(info.getTrack1());
                                pollingResult.setTrack2(info.getTrack2());
                                pollingResult.setTrack3(info.getTrack3());
                                readCardOK(cardCallback);
                                break;
                            }
                        }

                        if (isSupportSwipe && isDetectCard && isIccDisplay) {
//                            cardCallback.onReadCardError(TransResult.ERR_REMOVE_CARD);
                            isIccDisplay = false;
                        }

                        try {
                            Object piccResult = piccReader.detect(EDetectMode.ISO14443_AB);
                            System.out.println("This is picc sw:" + piccResult);
                            if (isSupportTap && null != piccResult) {
                                SystemClock.sleep(500);
                                if (magReader.isSwiped()) {
                                    continue;
                                }
                                pollingResult.setReaderType(EReaderType.PICC);
                                readCardOK(cardCallback);
                                break;
                            }
                        } catch (PiccDevException e) {
//                            AppLog.e(e.getMessage());
                            String errMsg = e.getErrMsg();
                            System.out.println("This is errMeg:" + errMsg);

                            if (errMsg != null && errMsg.contains("too many card")) {
                                // Fix AXP EP004
                                cardCallback.onReadCardError(TransResult.ERR_TOO_MANY_CARDS);
                                break;
                            }
                        }
                    } else {
//                        System.out.println("This is icc sw1:" + iccReader.detect((byte) 0));
                        if (isSupportInsert && iccReader.detect((byte) 0)) {
                            //Fix ANDROIDNAT-365
                            byte[] ret = iccReader.init((byte) 0);
                            // if (ret == null){
                            pollingResult.setReaderType(EReaderType.ICC);
                            readCardOK(cardCallback);
                            break;
                            //}
                        }
//                        System.out.println("This is mag read sw1:" + magReader.isSwiped());
                        if (isSupportSwipe && magReader.isSwiped()) {
                            TrackData info = magReader.read();
                            if (info != null) {
                                SystemClock.sleep(500);
                                if (iccReader.detect((byte) 0)) {
                                    continue;
                                }

                                magReader.reset();
                                if (isTrack2Mandatory && TextUtils.isEmpty(info.getTrack2())) {
                                    cardCallback.onReadCardError();
                                    continue;
                                }
                                pollingResult.setReaderType(EReaderType.MAG);
                                pollingResult.setTrack1(info.getTrack1());
                                pollingResult.setTrack2(info.getTrack2());
                                pollingResult.setTrack3(info.getTrack3());
                                readCardOK(cardCallback);
                                break;
                            }
                        }

                        try {
//                            System.out.println("This ispiccReader.detect(EDetectMode.ISO14443_AB)1:" + piccReader.detect(EDetectMode.ISO14443_AB));
                            if (isSupportTap && null != piccReader.detect(EDetectMode.ISO14443_AB)) {
                                SystemClock.sleep(500);
                                if (magReader.isSwiped()) {
                                    continue;
                                }
                                pollingResult.setReaderType(EReaderType.PICC);
                                readCardOK(cardCallback);
                                break;
                            }
                        } catch (PiccDevException e) {
//                            AppLog.e(e.getMessage());
                            String errMsg = e.getErrMsg();

                            if (errMsg != null && errMsg.contains("too many card")) {
                                // Fix AXP EP004
                                cardCallback.onReadCardError(TransResult.ERR_TOO_MANY_CARDS);
                                break;
                            }
                        }
                    }  //Aries8
                }

//                toast(context,context.getString(R.string.remove_card));
                String track1 = pollingResult.getTrack1();
                String track2 = pollingResult.getTrack2();
                System.out.println("This is track2:" + track2);
            }
        };
        searchCardThread.start();
    }


    public void toast(final Context context, final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void removeCardOK(final SearchCardCallback cardCallback) {
        transTimeout.stopTimer();


        if (result.getReaderType() == EReaderType.MAG) {
            closePolling();
            cardCallback.onReadCardOk("MSR Removed");
        } else if (result.getReaderType() == EReaderType.ICC) {
            closeMag();
            closePicc();
            cardCallback.onReadCardOk("ICC Removed");
//                cardCallback.onReadCardOk(new SearchCardAction.CardInformation(SearchCardAction.SearchMode.INSERT));
        } else if (result.getReaderType() == EReaderType.PICC) {
            closeIcc();
            closeMag();
            cardCallback.onReadCardOk("PICC Removed");
//                cardCallback.onReadCardOk(new SearchCardAction.CardInformation(SearchCardAction.SearchMode.TAP));
        }else{
            cardCallback.onReadCardOk("default Removed");
        }
    }

    private void readCardOK(final SearchCardCallback cardCallback) {
        transTimeout.stopTimer();

        String track1 = pollingResult.getTrack1();
        String track2 = pollingResult.getTrack2();
        System.out.println("This is track2:"+track2);


        if (pollingResult.getReaderType() == EReaderType.MAG) {
            closePolling();
            cardCallback.onReadCardOk(track2+":MSR");
        } else if (pollingResult.getReaderType() == EReaderType.ICC) {
            closeMag();
            closePicc();
            cardCallback.onReadCardOk(track2+":ICC");
//                cardCallback.onReadCardOk(new SearchCardAction.CardInformation(SearchCardAction.SearchMode.INSERT));
        } else if (pollingResult.getReaderType() == EReaderType.PICC) {
            closeIcc();
            closeMag();
            cardCallback.onReadCardOk(track2+":PICC");
//                cardCallback.onReadCardOk(new SearchCardAction.CardInformation(SearchCardAction.SearchMode.TAP));
        }else{
            cardCallback.onReadCardOk(track2+":Def");
        }
    }

    public void isCardRemoved(final SearchCardCallback cardCallback){

//        isDetectCard = true;

        System.out.println("This is star to detect remove card");
        if (!isSupportSwipe && !isSupportInsert && !isSupportTap) {
//            AppLog.e("SearchMode = 0");
            return;
        }

        System.out.println("This is star to detect card");

        if (isSupportSwipe) {
            magReader.open();
            magReader.reset();
        }

        // 支持非接
        if (isSupportTap) {
            piccReader.open();
//            App.getApp().runOnUiThreadDelay(() -> EventBusUtil.doEvent(new SearchCardEvent(SearchCardEvent.Status.CLSS_LIGHT_STATUS_READY_FOR_TXN)), 200);
        }
        isStopPolling = false;

        searchCardThread = new Thread() {
            public void run() {
                pollingResult = new PollingResult();
                transTimeout.start();
                while (true) {
                    if (transTimeout.isTimeout()) {
                        System.out.println("this is time out");
                        closePolling();
                        cardCallback.onReadCardError(TransResult.ERR_TIMEOUT);
                        break;
                    }
                    try{
//                        if(null !=piccReader.detect(EDetectMode.ISO14443_AB)){
//                            break;
//                        }
                        SystemClock.sleep(1000);
                        Object result = piccReader.detect(EDetectMode.ISO14443_AB);
                        boolean iccRemove = iccReader.detect((byte) 0);
//                        boole
                        System.out.println("icc:" + iccReader.detect((byte) 0));
                        System.out.println("magReader:" + magReader.isSwiped());
                        System.out.println("picc remove:" + result);

                        if(null == result){
                            break;
                        }
                    }catch (Exception e){
                        System.out.println("This is detect error");
                    }


                }
            }
        };
        searchCardThread.start();
    }

    public void warnRemove(final SearchCardCallback cardCallback){
        searchCardThread = new Thread() {
            public void run() {
                System.out.println("This is -------------------------------");
                try {
                    int count = 0;
                    while (true) {
                        //don't use 60*1000, because of each action end will check this function, and waiting for timeout
                        //helper = App.getDal().getCardReaderHelper();
                        //result = helper.polling(EReaderType.ICC_PICC, 60*1000);
                        System.out.println("This is warn");
                        result = CardReaderHelper.getInstance().polling(EReaderType.ICC_PICC, 100);
                        System.out.println("The result:"+result);
                        if (result == null) {
//                            Logger.e("NULL");
                            removeCardOK(cardCallback);
                            break;
                        }
                        EReaderType readerType = result.getReaderType();
                        System.out.println("The readerType:"+readerType.getEReaderType());
                        if (readerType == EReaderType.ICC || readerType == EReaderType.PICC) {
                            System.out.println("this is readerType" + readerType);
////                            if (transProcessListener != null)
////                                transProcessListener.onShowWarn(message);
//                            SystemClock.sleep(500);
//                            String mode = App.getSysParam().get(SysParam.StringParam.EMV_CARD_REMOVE_BEEP);
//                            if ((EmvRemoveCardBeep.ONE_BEEP.endsWith(mode) && count == 0) || EmvRemoveCardBeep.CONTINUOUS_BEEP.endsWith(mode)) {
////                                Device.beepRemoveCard();
//                                count = 1;
//                                if (readerType == EReaderType.PICC) {
//                                    com.pax.utils.eventbus.EventBusUtil.doEvent(new SearchCardEvent(SearchCardEvent.Status.CLSS_LIGHT_STATUS_ERROR));
//                                }
//                            }
                        } else {
                            removeCardOK(cardCallback);
                            break;
                        }
                    }
                } catch (MagDevException | IccDevException | PiccDevException e) {
//                    Logger.e(e.getMessage());
                    System.out.println("This is exception"+e.getMessage());
                }
            }
        };
        searchCardThread.start();
    }

    public PollingResult getPollingResult() {
        return pollingResult;
    }

    public interface SearchCardCallback {

        void onReadCardError(int errorCode);

        void onReadCardOk(String track2);

        void onReadCardError();

    }

}
