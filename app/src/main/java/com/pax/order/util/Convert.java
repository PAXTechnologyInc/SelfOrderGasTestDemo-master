package com.pax.order.util;

import android.util.Log;

import com.paxsz.easylink.model.DataModel;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by lixc on 2017/4/27.
 */

public class Convert {

    private final static char[] mHexChars = "0123456789ABCDEF".toCharArray();
    private final static String mHexString = "0123456789ABCDEF";

    private Convert() {

    }

    //bytes to string
    public static String byte2Str(byte[] bytes, int startIndex) {
        StringBuilder temp = new StringBuilder(bytes.length);
        int length = bytes.length - startIndex;
        char[] tChars = new char[length];
        for (int i = startIndex; i < bytes.length; i++) {
            tChars[i - startIndex] = (char) bytes[i];
        }
        temp.append(tChars);
        return temp.toString();
    }

    public static boolean IsValidHexString(String hexString){
        String temp = hexString.trim().replace(" ", "").toUpperCase(Locale.US);
        int strLen = temp.length();

        if ((strLen <= 1) && (strLen%2 != 0)){
            return false;
        }

        for (int loop=0; loop<strLen; loop++){
            if (!mHexString.contains(temp.substring(loop, loop+1))){
                return false;
            }
        }

        return true;
    }

    public static byte[] HexString2Bytes(String hexString){
        if (!IsValidHexString(hexString))
            return null;

        char[] charArray = hexString.trim().replace(" ", "").toUpperCase().toCharArray();
        int bytesLen = charArray.length / 2;
        byte[] bytes = new byte[bytesLen];
        byte temp;

        for (int loop=0; loop<charArray.length; loop=loop+2){
            if (charArray[loop] > '9'){
                temp = (byte)(charArray[loop] - 'A' + 0x0A);
            } else{
                temp = (byte)(charArray[loop] & 0x0F);
            }
            bytes[loop/2] = (byte)(temp << 4);

            if (charArray[loop+1] > '9'){
                temp = (byte)(charArray[loop+1] - 'A' + 0x0A);
            } else{
                temp = (byte)(charArray[loop+1] & 0x0F);
            }
            bytes[loop/2] |= temp;
        }

        return bytes;
    }

    public static String Bytes2HexString(byte[] bytes){
        StringBuilder sb = new StringBuilder("");
        int tmp;

        for (int loop=0; loop<bytes.length; loop++){

            tmp = (bytes[loop] & 0xF0) >>  4;
            sb.append(mHexChars[tmp]);
            //Log.i("Convert.Java", "appended char = " + mHexChars[tmp]);

            tmp = bytes[loop] & 0x0F;
            sb.append(mHexChars[tmp]);
            //Log.i("Convert.Java", "appended char = " + mHexChars[tmp]);


        }

        return sb.toString();
    }

    public static boolean IsNullString(String str){
        if ((str.toUpperCase().equals("NULL")) || (str.length() == 0) || (str == null)){
            return true;
        }

        return false;
    }

    public static int GetUnsignedInt(byte b) {
        return (0xFF & b);
    }

    public static HashMap<String, String> ParseTLVData(DataModel.DataType dataType, byte[] tlvData)
    {
        HashMap<String, String> map = new HashMap<>();
        int dataSize;

        dataSize = 256*GetUnsignedInt(tlvData[0]) + GetUnsignedInt(tlvData[1]);
        if (dataSize < 4 || tlvData.length != (dataSize+2)) {
            Log.i("TEST INFO", "Invalid TLV Data. dataSize = " + dataSize + " ### " + "length = " + tlvData.length);
            return map;
        }

        String tlvString = Bytes2HexString(tlvData).substring(4);
        tlvString = tlvString + " ";    // add 1 more extra byte so that bound overflow won't happen for String.subString
        Log.i("TEST INFO", "parseTLVData: tlvString = " + tlvString);
        int length, tagLen;

        if (dataType == DataModel.DataType.CONFIGURATION_DATA) {
            // customized tag. Length of TAG is always 2 bytes
            tagLen = 2;
            for (int loop = 0; loop < dataSize; ) {
                length = GetUnsignedInt(tlvData[loop + tagLen + 2]);    // offset = 2: the 1st 2 bytes in tlvData is the length of the whole tlv data

                map.put(tlvString.substring(loop*2, (loop + tagLen)*2), tlvString.substring((loop + tagLen + 1)*2, (loop + tagLen + 1 + length)*2));
                loop += tagLen + 1 + length;
            }
        }else if (dataType == DataModel.DataType.TRANSACTION_DATA) {
            // emv tag
            for (int loop = 0; loop < dataSize; ) {
                // the length of emv TAG depends on the following rule
                tagLen = 1;
                if ((tlvData[loop + 2] & 0x1F) == 0x1F)     // if the lowest 5 bits of the 1st byte are all '1', then there's a following byte as part of the TAG
                    tagLen++;
                int tmp = loop + 1;
                while ((tlvData[tmp + 2] & 0x80) == 0x80){  // if the highest bit (bit 8) of the following bytes is '1', then there's another following byte as part of the TAG
                    tagLen++;
                    tmp++;
                }

                length = GetUnsignedInt(tlvData[loop + tagLen + 2]);    // offset = 2: the 1st 2 bytes in tlvData is the length of the whole tlv data
                map.put(tlvString.substring(loop*2, (loop + tagLen)*2), tlvString.substring((loop + tagLen + 1)*2, (loop + tagLen + 1 + length)*2));
                loop += tagLen + 1 + length;
            }
        }

        return map;
    }


    public static<T extends Object> void checkType(T object) {
        if (object instanceof Integer)
            System.out.println("Integer ");
        else if(object instanceof Double)
            System.out.println("Double ");
        else if(object instanceof Float)
            System.out.println("Float : ");
        else if(object instanceof String)
            System.out.println("List! ");
        else if(object instanceof HashMap)
            System.out.println("HashMap! ");
        else if(object instanceof JSONObject)
            System.out.println("JSONObject! ");
    }

}
