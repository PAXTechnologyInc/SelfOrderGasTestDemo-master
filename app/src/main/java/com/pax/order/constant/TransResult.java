package com.paxus.common.type;

public class TransResult {
    /**
     * 交易成功
     */
    public static final int SUCC = 0;

    /**
     * 超时
     */
    public static final int ERR_TIMEOUT = -1;
    /**
     * 连接超时
     */
    public static final int ERR_CONNECT = -2;
    /**
     * 交易终止
     */
    public static final int ERR_USER_CANCEL = -3;
    /**
     * 发送失败
     */
    public static final int ERR_SEND = -4;
    /**
     * 接收失败
     */
    public static final int ERR_RECV = -5;

    /**
     * 无交易
     */
    public static final int ERR_NO_TRANS = -6;

    public static final int ERR_SUB_APP_DISCONNECTED = -7;

    /**
     * 此交易已撤销
     */
    public static final int ERR_HAS_VOID = -8;
    /**
     * 此交易不可撤销
     */
    public static final int ERR_VOID_UNSUPPORTED = -9;
    /**
     * 失败
     */
    public static final int ERR_HOST_REJECT = -10;

    public static final int ERR_TIP_NOT_ALLOWED = -11;

    /**
     * 预处理相关 交易笔数超限，立即结算
     */
    public static final int ERR_NEED_SETTLE_NOW = -12;
    /**
     * 预处理相关 交易笔数超限，稍后结算
     */
    public static final int ERR_NEED_SETTLE_LATER = -13;
    /**
     * 预处理相关 存储空间不足
     */
    public static final int ERR_NO_FREE_SPACE = -14;
    /**
     * 预处理相关 终端不支持该交易
     */
    public static final int ERR_NOT_SUPPORT_TRANS = -15;
    /**
     * 卡号不一致
     */
    public static final int ERR_CARD_NUM = -16;
    /**
     * 密码错误
     */
    public static final int ERR_PASSWORD = -17;
    /**
     * 参数错误
     */
    public static final int ERR_PARAM = -18;

    /**
     * 终端批上送未完成
     */
    public static final int ERR_BATCH_UP_NOT_COMPLETED = -19;

    /**
     * 金额超限
     */
    public static final int ERR_AMOUNT_EXCEED = -20;

    /**
     * 卡片拒绝
     */
    public static final int ERR_CARD_DENIED = -21;

    public static final int ERR_SUB_APP_UNINSTALLED = -22;

    /**
     * 无有效交易
     */
    public static final int ERR_NO_VALID_TRANS = -23;
    /**
     * 工作密钥长度错误
     */
    public static final int ERR_TWK_LENGTH = -24;

    /**
     * 卡片信息错误
     */
    public static final int ERR_CARD_INFO = -25;

    /**
     * 卡片过期
     */
    public static final int ERR_CARD_EXPIRED = -26;

    /**
     * no support step
     */
    public static final int NO_SUPPORT = -27;

    /**
     * get info failed form release.host
     */
    public static final int ERR_NO_DATA_HOST = -28;
    /**
     * 重复交易
     */
    public static final int ERR_DUP_TRANS = -29;
    // declined.
    public static final int ERR_DECLINED = -30;
    // no response from server.
    public static final int ERR_NO_RESPONSE = -31;
    // general error.
    public static final int ERR_GENERAL = -32;

    /**
     * 金额错误
     */
    public static final int ERR_AMOUNT_ERROR = -33;

    public static final int ERR_DUP_INVOICE = -34;

    public static final int ERR_INVALID_CARD_TYPE = -35;

    /**
     * 输入pin被取消
     */
    public static final int ERR_CANCEL_ENTER_BIN = -36;

    /**
     * 网络不可用
     */
    public static final int ERR_NETWORK_AVAILABLE = -37;

    /**
     * 密钥没有注入
     */
    public static final int ERR_KEY_NO_INJECTED = -38;

    /**
     * Secure Card错误
     */
    public static final int ERR_SECURE_CARD = -39;
    /**
     * Invoice 输入错误
     */
    public static final int ERR_INVOICE = -40;

    public static final int ERR_SF_RECORD_OVERCOUNT = -41;

    /**
     * 不支持SplitTender
     */
    public static final int ERR_NO_SUPPORT_SPLIT_TENDER = -42;

    /**
     * EDC TYPE MISMATCHING
     */
    public static final int ERR_EDC_TYPE_MISMATCHING = -43;

    /**
     * Upload失败
     */
    public static final int UPLOAD_FAILED = -44;

    public static final int ERR_NOT_ACCEPT_FSA_CARD = -45; //If ForceFSA but the host does not support FSA, return this error

    public static final int CLOSE_TRANS_NOT_PROMPT = -46;

    public static final int FALLBACK_SWIPE_NOT_ALLOWED = -47;

    public static final int ERR_EMV_CREDIT_ONLY = -48;

    public static final int ERR_EMV_DEBIT_ONLY = -49;


    public static final int ERR_ALREADY_COMPL = -50;

    /**
     *
     */
    public static final int ERR_CHIP_CARD = -51;

    public static final int UPLOAD_COMPLETE = -52;

    public static final int ERR_NO_ENTRY_ALLOWED = -53;


    public static final int BATCH_CLOSE_FAILED = -54;

    public static final int NO_EMVKEY_UPDATE = -55;

    public static final int ERR_FILE = -56;

    public static final int ERR_TOO_MANY_CARDS = -57;

    public static final int ERR_CASHBACK_NOT_ALLOWED = -58;

    public static final int ERR_OFFLINE_DECLINED = -59;

    public static final int ERR_INVALID_ONLINE_PIN = -60;

    public static final int NO_SUPPORT_IN_OFFLINE_MODE = -61;

    public static final int NO_SUPPORT_IN_NO_PAPER_MODE = -62;

    public static final int QUICK_CHIP_NOT_SUPPORTED = -63;

    public static final int ERR_CARD_TYPE_DISABLED = -64;

    public static final int ERR_MERCHANT_FEE_NOT_ALLOWED = -65;

    public static final int ERR_NOT_SUPPORTED_IN_DEMO_MODE = -66;

    public static final int ERR_PRINT_DATA_ERROR = -67;

    public static final int ERR_FSA_SUB_HEALTH_CARE_AMT_EXCEED = -68;

    public static final int ERR_FSA_AMT_EXCEED = -69;

    public static final int ERR_FSA_VISA_ONLY = -70;

    public static final int ERR_FSA_VISA_MASTERCARD_ONLY = -71;

    public static final int ERR_FSA_NOT_SUPPORT = -72;

    public static final int ERR_FSA_PASSTHRU_DATA_MISSING = -73;

    public static final int ERR_ME_MODE_NOT_SUPPORT = -74;

    public static final int ERR_POSLINK_EXCEPTION = -75;

    public static final int ERR_FLEET_AMOUNT_EXCEED = -76;

    public static final int ERR_FLEET_FUEL_ONLY = -77;

    public static final int ERR_EXTERNAL_PIN_PAD_COMM = -78;

    public static final int ERR_SWIPE_ONLY = -79;

    public static final int ERR_SF_OVER_TOTAL_AMOUNT = -80;

    public static final int ERR_SF_OVER_CARDTYPE_MAX_AMT = -81;

    public static final int ERR_SF_OVER_CARDTYPE_TOTAL_AMT = -82;

    public static final int ERR_AMT_EXCEED_HALO = -83;

    private TransResult() {

    }
}
