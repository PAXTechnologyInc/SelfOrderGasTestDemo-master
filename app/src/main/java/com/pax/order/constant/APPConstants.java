package com.pax.order.constant;

import java.util.HashMap;

/**
 * Created by Terry on 6/13/2017.
 */

public class APPConstants {

    // Demo
    public static final String DEMO="PAX Demo";
    public static final double gas_cost = 45.87;

    // API version
    public static final String SoundPayment = "SoundPayment";
    public static final String TPI = "tpi";

    // SSL status
    public static final String INVALID = "Invalid";
    public static final String VALID = "valid";

    // card type comment
    public static final String CREDIT = "credit";
    public static final String DEBIT = "debit";
    public static final String EBT = "ebt";
    public static final String CASH = "cash";

    public static final String FUNC = "func";
    public static final String BT = "bluetooth";
    public static final String DEBUG_TAG = "debug";

    public static final String SWIPE = "swipe";
    public static final String MANUAL = "manual";
    public static final String FALLBACK = "fallback";
    public static final String INSERT = "insert";
    public static final String TAP = "tap";

    // EMV INSERT/SWIPE ACTION
    public static final String FULL = "full";
    public static final String HALF = "half";
    public static final String NON = "non";

    // transaction type
    public static final String SALE = "Sale";
    public static final String PRE_AUTH = "Pre-Auth";
    public static final String AUTH = "Auth";
    public static final String POSAUTH = "Pos-Auth";
    public static final String VOID = "Void";
    public static final String REFUND = "Return";
    public static final String REVERSAL = "Reversal";
    public static final String ADJUST = "Adjust";
    public static final String TRANCTION_HISTORY="Trans-History";
    public static final String SETTLEMENT = "Settlement";
    public static final String OTHER = "Other";
    public static final String SETTING = "Settings";
    public static final String FORCED = "Force";
    public static final String REDEEM = "Redeem";
    public static final String RELOAD = "Reload";
    public static final String ACTIVATE = "Activate";
    public static final String DEACTIVATE = "Deactivate";
    public static final String INQUIRE = "Inquire";
    public static final String CASHBACK = "Cashback";

    // dialog prompt need
    public static final String WANTBYPASS = "Want to Bypass";
    public static final String CANNOTREAD = "Cannot Read CVV";
    public static final String NOTEXIST = "Not Exist";


    // card brand type
    public static final String VISA = "Visa";
    public static final String MASTERCARD = "MasterCard";
    public static final String AMEX = "AMEX";
    public static final String DINERS = "Diners";
    public static final String DISCOVER = "Discover";
    public static final String JCB = "JCB";
    public static final String ENROUTE = "enRoute";

    // ebt
    public static final String VOUCHER = "Voucher";
    public static final String FOODSTAM = "Food Stamp";
    public static final String CASHBENEFIT = "Cash Benefit";
    public static final String USERPASSWORD = "UserPassword";

    public static final HashMap<String, String> EMV_TXN_TYPE = new HashMap<String, String>(){
        {
            put(SALE, "00");
            put(PRE_AUTH, "30");
            put(AUTH, "00");
            put(POSAUTH, "00");
            put(FORCED, "00");
            put(VOID, "10");
            put(REFUND, "20");
            put(CASHBACK, "09");
        }
    };

    // TPI vars pack
    public static final String USERNAME = "UserName";
    public static final String PASSWORD = "Password";
    public static final String TRANSTYPE = "TransType";
    public static final String CARDNUM = "CardNum";
    public static final String EXPDATE = "ExpDate";
    public static final String MAGDATA = "MagData";
    public static final String NAMEONCARD = "NameOnCard";
    public static final String CARDTYPE = "CardType";
    public static final String AMOUNT = "Amount";
    public static final String INVNUM = "InvNum";
    public static final String PNREF = "PNRef";
    public static final String ZIP = "Zip";
    public static final String STREET = "Street";
    public static final String CVNUM = "CVNum";
    public static final String EXTDATA = "ExtData";
    public static final String DOMAIN = "domain";
    public static final String POST = "POST";
    public static final String EMVDATA = "EMVData";

    // TPI response variables
    public static final String RESPONSE = "Response";
    public static final String RESULT = "Result";
    public static final String RESPMSG = "RespMSG";
    public static final String MESSAGE = "Message";
    public static final String MESSAGE1 = "Message1";
    public static final String MESSAGE2 = "Message2";
    public static final String AUTHCODE = "AuthCode";
    public static final String HOSTCODE = "HostCode";
    public static final String HOSTURL1 = "HostURL1";
    public static final String HOSTPORT1 = "HostPort1";
    public static final String HOSTURL2 = "HostURL2";
    public static final String HOSTPORT2 = "HostPort2";
    public static final String GETAVSRESULT = "GetAVSResult";
    public static final String GETAVSRESULTEXT = "GetAVSResultTXT";
    public static final String GETSTREETMATCHTXT = "GetStreetMatchTXT";
    public static final String GETZIPMATCHTXT = "GetZipMatchTXT";
    public static final String GETGETORIGRESULT = "GetGetOrigResult";
    public static final String GETCOMMERCIALCARD = "GetCommercialCard";

    // response
    public static final String DECLINE = "DECLINE";
    public static final String FAIL = "FAIL";
    public static final String APPROVAL = "APPROVAL";
    public static final String NETWORK_ERROR = " Network Error";

    // host setting
    public static final String PTPE = "P2PE Model";
    public static final String PARTIALAPPROVAL = "Partial Approval Model";

    // fix timer
    public static final int  MILLISINFUTURE = 50000;

    // options
    public static final String CC="CC";
    public static final String STAR="STAR";
    public static final String FSA="FSA";
    public static final String BLANK = "";

    // operation
    public static final String DEMON = "Demon Activation";
    public static final String TIPRATEOPTION1 =  "tip_rate_option1";
    public static final String TIPRATEOPTION2 =  "tip_rate_option2";
    public static final String TIPRATEOPTION3 =  "tip_rate_option3";

    //EMV tage
    public static final String EMVAIDTAG = "4F";

    // Soundpayment POST Request  API fields
    public static final String WorkstationID = "WorkstationID";
    public static final String PosID = "PosID";
    public static final String Token = "Token";
    public static final String TerminalID = "TerminalID";
    public static final String SerialNum = "SerialNum";
    public static final String TraceNum = "TraceNum";
    public static final String TimeOut = "TimeOut";
    public static final String TenderType = "TenderType";
    public static final String TransType = "TransType";
    public static final String Amount = "Amount";

    public static final String CashBackAmt = "CashBackAmt";
    public static final String FuelAmt = "FuelAmt";
    public static final String ClerkID = "ClerkID";
    public static final String Zip = "Zip";
    public static final String TipAmt = "TipAmt";
    public static final String TaxAmt = "TaxAmt";
    public static final String Street = "Street";
    public static final String Street2 = "Street2";
    public static final String SurchargeAmt = "SurchargeAmt";
    public static final String PONum = "PONum";

    public static final String InvNum = "InvNum";
    public static final String ECRRefNum = "ECRRefNum";
    public static final String ECRTransID = "ECRTransID";

    public static final String SigSavePath = "SigSavePath";
    public static final String Account = "Account";
    public static final String ExpDate = "ExpDate";
    public static final String CVV = "CVV";
    public static final String EBTFoodStampVoucher = "EBTFoodStampVoucher";

    public static final String VoucherNum = "VoucherNum";
    public static final String Force = "Force";
    public static final String FirstName = "FirstName";
    public static final String LastName = "LastName";
    public static final String CountryCode = "CountryCode";
    public static final String StateCode = "StateCode";
    public static final String CityName = "CityName";
    public static final String EmailAddress = "EmailAddress";
    public static final String CheckSaleType = "CheckSaleType";
    public static final String CheckRoutingNum = "CheckRoutingNum";

    public static final String CheckNum = "CheckNum";
    public static final String CheckType = "CheckType";
    public static final String CheckIDType = "CheckIDType";
    public static final String CheckIDValue = "CheckIDValue";
    public static final String Birth = "Birth";
    public static final String PhoneNum = "PhoneNum";
    public static final String TimeStamp = "TimeStamp";
    public static final String ShiftID = "ShiftID";
    public static final String CustomerCode = "CustomerCode";
    public static final String TaxExempt = "TaxExempt";

    public static final String TaxExemptID = "TaxExemptID";
    public static final String MerchantTaxID = "MerchantTaxID";
    public static final String DestinationZipCode = "DestinationZipCode";
    public static final String ProductDescription = "ProductDescription";
    public static final String MOTOECommerceMode = "MOTOECommerceMode";
    public static final String MOTOECommerceTransType = "MOTOECommerceTransType";
    public static final String ECommerceSecureType = "ECommerceSecureType";
    public static final String MOTOECommerceOrderNum = "MOTOECommerceOrderNum";
    public static final String Installments = "Installments";
    public static final String CurrentInstallment = "CurrentInstallment";

    public static final String TableNum = "TableNum";
    public static final String GuestNum = "GuestNum";
    public static final String SignatureCapture = "SignatureCapture";
    public static final String TicketNum = "TicketNum";
    public static final String HRefNum = "HRefNum";
    public static final String TipRequest = "TipRequest";
    public static final String SignUploadFlag = "SignUploadFlag";
    public static final String ReportStatus = "ReportStatus";
    public static final String TokenRequest = "TokenRequest";
    public static final String CardType = "CardType";

    public static final String CardTypeBitmap = "CardTypeBitmap";
    public static final String PassthruData = "PassthruData";
    public static final String ReturnReason = "ReturnReason";
    public static final String OrigTransDate = "OrigTransDate";
    public static final String OrigPAN = "OrigPAN";
    public static final String OrigExpiryDate = "OrigExpiryDate";
    public static final String OrigTransTime = "OrigTransTime";
    public static final String DisProgPrompt = "DisProgPrompt";
    public static final String GatewayID = "GatewayID";
    public static final String POSEchoData = "POSEchoData";


    public static final String GetSign = "GetSign";
    public static final String EntryModeBitmap = "EntryModeBitmap";
    public static final String ReceiptPrint = "ReceiptPrint";
    public static final String CPMode = "CPMode";
    public static final String Odometer = "Odometer";
    public static final String VehicleNo = "VehicleNo";
    public static final String JobNo = "JobNo";
    public static final String DriverID = "DriverID";
    public static final String EmployeeNo = "EmployeeNo";
    public static final String LicenseNo = "LicenseNo";

    public static final String JobID = "JobID";
    public static final String DepartmentNo = "DepartmentNo";
    public static final String CustomerData = "CustomerData";
    public static final String UserID = "UserID";
    public static final String VehicleID = "VehicleID";
    public static final String ExtData = "ExtData";

    //setting
    public static final String header1 = "Header Text1";
    public static final String header2 = "Header Text2";
    public static final String header3 = "Header Text3";
    public static final String header4 = "Header Text4";
    public static final String header5 = "Header Text5";
    public static final String trailer1 = "Trailer Text1";
    public static final String trailer2 = "Trailer Text2";
    public static final String trailer3 = "Trailer Text3";
    public static final String trailer4 = "Trailer Text4";
    public static final String trailer5 = "Trailer Text5";
    public static final String avsOnSwipe = "AVS On Swipe";
    public static final String avsOnCardPrsnt = "AVS On CardPrsnt";
    public static final String avsOnCardNotPrsnt = "AVS On CardNotPrsnt";
    public static final String cardPresnet = "Card Present";
    public static final String needCvv = "CVV";
    public static final String localDupCheck = "Local Dup Check";
    public static final String enable = "Yes";
    public static final String disable = "No";





    // SoundPayment API Response fields
    public static final String PosPaymentResult = "PosPaymentResult";
    public static final String ResultCode = "ResultCode";
    public static final String ResultMsg = "ResultMsg";
    public static final String IPaddress = "IPaddress";
    public static final String Port = "Port";
    public static final String MacAddress = "MacAddress";
    public static final String AuthCode = "AuthCode";
    public static final String ApprovedAmount = "ApprovedAmount";
    public static final String MaskedPAN = "MaskedPAN";
    public static final String RemainingBalance = "RemainingBalance";
    public static final String ExtraBalance = "ExtraBalance";
    public static final String AvsResponse = "AvsResponse";
    public static final String CvResponse = "CvResponse";

    public static final String HostCode = "HostCode";
    public static final String HostResponse = "HostResponse";
    public static final String HostMessage = "HostMessage";
    public static final String HostTraceNum = "HostTraceNum";
    public static final String RefNum = "RefNum";
    public static final String TransDate = "TransDate";
    public static final String TransTime = "TransTime";

    public static final String OrigRefNum = "OrigRefNum";
    public static final String BatchNum = "BatchNum";
    public static final String SigFileName = "SigFileName";
    public static final String SignData = "SignData";
    public static final String RawResponse = "RawResponse";



}
