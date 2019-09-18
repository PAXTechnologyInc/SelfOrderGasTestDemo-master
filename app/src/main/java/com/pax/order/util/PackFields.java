package com.pax.order.util;

import android.util.Log;

import com.pax.order.constant.APPConstants;
import com.pax.order.constant.GlobalVariable;
import com.pax.order.constant.SystemVariable;

import java.util.HashMap;

/**
 * Created by Terry on 2/22/2018.
 * This is packing field before do http post
 */

public class PackFields {



    private GlobalVariable global_var;
    private HashMap<String, String>  transData;
    private SystemVariable sys_var;
    private HashMap<String, String> reversal_map = new HashMap<String, String>();
    private String reversal_ext_data;
    private String ext_data;

    public PackFields(GlobalVariable globalVar, HashMap<String, String> transData, SystemVariable sys_var) {
        this.global_var = globalVar;
        this.transData = transData;
        this.sys_var = sys_var;

    }

    public HashMap<String, String> getReversalMap() {
        return reversal_map;
    }

    public GlobalVariable getGlobalVar() {
        return global_var;
    }


    // pre-packing data before go through Soundpayment API
    public void soundPaymentAPIDataPack(){

        // 1. get card info
        String emvTag = "";
        String base_amount = global_var.getBaseAmount();
        String tip_amount = global_var.getTipAmount();
        System.out.println("gltip_amount :"+tip_amount);
        String card_number = global_var.getCardNumber();

        String domain = global_var.SPMap.get(APPConstants.DOMAIN);
        String username = global_var.SPMap.get(APPConstants.USERNAME);
        String password = global_var.SPMap.get(APPConstants.PASSWORD);
        String expired_date = global_var.getExpiredDate();
        String magdata = global_var.getMagData();
        String EDC_type = global_var.getEDCType().toUpperCase();
        String transaction_type = global_var.getTransactionType().toUpperCase();
        Log.i("The transaction_type:", transaction_type);
        Log.i("The card_number global:", card_number);
        Log.i("the card_expire global", "" + expired_date);
        Log.i("the card_mag text", "" + magdata);
        System.out.println("transData :" + transData.toString());

        // ext data
        packingExtData();

        // 2. get data and do tpi
        global_var.SPMap.put(APPConstants.DOMAIN, domain);
        global_var.SPMap.put(APPConstants.USERNAME, username);
        global_var.SPMap.put(APPConstants.PASSWORD, password);
        // global_var.SPMap.put(APPConstants.PosID, "TestPosID2");
        global_var.SPMap.put(APPConstants.TerminalID, "00000001");
        global_var.SPMap.put(APPConstants.SerialNum, "53051998");
        global_var.SPMap.put(APPConstants.CashBackAmt, global_var.getCashbackAmount());

        global_var.SPMap.put(APPConstants.TransType, transaction_type);
        global_var.SPMap.put(APPConstants.Amount, base_amount);
        global_var.SPMap.put(APPConstants.TipAmt, tip_amount);
        // global_var.SPMap.put(APPConstants.Zip, "32225");
        // global_var.SPMap.put(APPConstants.Street, "TEST");
        // TODO: BEFORE IS 1
        global_var.SPMap.put(APPConstants.ECRRefNum, "1");
        global_var.SPMap.put(APPConstants.ECRTransID, "10");
        global_var.SPMap.put(APPConstants.EXTDATA, ext_data);

        // card type
        global_var.SPMap.put(APPConstants.CARDTYPE, null);

        //timestamp
        global_var.SPMap.put(APPConstants.TimeStamp, global_var.getTransactionTime());

        // EBT Voucher
        if (APPConstants.VOUCHER.equalsIgnoreCase(global_var.getEbtFullType()))
            global_var.SPMap.put(APPConstants.EBTFoodStampVoucher, "T");
        global_var.SPMap.put(APPConstants.VoucherNum, global_var.getVoucherNumber());

        global_var.SPMap.put(APPConstants.AUTHCODE, global_var.getVoucherAuthNum());
        // swipe
        if(!global_var.getSwipCardStatus()){
            global_var.SPMap.put(APPConstants.Account, global_var.getCardNumber());
            global_var.SPMap.put(APPConstants.ExpDate, global_var.getExpiredDate());
            global_var.SPMap.put(APPConstants.CVV, global_var.getCVV());
        }

        // ebt
        if(global_var.getEDCType().equals(APPConstants.EBT)){
            String ebt_str = global_var.getEbtFullType().replace(" ","").toUpperCase();
            if(global_var.getEbtFullType().equals(APPConstants.VOUCHER)){
                global_var.SPMap.put(APPConstants.TenderType, "EBT" );
            }else {
                global_var.SPMap.put(APPConstants.TenderType, EDC_type + "_" + ebt_str);
            }
        }else{
            global_var.SPMap.put(APPConstants.TenderType, EDC_type);
        }

        // EMV check AID to change EDC type with debit
        checkGlobalAID(global_var.getEMVStatus());


        // finally, pack reversal version of data
        packingReversalData();

    }

    private void packingReversalData(){

        for(String key:global_var.SPMap.keySet()) {

            // reversal then pan set into account

            if(APPConstants.Account.equals(key)){


                reversal_map.put(key, global_var.getCardNumber());
                continue;
            }

            if(APPConstants.ExpDate.equals(key)){
                reversal_map.put(APPConstants.ExpDate, global_var.getExpiredDate());
                continue;
            }

            if(APPConstants.TransType.equals(key)){
                reversal_map.put(key, APPConstants.REVERSAL.toUpperCase());
                continue;
            }

            if(APPConstants.TimeStamp.equals(key)){
                reversal_map.put(key, global_var.getTransactionTime());
                continue;
            }

            if(APPConstants.OrigTransDate.equals(key)){
                reversal_map.put(key, global_var.getTransactionTime().substring(0,8));
                continue;
            }

            if(APPConstants.OrigTransTime.equals(key)){
                reversal_map.put(key, global_var.getTransactionTime().substring(8));
                continue;
            }

            if(APPConstants.EXTDATA.equals(key)){
                reversal_map.put(key, reversal_ext_data);
                continue;
            }
            reversal_map.put(key, global_var.SPMap.get(key));
        }

        System.out.println("The reversal_map:"+ reversal_map.toString());
    }


    private void packingExtData(){
        String emvTag = "";
        String card_type = global_var.getCardType();
        System.out.println("card_type:"+card_type);
        // 1. deal with pinblock and ksn
        String pinStr = "";
        System.out.println("global_var.getPinBlock():"+global_var.getPinBlock());
        if (global_var.getPinBlock() != null){
            pinStr = "<PinBlock>"+ global_var.getPinBlock() +"</PinBlock><KSN>"+ global_var.getKSN()  +"</KSN>";
            // pinStr = "<PinBlock>8D6474E2FA1C65BC</PinBlock><KSN>F876543210000020000B</KSN>";
        }else{
            pinStr = "</PinBlock></KSN>";
        }

        // 2. EMV
        if (transData.get("EMV_DATA") != null){
            if(global_var.getEmvInsertTag().equals(APPConstants.HALF)){
                emvTag = "</EMVDATA>";
            }else{
                emvTag = "<EMVData>"+ transData.get("EMV_DATA") +"</EMVData>";
            }

        }else{
            emvTag = "</EMVDATA>";
        }

        // 3. P2PE
        String p2pe;
        if(sys_var.map.get(APPConstants.PTPE).equals(APPConstants.disable)){
            global_var.SPMap.put(APPConstants.TokenRequest, null);
            p2pe = "<P2peMode>N</P2peMode>";
        }else{
            global_var.SPMap.put(APPConstants.TokenRequest, "1");
            p2pe = "<P2peMode>A</P2peMode>";

        }

        // 4. SupportPartialApproval
        String support_party;
        if(sys_var.map.get(APPConstants.PARTIALAPPROVAL).equals(APPConstants.disable)){
            support_party = "<SupportPartialApproval>N</SupportPartialApproval>";
        }else{
            support_party = "<SupportPartialApproval>Y</SupportPartialApproval>";
        }

        // 5. card_type
        String card_type_str = "";
        if (card_type == null ||
                global_var.getEDCType().equals(APPConstants.DEBIT )||
                global_var.getEDCType().equals(APPConstants.EBT)){
            card_type_str = "</CardType>";
        }else{
            card_type_str = "<CardType>" + card_type +"</CardType>";
        }

        // 6. track2
        String track2 = "";
        if(global_var.getTrack2data() == null){
            track2 = "</Track2>";
        }else{
            track2 = "<Track2>"+ global_var.getTrack2data()+"</Track2>";
        }

        // 7 entry model TODO: ADD MORE ENTRY MODEL LATER
        String entryModel = "";
        String entryModelTag = "";
        String pan = "";
        if (global_var.getCardChooseOption().equals(APPConstants.SWIPE)){
            entryModel = "S";
        }else if(global_var.getCardChooseOption().equals(APPConstants.MANUAL)){
            entryModel = "M";
        }else if(global_var.getCardChooseOption().equals(APPConstants.FALLBACK)){
            entryModel = "S";
        }else if(global_var.getCardChooseOption().equals(APPConstants.INSERT)){
            entryModel = "I";
        }else{
            entryModel = "C";
        }
        entryModelTag = "<EntryMode>" + entryModel + "</EntryMode>";

        // 8. reversal fields
        String OrigTranDateTime = "</OrigTranDateTime>";
        String OrigTranType = "</OrigTranType>";
        String OrigEntryMode = "</OrigEntryMode>";
        String OrigSTAN = "</OrigSTAN>";



            // OrigTranDateTime
        String OrigTranDateTimeReversal = "<OrigTranDateTime>" + global_var.getGmtTime()  + "</OrigTranDateTime>";

            // 8.1 OrigTranType
        String OrigTranTypeReversal  = "<OrigTranType>" + global_var.getTransactionType().toUpperCase()  + "</OrigTranType>";

            // 8.2 OrigEntryMode
        String OrigEntryModeReversal  = "<OrigEntryMode>" + entryModel + "</OrigEntryMode>";

            // 8.3 OrigSTAN
        global_var.setOrignalReversalStan(GlobalVariable.STAN);
        String OrigSTANReversal  = "<OrigSTAN>" + global_var.getOrignalReversalStan() + "</OrigSTAN>";


        // 9 finall extension data
        ext_data =
                // "<TimeZone>" + global_var.getTimeZone()+"</TimeZone>" +
                "<GMTTime>" + global_var.getGmtTime() + "</GMTTime>" +
                        "<STAN>"+ GlobalVariable.STAN+"</STAN>"
                        // "<STAN>000010</STAN>"
                        + track2 + card_type_str + entryModelTag +
                        "<ClientRef>9</ClientRef>" + pinStr + emvTag + support_party + p2pe
        + OrigTranDateTime + OrigTranType + OrigEntryMode + OrigSTAN;

        // 10. reversal extension data
        reversal_ext_data = "<GMTTime>" + global_var.getGmtTime() + "</GMTTime>" +
                            "<STAN>"+ GlobalVariable.STAN+"</STAN>" +
                            "</Track2>" + card_type_str + entryModelTag +
                            "<ClientRef>9</ClientRef>" + pinStr + emvTag + support_party + p2pe +
                OrigTranDateTimeReversal + OrigTranTypeReversal + OrigEntryModeReversal + OrigSTANReversal;
    }


    // Check global AID, credit refund ,then change tender type into debit
    private void checkGlobalAID(boolean EMVStatus){

        if(EMVStatus){
            // 1. terminal aid
            if(transData.containsKey(APPConstants.EMVAIDTAG )){
                String term_aid = transData.get(APPConstants.EMVAIDTAG);

                for (String s : global_var.getGlobalAID()) {
                    System.out.println(s);
                    if(term_aid.contains(s)){
                        global_var.SPMap.put(APPConstants.TenderType, APPConstants.DEBIT);
                        break;
                    }
                }
            }

            // 2 if credit refund, change tender type
            if(global_var.getEDCType().equals(APPConstants.CREDIT) && global_var.getTransactionType().equals(APPConstants.REFUND)){
                global_var.SPMap.put(APPConstants.TenderType, APPConstants.DEBIT);
            }
        }
    }


}
