package com.pax.order.constant;

import android.app.Application;

import com.clj.fastble.data.BleDevice;
import com.pax.order.entity.OpenTicket;
import com.paxsz.easylink.api.EasyLinkSdkManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Boolean.FALSE;

/**
 * Created by Terry on 6/7/2017.
 */


public class GlobalVariable extends Application {

    private String tpi_version = "SoundPayment";
    //private String tpi_version = "tpi";
    private String SSL_valid = "valid";
    private String tpi_filename = "tpi_data.json";


    private BleDevice bleDevice;
    private String EDCTpye = APPConstants.CREDIT;// cardType is Visa, Master, not EDCType

    public static final Set<String> EDC_set = new HashSet<>(Arrays.asList(APPConstants.CREDIT, APPConstants.DEBIT,
            APPConstants.EBT, APPConstants.CASH ));

    public static final Set<String> Global_AID = new HashSet<>(Arrays.asList("A0000000043060",
            "A0000000033010", "A0000000980840", "A0000000042203", "A0000006200620", "A0000001524010"));

    private String card_type = null;
    private String base_amount = null;
    private String total_amount = base_amount;

    private String sum_amount = null;
    private String tip_amount = null;
    private String cashback_amount = "null";
    private String card_number = null;
    private String card_choose_option = null;
    private String expired_date = null;
    private String ext_data = null;
    private String name_on_card = null;
    private String zip = null;

    private String address = null;
    private String cvv = null;
    private String street = null;
    private String transaction_type = null;
    private String domain = "https://gatewaystage.itstgate.com/smartpayments/transact.asmx/";
    private String method = APPConstants.POST;
    private String mag_data = null;
    private String inv_num = null;
    private String pn_ref = null;
    private String tip_rate_option1 = "10%";
    private String tip_rate_option2 = "15%";
    private String tip_rate_option3 = "20%";
    private String transaction_time = null;
    private Boolean swip_card_status = true;

    private Boolean EMV_Status = false;

    private String ebt_full_type = null;
    public static int stan=1;

    private String input_sys_password = null;
    private boolean card_present_status = false;
    private String time_zone = null;


    private String gmt_time = null;
    private String pin_block = null;
    private String ksn = null;
    private String track2data = null;
    private String ebt_type = null;
    private Boolean demo_version = FALSE;
    private String cvv_reason = null;

    private String voucher_number = null;


    private boolean get_data_host = false;

    public String getReadCardType() {
        return readCardType;
    }

    public void setReadCardType(String readCardType) {
        this.readCardType = readCardType;
    }

    public String readCardType = null;



    private String voucher_auth_num = null;


    // signature define
    private String signature_path = null;

    // todo change back to null after testing
    private String user_email="liujianpingsysu@gmail.com";

    public static int STAN = 0;
    public static int clientRef = 0;
    public static int Https_count = 0;
    public int orignal_reversal_stan = 0;
    public int server_stan = 0;

    // emv transdata
    private HashMap<String, String> transData = new HashMap<String, String>();


    // emv insert card action tag
    private String emv_insert_tag = null;
    private EasyLinkSdkManager easylink;

    // Temporary fake pay to clear the cart after paying
    private ArrayList<String> paidTables = new ArrayList<String>();

    public double getGasCost() {
        return gas_cost;
    }

    public void setGasCost(double gas_cost) {
        this.gas_cost = gas_cost;
    }

    // gas total
    public double gas_cost = 0;

    public String getGasDetail() {
        return gasDetail;
    }

    public void setGasDetail(String gasDetail) {
        this.gasDetail = gasDetail;
    }

    // gas desc
    public String gasDetail ="";

    public boolean isFuelUp() {
        return isFuelUp;
    }

    public void setFuelUp(boolean fuelUp) {
        isFuelUp = fuelUp;
    }

    // fuel up or not
    public boolean isFuelUp = false;





    public boolean getPaid(String tableId){return paidTables.contains(tableId);}
    public void setPaid(String tableId){ paidTables.add(tableId);}

    public EasyLinkSdkManager getEasylink() {
        return easylink;
    }

    public void setEasylink(EasyLinkSdkManager easylink) {
        this.easylink = easylink;
    }


    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }


    // Soundpayment hashmap
    public  HashMap<String, String> SPMap = new HashMap<String, String>(){
        {
            put(APPConstants.DOMAIN, "https://192.168.21.46/Service/MPosService.asmx/Transact?");
            // put(APPConstants.DOMAIN, "https://50.79.90.188:9011/Service/MPosService.asmx/Transact?");
//            put(APPConstants.USERNAME, "5tnRwZBt");
//            put(APPConstants.PASSWORD, "0pNRzSL7O");
//            put(APPConstants.PosID, "TestPosID1");

            put(APPConstants.USERNAME, "TestPosID1");
            put(APPConstants.PASSWORD, "TestPosID1");
            put(APPConstants.PosID, "TestPosID1");

            put(APPConstants.WorkstationID, null);
            put(APPConstants.Token, null);
            put(APPConstants.TerminalID, null);
            put(APPConstants.SerialNum, null);
            put(APPConstants.TraceNum, null);
            put(APPConstants.TimeOut, null);
            put(APPConstants.TenderType, null);

            put(APPConstants.TransType, null);
            put(APPConstants.Amount, null);
            put(APPConstants.CashBackAmt, null);
            put(APPConstants.FuelAmt, null);
            put(APPConstants.ClerkID, null);
            put(APPConstants.Zip, null);
            put(APPConstants.TipAmt, null);
            put(APPConstants.TaxAmt, null);

            put(APPConstants.Street, null);
            put(APPConstants.Street2, null);
            put(APPConstants.SurchargeAmt, null);
            put(APPConstants.PONum, null);
            put(APPConstants.OrigRefNum, null);
            put(APPConstants.InvNum, null);
            put(APPConstants.ECRRefNum, null);
            put(APPConstants.ECRTransID, null);

            put(APPConstants.AuthCode, null);
            put(APPConstants.SigSavePath, null);
            put(APPConstants.Account, null);
            put(APPConstants.ExpDate, null);
            put(APPConstants.CVV, null);
            put(APPConstants.EBTFoodStampVoucher, null);
            put(APPConstants.VoucherNum, null);
            put(APPConstants.Force, null);

            put(APPConstants.FirstName, null);
            put(APPConstants.LastName, null);
            put(APPConstants.CountryCode, null);
            put(APPConstants.StateCode, null);
            put(APPConstants.CityName, null);
            put(APPConstants.EmailAddress, null);
            put(APPConstants.CheckSaleType, null);
            put(APPConstants.CheckRoutingNum, null);

            put(APPConstants.CheckNum, null);
            put(APPConstants.CheckType, null);
            put(APPConstants.CheckIDType, null);
            put(APPConstants.CheckIDValue, null);
            put(APPConstants.Birth, null);
            put(APPConstants.PhoneNum, null);
            put(APPConstants.TimeStamp, null);
            put(APPConstants.ShiftID, null);

            put(APPConstants.CustomerCode, null);
            put(APPConstants.TaxExempt, null);
            put(APPConstants.TaxExemptID, null);
            put(APPConstants.MerchantTaxID, null);
            put(APPConstants.DestinationZipCode, null);
            put(APPConstants.ProductDescription, null);
            put(APPConstants.MOTOECommerceMode, null);
            put(APPConstants.MOTOECommerceTransType, null);

            put(APPConstants.ECommerceSecureType, null);
            put(APPConstants.MOTOECommerceOrderNum, null);
            put(APPConstants.Installments, null);
            put(APPConstants.CurrentInstallment, null);
            put(APPConstants.TableNum, null);
            put(APPConstants.GuestNum, null);
            put(APPConstants.SignatureCapture, null);
            put(APPConstants.TicketNum, null);

            put(APPConstants.HRefNum, null);
            put(APPConstants.TipRequest, null);
            put(APPConstants.SignUploadFlag, null);
            put(APPConstants.ReportStatus, null);
            put(APPConstants.TokenRequest, null);
            put(APPConstants.CardType, null);
            put(APPConstants.CardTypeBitmap, null);
            put(APPConstants.PassthruData, null);

            put(APPConstants.ReturnReason, null);
            put(APPConstants.OrigTransDate, null);
            put(APPConstants.OrigPAN, null);
            put(APPConstants.OrigExpiryDate, null);
            put(APPConstants.OrigTransTime, null);
            put(APPConstants.DisProgPrompt, null);
            put(APPConstants.GatewayID, null);
            put(APPConstants.POSEchoData, null);

            put(APPConstants.GetSign, null);
            put(APPConstants.EntryModeBitmap, null);
            put(APPConstants.ReceiptPrint, null);
            put(APPConstants.CPMode, null);
            put(APPConstants.Odometer, null);
            put(APPConstants.VehicleNo, null);
            put(APPConstants.JobNo, null);
            put(APPConstants.DriverID, null);

            put(APPConstants.EmployeeNo, null);
            put(APPConstants.LicenseNo, null);
            put(APPConstants.JobID, null);
            put(APPConstants.DepartmentNo, null);
            put(APPConstants.CustomerData, null);
            put(APPConstants.UserID, null);
            put(APPConstants.VehicleID, null);
            put(APPConstants.ExtData, null);


        }
    };

    public HashMap<String, String> TPIMap = new HashMap<String, String>(){
        {
            put(APPConstants.DOMAIN, "https://gatewaystage.itstgate.com/smartpayments/transact.asmx/");
            put(APPConstants.USERNAME, "PAXUSTEST");
            put(APPConstants.PASSWORD, "paxdata");

            put(APPConstants.TRANSTYPE, "NULL");
            put(APPConstants.CARDNUM, "NULL");
            put(APPConstants.CARDTYPE, "NULL");
            put(APPConstants.EXPDATE, "NULL");
            put(APPConstants.MAGDATA, "NULL");
            put(APPConstants.NAMEONCARD, "NULL");

            put(APPConstants.AMOUNT, "NULL");
            put(APPConstants.INVNUM, "NULL");
            put(APPConstants.PNREF, "NULL");
            put(APPConstants.ZIP, "NULL");
            put(APPConstants.STREET, "NULL");

            put(APPConstants.CVNUM, "NULL");
            put(APPConstants.EXTDATA, "NULL");
        }
    };

    // Get data from host
    public boolean isGetDataHost() {
        return get_data_host;
    }

    public void setGetDataHost(boolean get_data_host) {
        this.get_data_host = get_data_host;
    }


    // input system setting password
    public String getInputSysPassword() {
        return input_sys_password;
    }

    public void setInputSysPassword(String input_sys_password) {
        this.input_sys_password = input_sys_password;
    }

    //gmt time
    public String getGmtTime() {
        return gmt_time;
    }

    public void setGmtTime(String gmt_time) {
        this.gmt_time = gmt_time;
    }

    // edc list
    public Set<String> getEDCSet() {
        return EDC_set;
    }

    // AUTH CODE IN EBT
    public String getVoucherAuthNum() {
        return voucher_auth_num;
    }

    public void setVoucherAuthNum(String voucher_auth_num) {
        this.voucher_auth_num = voucher_auth_num;
    }

    // init AID
    public static Set<String> getGlobalAID() {
        return Global_AID;
    }

    // server stan
    public int getServerStan() {
        return server_stan;
    }

    public void setServerStan(int server_stan) {
        this.server_stan = server_stan;
    }

    // original reversal stan
    public  int getOrignalReversalStan() {
        return orignal_reversal_stan;
    }

    public  void setOrignalReversalStan(int orignal_reversal_stan) {
        this.orignal_reversal_stan = orignal_reversal_stan;
    }

    // SIGNATURE
    public String getSignaturePath() {
        return signature_path;
    }

    public void setSignaturePath(String signature_path) {
        this.signature_path = signature_path;
    }

    // EMV transdata
    public HashMap<String, String> getTransData() {
        return transData;
    }

    public void setTransData(HashMap<String, String> transData) {
        this.transData = transData;
    }

    // address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // demo version or not
    public boolean getDemoVersion(){
        return demo_version; 
    }
    public void setDemoVersion(Boolean status) {
        this.demo_version = status; 
    }

    // voucher number
    public String getVoucherNumber() {
        return voucher_number;
    }

    public void setVoucherNumber(String voucher_number) {
        this.voucher_number = voucher_number;
    }

    public int getStan() {
        return this.stan;
    }
    public void setStan(int data) {
        this.stan = data;
    }

    // pin block
    public String getPinBlock() {
        return pin_block;
    }

    public void setPinBlock(String pin_block) {
        this.pin_block = pin_block;
    }
    // EMV status
    public Boolean getEMVStatus() {
        return EMV_Status;
    }

    public void setEMVStatus(Boolean EMV_Status) {
        this.EMV_Status = EMV_Status;
    }

    // emv insert card action tag
    public String getEmvInsertTag() {
        return emv_insert_tag;
    }

    public void setEmvInsertTag(String emv_insert_tag) {
        this.emv_insert_tag = emv_insert_tag;
    }

    // ksn
    public String getKSN() {
        return ksn;
    }

    public void setKSN(String ksn) {
        this.ksn = ksn;
    }

    // getCVVReason
    public String getCVVReason(){
        return cvv_reason;
    }

    public void setCVVReason(String str){
        cvv_reason = str;
    }

    // sum amount
    public String getSumAmount() {
        return sum_amount;
    }

    public void setSumAmount(String sum_amount) {
        this.sum_amount = sum_amount;
    }

    // transaction time
    public String getTransactionTime() {
        return transaction_time;
    }

    public void setTransactionTime(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    // card present or not
    public boolean isCardPresentStatus() {
        return card_present_status;
    }

    public void setCardPresentStatus(boolean card_present_status) {
        this.card_present_status = card_present_status;
    }

    // clientRef
    public static int getClientRef() {
        return clientRef;
    }

    public static void setClientRef(int clientRef) {
        GlobalVariable.clientRef = clientRef;
    }

    public String getEbtFullType() {
        return ebt_full_type;
    }

    public void setEbtFullType(String ebt_full_type) {
        this.ebt_full_type = ebt_full_type;
    }

    // ebt_type
    public String getEBTType() {
        return ebt_type;
    }

    public void setEBTType(String ebt_type) {
        String ebt_str = "";
        if(ebt_type.equals("Food Stamp")){
            ebt_str = "F";
        }else if(ebt_type.equals("Cash Benefit")){
            ebt_str = "C";
        }else{
            ebt_str = "V";
        }

        this.ebt_type = ebt_str;
    }

    // tpi version
    public String getTPIVersion() {
        return this.tpi_version;
    }
    public void setTPIVersion(String tpi_version){
        this.tpi_version = tpi_version;
    }

    // SSL valid
    public String getSSLValid(){
        return this.SSL_valid;
    }
    public void setSSLValid(String str){
        this.SSL_valid = str;
    }

    // track2
    public String getTrack2data() {
        return track2data;
    }

    public void setTrack2data(String track2data) {
        this.track2data = track2data;
    }

    // time zone
    public String getTimeZone() {
        return time_zone;
    }

    public void setTimeZone(String time_zone) {
        this.time_zone = time_zone;
    }

    // tip response
    private String tpi_response = null;
    public String getTPIResponse(){
        return this.tpi_response;
    }
    public void setTPIResponse(String str){
        this.tpi_response = str;
    }

    // swip card event ture or false, true means user cancel the swip card
    public Boolean getSwipCardStatus(){
        return this.swip_card_status;
    }
    public void setSwipCardStatus(Boolean value){
        this.swip_card_status = value;
    }

    // tip rate option1
    public String getTipRateOption1(){
        return this.tip_rate_option1;
    }
    public void setTipRateOption1(String str){
        this.tip_rate_option1 = str;
    }

    // tip rate option2
    public String getTipRateOption2(){
        return this.tip_rate_option2;
    }
    public void setTipRateOption2(String str){
        this.tip_rate_option2 = str;
    }

    // tip rate option3
    public String getTipRateOption3(){
        return this.tip_rate_option3;
    }
    public void setTipRateOption3(String str){
        this.tip_rate_option3 = str;
    }

    // global user email
    public String getUserEmail(){
        return this.user_email;
    }
    public void setUserEmail(String str){
        this.user_email = str;
    }

    // pn_ref
    public String getPNRef() {
        return this.pn_ref;
    }
    public void setPNRef(String str) {
        this.pn_ref = str;
    }

    // exp_date
    public String getExtData() {
        return this.ext_data;
    }
    public void setExtData(String str) {
        this.ext_data = str;
    }

    // file name
    public String getTPIFileName() {
        return this.tpi_filename;
    }
    public void setTPIFilename(String str) {
        this.tpi_filename = str;
    }

    // mag data
    public String getMagData() {
        return this.mag_data;
    }
    public void setMagData(String str) {
        this.mag_data = str;
    }

    // inv_num
    public String getInvNum(){
        return this.inv_num;
    }
    public void setInvNum(String str){
        this.inv_num = str;
    }

    // choose card option
    public String getCardChooseOption() {
        return this.card_choose_option;
    }

    public void setCardChooseOption(String card_choose_option) {
        this.card_choose_option = card_choose_option;
    }


    // base amount
    public String getBaseAmount() {
        return this.base_amount;
    }
    public void setBaseAmount(String str) {
        this.base_amount = str;
    }

    // tip amount
    public String getTipAmount() {
        return this.tip_amount;
    }
    public void setTipAmount(String str) {
        this.tip_amount = str;
    }

    // cashback amount
    public String getCashbackAmount() {
        return this.cashback_amount;
    }
    public void setCashbackAmount (String str) {
        this.cashback_amount = str;
    }

    // transfer type
    public String getTransactionType(){
        return this.transaction_type ;
    }
    public void setTransactionType(String str) {
        this.transaction_type = str;
    }

    // domain
    public String getDomain() {
        return this.domain;
    }
    public void setDomain(String str) {
        this.domain = str;
    }


    // EDC type
    public String getEDCType() {
        return this.EDCTpye;
    }
    public void setEDCType(String str) {
        this.EDCTpye = str;
    }

    // total_amount
    public String getTotalAmount() {
        return this.total_amount;
    }
    public void setTotalAmount(String str) {
        this.total_amount = str;
    }

    // card_number
    public String getCardNumber() {
        return this.card_number;
    }
    public void setCardNumber(String str) {
        this.card_number = str;
    }

    // expired_date
    public String getExpiredDate() {
        return this.expired_date;
    }
    public void setExpiredDate(String str) {
        this.expired_date = str;
    }

    // name_on_card
    public String getNameOnCard() {
        return this.name_on_card;
    }
    public void setNameOnCard(String str) {
        this.name_on_card = str;
    }

    // zip
    public String getZip() {
        return this.zip;
    }
    public void setZip(String str) {
        this.zip = str;
    }

    // cvv
    public String getCVV() {
        return this.cvv;
    }
    public void setCVV(String str) {
        this.cvv = str;
    }

    // cvv
    public String getStreet() {
        return this.street;
    }
    public void setStreet(String str) {
        this.street = str;
    }

    public String getCardType() {
        return card_type;
    }

    public void setCardType(String card_type) {
        this.card_type = card_type;
    }

    // clean necessary global variable data
    public void cleanGlobalValue(){

        // reversal original stan keep
        if(transaction_type != null){
            if(!transaction_type.equals(APPConstants.REVERSAL)){
                orignal_reversal_stan=0;
            }
        }

        card_type = null;
        base_amount = null;
        total_amount = base_amount;

        sum_amount = null;
        tip_amount = "0.00";
        cashback_amount = "null";
        card_number = null;
        card_choose_option = null;
        expired_date = null;
        ext_data = null;
        name_on_card = null;
        zip = null;
        EDCTpye = APPConstants.CREDIT;

        address = null;
        cvv = null;
        street = null;
        transaction_type = null;
        method = APPConstants.POST;
        mag_data = null;
        inv_num = null;
        pn_ref = null;
        transaction_time = null;
        swip_card_status = true;
        ebt_full_type = APPConstants.FOODSTAM;

        input_sys_password = null;
        card_present_status = false;
        time_zone = null;
        pin_block = null;
        ksn = null;
        track2data = null;
        ebt_type = null;
        demo_version = FALSE;
        cvv_reason = null;

        // data from host
        get_data_host = false;




        // signature define
        File fdelete;
        if(signature_path != null){
            fdelete = new File(signature_path);
        }else{
            fdelete = null;
        }
        if (fdelete != null){
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + signature_path);
                } else {
                    System.out.println("file not Deleted :" + signature_path);
                }
            }
        }

        signature_path = null;

        // todo change back to null after testing
         String user_email="liujianpingsysu@gmail.com";


        // clean SP hashmap
        for(String key:SPMap.keySet()){
            if(key== APPConstants.DOMAIN || key==APPConstants.USERNAME ||
                    key==APPConstants.PASSWORD || key==APPConstants.PosID){
                System.out.println("The log");
            }else{
                SPMap.put(key, null);
            }


        }

        // clean TPI hashmap
        for(String key:TPIMap.keySet()){
            TPIMap.put(key, null);
        }

        System.out.println("This is the cleaned SPmap");
    }

    public boolean checkCardType(String checkType){
        return getCardChooseOption().equals(checkType);
    }


    // Methods added for demo::
    private static OpenTicket openTicket;
    public void setOrderTicket(OpenTicket ticket) {
        openTicket = ticket;
    }

    public static OpenTicket getOrderTicket() {
        return openTicket;
    }
}
