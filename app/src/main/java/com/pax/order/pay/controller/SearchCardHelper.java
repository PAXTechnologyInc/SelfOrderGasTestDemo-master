package com.pax.order.pay.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.pax.order.FinancialApplication;
import com.pax.order.R;
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
import com.pax.order.logger.AppLog;

/**
 * Created by Leon on 2017/8/15.
 */

public class SearchCardHelper {
    private static final String TAG = "SearchCardHelper";
    private PollingResult pollingResult;
    private Thread searchCardThread;
    private String entryMode;
    private MagReader magReader;
    private IccReader iccReader;
    private PiccReader piccReader;
    private boolean isSupportInsert = false;
    private boolean isSupportTap = false;
    private boolean isSupportSwipe = false;
    private TransTimeout transTimeout;
    private boolean isStopPolling = false;
    private boolean isTrack2Mandatory = false;
    private boolean isIccDisplay = false;
    private boolean isEnableSwipe = false;
    private boolean isDetectCard = false;
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
            AppLog.e("SearchMode = 0");
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
                                } else {
                                    pollingResult.setReaderType(EReaderType.ICC);
                                    readCardOK(cardCallback);
                                    break;
                                }
                            }
                        } else {
                            isDetectCard = false;
                        }

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
                            AppLog.e(e.getMessage());
                            String errMsg = e.getErrMsg();
                            System.out.println("This is errMeg:" + errMsg);

                            if (errMsg != null && errMsg.contains("too many card")) {
                                // Fix AXP EP004
                                cardCallback.onReadCardError(TransResult.ERR_TOO_MANY_CARDS);
                                break;
                            }
                        }
                    } else {
                        if (isSupportInsert && iccReader.detect((byte) 0)) {
                            //Fix ANDROIDNAT-365
                            byte[] ret = iccReader.init((byte) 0);
                            // if (ret == null){
                            pollingResult.setReaderType(EReaderType.ICC);
                            readCardOK(cardCallback);
                            break;
                            //}
                        }

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
                            AppLog.e(e.getMessage());
                            String errMsg = e.getErrMsg();

                            if (errMsg != null && errMsg.contains("too many card")) {
                                // Fix AXP EP004
                                cardCallback.onReadCardError(TransResult.ERR_TOO_MANY_CARDS);
                                break;
                            }
                        }
                    }  //Aries8
                }

                toast(context,context.getString(R.string.remove_card));
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
        }
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
