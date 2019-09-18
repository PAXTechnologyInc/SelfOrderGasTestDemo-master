package com.pax.order.constant;

import com.pax.order.util.Convert;

/**
 * Created by lixc on 2017/3/28.
 */

public class TransDataTag {


    //set transaction data before start transaction
    public byte[] getTransDataTagSet() {
        return new byte[]{(byte) 0x9F, 0x02, 0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01,
                (byte) 0x9C, 0x01, 0x01,
                0x5F, 0x2A, 0x02, 0x01, 0x01,
                0x5F, 0x36, 0x01, 0x01,
                (byte) 0x9A, 0x03, 0x16, 0x11, 0x15,
                (byte) 0x9F, 0x21, 0x03, 0x15, 0x26, 0x59};
    }

    //set parameter data before get pin block when MSR
    public byte[] PINEntryParams() {
        StringBuilder tlvString = new StringBuilder();
        // TODO: parameter needs to be set from configuration file
        tlvString.append("0202 01 02");    // Tag = 0202 (PINEncryption Type), Value = 02
        tlvString.append("0203 01 01");    // PIN Encryption Key Index
        tlvString.append("0204 01 00");    // PIN Block Mode
        return Convert.HexString2Bytes(tlvString.toString());
    }

    public byte[] trackDataTagList(){
        return new byte[]{0x03,0x04,0x03,0x05,0x03,0x06};
    }


    public byte[] getConfigureDataTagSet(String emv_tag){
        StringBuilder tlvString = new StringBuilder();
        if(emv_tag.equals(APPConstants.FULL))
        {
            tlvString.append("031C 01 00"); // EMV SUPPORTED
        }else if(emv_tag.equals(APPConstants.HALF)){
            tlvString.append("031C 01 01"); // HALF EMV
        }else{
            tlvString.append("031C 01 02"); // NON EMV
        }

        return Convert.HexString2Bytes(tlvString.toString());


    }

    public byte[] EMVTagList(){
        return new byte[]{
                0x4F,       // AID (ICC)
                0x57,       // Track 2 Equivalent Data
                // 0x5A,       // PAN
                0x5F,0x28,  // Issuer Country Code
                0x5F,0x2A,  // Transaction Currency Code
                // 0x5F,0x2D,  // Language Preference
                0x5F,0x34,  // Application PAN Sequence Number
                (byte)0x82,     // AIP
                (byte)0x84,     // DF Name
                (byte)0x95,     // TVR
                (byte)0x9A,     // Transaction Date
                (byte)0x9B,     // TSI
                (byte)0x9C,     // Transaction Type
                (byte)0x9F,0x02,    // Transaction Amount
                (byte)0x9F,0x03,    // Cashback Amount
                (byte)0x9F,0x06,    // AID (Terminal)
                (byte)0x9F,0x07,    // Application Usage Control
                (byte)0x9F,0x08,    // Application Version Number (ICC)
                (byte)0x9F,0x09,    // Application Version Number (TERMINAL)
                (byte)0x9F,0x0D,    // IAC - Default 
                (byte)0x9F,0x0E,    // IAC - Denial
                (byte)0x9F,0x0F,    // IAC - Online
                (byte)0x9F,0x10,    // Issuer Application Data
                (byte)0x9F,0x1A,    // Terminal Country Code
                (byte)0x9F,0x21,    // Transaction Time
                (byte)0x9F,0x26,    // Application Cryptogram
                (byte)0x9F,0x27,    // Cryptogram Information Data (CID)
                (byte)0x9F,0x33,    // Terminal Capabilities
                (byte)0x9F,0x34,    // CVM Result
                (byte)0x9F,0x35,    // Terminal Type
                (byte)0x9F,0x36,    // Application Transaction Counter
                (byte)0x9F,0x37,    // Unpredictable Number
                (byte)0x9F,0x39,    // POS Entry Mode
                (byte)0x9F,0x40,    // Additional Terminal Capabilities
                (byte)0x9F,0x41,    // Transaction Sequence Counter
        };
    }
}
