package com.pax.order.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Tools {
    private static final String TAG = Tools.class.getSimpleName();
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
            'E', 'F'};

    public static byte[] int2Bytes(long num) {
        byte[] tmp = new byte[4];
        for (int i = 0; i < 4; i++) {
            tmp[3 - i] = (byte) (num >>> (24 - i * 8));
        }
        return tmp;
    }

    /**
     * int到byte[]
     *
     * @param i
     * @return
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        // 由高位到低位
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    /**
     * byte[]转int
     *
     * @param bytes
     * @return
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        // 由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    public static byte[] string2Bytes(String source) {
        byte[] result = new byte[0];

        try {
            if (source != null)
                result = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] string2Bytes(String source, int checkLen) {
        byte[] result = new byte[0];
        if ((null == source) || (source.length() != checkLen))
            return result;
        try {
            if (source != null)
                result = source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bcd2Str(byte[] b, int length) {
        if (b == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(length * 2);
        for (int i = 0; i < length; ++i) {
            sb.append(HEX_DIGITS[((b[i] & 0xF0) >>> 4)]);
            sb.append(HEX_DIGITS[(b[i] & 0xF)]);
        }

        return sb.toString();
    }

    private static int strByte2Int(byte b) {
        int j;
        if ((b >= 'a') && (b <= 'z')) {
            j = b - 'a' + 0x0A;
        } else {
            if ((b >= 'A') && (b <= 'Z'))
                j = b - 'A' + 0x0A;
            else
                j = b - '0';
        }
        return j;
    }

    public static byte[] str2Bcd(String asc) {
        String str = asc;
        if (str.length() % 2 != 0) {
            str = "0" + str;
        }
        int len = str.length();
        if (len >= 2) {
            len /= 2;
        }
        byte[] bbt = new byte[len];
        byte[] abt = str.getBytes();

        for (int p = 0; p < str.length() / 2; p++) {
            bbt[p] = (byte) ((strByte2Int(abt[(2 * p)]) << 4) + strByte2Int(abt[(2 * p + 1)]));
        }
        return bbt;
    }

    public static byte[] fillData(int dataLength, byte[] source, int offset) {
        byte[] result = new byte[dataLength];
        if (offset >= 0)
            System.arraycopy(source, 0, result, offset, source.length);
        return result;
    }

    public static byte[] fillData(int dataLength, byte[] source, int offset, byte fillByte) {
        byte[] result = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) {
            result[i] = fillByte;
        }
        if (offset >= 0)
            System.arraycopy(source, 0, result, offset, source.length);
        return result;
    }

    public static String bytes2String(byte[] source) {
        String result = "";
        try {
            if (source.length > 0)
                result = new String(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String bytes2String(byte[] source, int sourceLen) {
        String result = "";
        if (null == source || 0 == sourceLen)
            return result;

        int iLen = Math.min(sourceLen, source.length);
        try {
            if (iLen > 0)
                result = new String(source, 0, iLen, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] hexStr2HexByte(String hexStr, boolean PADDING_LEFT) {
        if (hexStr == null || hexStr.isEmpty())
            return null;

        String srchexStr = hexStr;
        if (srchexStr.length() % 2 != 0) {
            if (PADDING_LEFT)
                srchexStr = "0" + srchexStr;
            else
                srchexStr = srchexStr + "0";
        }

        String str = "0123456789ABCDEF";
        char[] hexs = srchexStr.toUpperCase().toCharArray();
        byte[] bytes = new byte[srchexStr.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            int n = str.indexOf(hexs[(2 * i)]) * 16;
            n += str.indexOf(hexs[(2 * i + 1)]);
            bytes[i] = ((byte) (n & 0xFF));
        }
        return bytes;
    }

    public static byte[] hexStr2HexByte(String hexStr, boolean PADDING_LEFT, char fillChar) {
        if (hexStr == null || hexStr.isEmpty())
            return null;

        String srchexStr = hexStr;
        if (srchexStr.length() % 2 != 0) {
            if (PADDING_LEFT)
                srchexStr = fillChar + srchexStr;
            else
                srchexStr = srchexStr + fillChar;
        }

        String str = "0123456789ABCDEF";
        char[] hexs = srchexStr.toUpperCase().toCharArray();
        byte[] bytes = new byte[srchexStr.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            int n = str.indexOf(hexs[(2 * i)]) * 16;
            n += str.indexOf(hexs[(2 * i + 1)]);
            bytes[i] = ((byte) (n & 0xFF));
        }
        return bytes;
    }

    public static String hexByte2HexStr(byte[] bs) {
        if (bs == null)
            return null;
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");

        for (int i = 0; i < bs.length; i++) {
            int bit = (bs[i] & 0xF0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0xF;
            sb.append(chars[bit]);
        }

        return sb.toString().trim();
    }

    public static String hexByte2HexStr(byte[] bs, int offst, int len) {
        if (bs == null)
            return null;
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");

        for (int i = offst; i < offst + len; i++) {
            int bit = (bs[i] & 0xF0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0xF;
            sb.append(chars[bit]);
        }

        return sb.toString().trim();
    }

    /**
     * 返回错误码给应用
     *
     * @param iErrCode 错误码
     * @return
     */
    public static int returnErrCode(int iErrCode) {
        int AdkEMV_ERR_BASE = -8000;
        if (iErrCode < AdkEMV_ERR_BASE) {
            return iErrCode;
        } else {
            return AdkEMV_ERR_BASE + iErrCode;
        }
    }

    /**
     * 功能： 整型数转换成高位在前的字符串表示(按照网络字节格式存放)
     *
     * @param ulSource 待转换的长整数
     * @param uiTCnt   目标缓冲区的大小(字节数)
     * @param psTarget 转换后的串
     */
    public static void Common_Long2Char(long ulSource, int uiTCnt, byte[] psTarget) {
        if (null == psTarget) {
            return;
        }

        for (int i = 0; i < uiTCnt; i++) {
            psTarget[i] = (byte) (ulSource >> (8 * (uiTCnt - i - 1)));
        }
    }

    /**
     * 功能： 整型数转换成高位在前的字符串表示(按照网络字节格式存放)
     *
     * @param uiSource 待转换的长整数
     * @param uiTCnt   目标缓冲区的大小(字节数)
     * @return 转换后的串
     */
    public static byte[] Common_Int2Char(int uiSource, int uiTCnt) {
        byte[] psTarget = new byte[uiTCnt];
        for (int i = 0; i < uiTCnt; i++) {
            psTarget[i] = (byte) (uiSource >> (8 * (uiTCnt - i - 1)));
        }
        return psTarget;
    }

    public static int Uint8(byte val) {
        return val & 0xFF;
    }

    /**
     * 高位在前的字符串表示转换为整型数
     *
     * @param psSource 待转换的串，最多4个字符
     * @param uiSCnt   源串的长度，最大为4，超过时，取psSource前面的4个字符进行转换
     * @return 转换后整数
     */
    public static long Common_Char2Long(byte[] psSource, int uiSCnt) {
        int i;
        long ulTmp;

        if (null == psSource) {
            return 0;
        }

        ulTmp = 0L;
        for (i = 0; i < uiSCnt; i++) {
            ulTmp |= (Uint8(psSource[i]) << 8 * (uiSCnt - i - 1));
        }

        return ulTmp;
    }

    /**
     * 高位在前的字符串表示转换为整型数
     *
     * @param psSource 待转换的串，最多4个字符
     * @param uiSCnt   源串的长度，最大为4，超过时，取psSource前面的4个字符进行转换
     * @return 转换后整数
     */
    public static int Common_Char2Int(byte[] psSource, int uiSCnt) {
        int i;
        int ulTmp;

        if (null == psSource) {
            return 0;
        }

        ulTmp = 0;
        for (i = 0; i < uiSCnt; i++) {
            ulTmp |= (Uint8(psSource[i]) << 8 * (uiSCnt - i - 1));
        }

        return ulTmp;
    }

    /**
     * 把TLV数据填入缓存数据中
     *
     * @param iTag      标签
     * @param iValueLen iTag数据长度
     * @param psValue   iTag数据
     * @return 缓存数据
     */
    public static byte[] Common_SetTlvToData(int iTag, int iValueLen, byte[] psValue) {
        byte[] sTagBuf = new byte[4];
        byte ucTagLen;
        byte[] sLenBuf = new byte[4];
        int iLenBufLen;

        if (iValueLen < 0) {
            return null;
        }

        if ((null == psValue) && (iValueLen > 0)) {
            return null;
        }

        int iMinValueLen = iValueLen;
        if ((null != psValue) && (psValue.length > 0)) {
            iMinValueLen = Math.min(iValueLen, psValue.length);
        }

        if (iTag > 0xFFFF) {
            ucTagLen = 3;
        } else if (iTag > 0xFF) {
            ucTagLen = 2;
        } else {
            ucTagLen = 1;
        }

        Common_Long2Char(iTag, ucTagLen, sTagBuf);

        if (iMinValueLen < 128)// 1个字节
        {
            sLenBuf[0] = (byte) iMinValueLen;
            iLenBufLen = 1;

        } else if (iMinValueLen < 256)// 2个字节
        {
            sLenBuf[0] = (byte) 0x81;
            sLenBuf[1] = (byte) iMinValueLen;
            iLenBufLen = 2;
        } else if (iMinValueLen < 256 * 256)// 3个字节
        {
            sLenBuf[0] = (byte) 0x82;
            sLenBuf[1] = (byte) (iMinValueLen / 256);
            sLenBuf[2] = (byte) (iMinValueLen % 256);
            iLenBufLen = 3;
        } else // 4个字节
        {
            sLenBuf[0] = (byte) 0x83;
            sLenBuf[1] = (byte) (iMinValueLen / (256 * 256));
            sLenBuf[2] = (byte) (iMinValueLen / (256) % 256);
            sLenBuf[3] = (byte) (iMinValueLen % 256);
            iLenBufLen = 4;
        }

        byte[] psData = new byte[ucTagLen + iLenBufLen + iMinValueLen];

        System.arraycopy(sTagBuf, 0, psData, 0, ucTagLen);
        System.arraycopy(sLenBuf, 0, psData, ucTagLen, iLenBufLen);

        if (0 != iMinValueLen) {
            System.arraycopy(psValue, 0, psData, ucTagLen + iLenBufLen, iMinValueLen);
        }

//        LogCat.i(TAG, _FUNC_LINE_() + "Common_SetTlvToData = " + StringUtil.toHexString(psData, psData.length));

        return psData;
    }

    /**
     * 功能描述:把TLV数据填入缓存数据中
     *
     * @param iTag        标签
     * @param iValueLen   iTag对应数据长度
     * @param psValue     iTag对应数据值
     * @param psTLVString 添加iTag对应TLV数据后的TLV数据串
     */
    public static void BaseC_BuildTLVString(int iTag, int iValueLen, byte[] psValue, byte[] psTLVString) {
        byte[] sTagBuf = new byte[4];
        byte ucTagLen;
        byte[] sLenBuf = new byte[4];
        int iLenBufLen;

        if (iValueLen < 0) {
            return;
        }

        // 获取TAG
        if (iTag > 0xFFFF) {
            ucTagLen = 3;
        } else if (iTag > 0xFF) {
            ucTagLen = 2;
        } else {
            ucTagLen = 1;
        }

        Common_Long2Char(iTag, ucTagLen, sTagBuf);
        // 获取Length
        if (iValueLen < 128)// 1
        {
            sLenBuf[0] = (byte) iValueLen;
            iLenBufLen = 1;

        } else if (iValueLen < 256)// 2
        {
            sLenBuf[0] = (byte) 0x81;
            sLenBuf[1] = (byte) iValueLen;
            iLenBufLen = 2;
        } else if (iValueLen < 256 * 256)// 3
        {
            sLenBuf[0] = (byte) 0x82;
            sLenBuf[1] = (byte) (iValueLen / 256);
            sLenBuf[2] = (byte) (iValueLen % 256);
            iLenBufLen = 3;
        } else {
            sLenBuf[0] = (byte) 0x83;
            sLenBuf[1] = (byte) (iValueLen / (256 * 256));
            sLenBuf[2] = (byte) (iValueLen / (256) % 256);
            sLenBuf[3] = (byte) (iValueLen % 256);
            iLenBufLen = 4;
        }
        // 添加TLV数据到TLV数据串
        System.arraycopy(sTagBuf, 0, psTLVString, 0, ucTagLen);
        System.arraycopy(sLenBuf, 0, psTLVString, ucTagLen, iLenBufLen);
        System.arraycopy(psValue, 0, psTLVString, ucTagLen + iLenBufLen, iValueLen);

        return;
    }

    /**
     * 功能描述:从TLV包中获取特定Tag对应的数据
     *
     * @param iTag          需要获取数据对应的Tag值
     * @param psTLVString   TLV包串
     * @param iTLVStringLen TLV包长度
     * @return 获取到的Tag数据串
     */
    public static byte[] BaseC_CaptureTLV(int iTag, byte[] psTLVString, int iTLVStringLen) {
        int iLenNum;
        int iOffset;
        int iTmpTag;
        int iTmpLen;

        if (iTLVStringLen <= 0) {
            return null;
        }

        iOffset = 0;
        while (iOffset < iTLVStringLen) {
            // 获取TAG
            iTmpTag = 0;
            iTmpTag = iTmpTag * 256 + Uint8(psTLVString[iOffset]);
            if ((psTLVString[iOffset] & 0x1f) == 0x1f)// 有第二个字节
            {
                iOffset += 1;
                iTmpTag = iTmpTag * 256 + Uint8(psTLVString[iOffset]);
                if ((psTLVString[iOffset] & 0x80) == 0x80)// 有第三个字节
                {
                    iOffset += 1;
                    iTmpTag = iTmpTag * 256 + Uint8(psTLVString[iOffset]);
                }
            }
            iOffset += 1;

            // 获取Length
            iTmpLen = 0;
            if ((psTLVString[iOffset] & 0x80) == 0x80)// 长度为多个字节
            {
                iLenNum = psTLVString[iOffset] & 0x7f;
                iOffset++;
                while (iLenNum > 0) {
                    iTmpLen = iTmpLen * 256 + Uint8(psTLVString[iOffset]);
                    iOffset++;
                    iLenNum--;
                }
            } else// 长度为1个字节
            {
                iTmpLen += Uint8(psTLVString[iOffset]);
                iOffset++;
            }

            // 获取Value
            if (iTag == iTmpTag) {
                return Arrays.copyOfRange(psTLVString, iOffset, iTmpLen + iOffset);
            }
            iOffset += iTmpLen;
        }

        return null;
    }

    /**
     * 功能描述: 修改TLV数据包中某个TAG的数据
     *
     * @param usTag     需更新的数据对应的tag值
     * @param psData    需更新的数据串
     * @param iDataLen  需更新的数据串的长度
     * @param psTLVData TLV包串
     * @param iTLVLen   TLV包长度
     */
    public static void BaseC_ModifiyTLV(int usTag, byte[] psData, int iDataLen, byte[] psTLVData, int iTLVLen) {
        int usTempLen, usTempTag;
        int iOffset;

        if (iTLVLen == 0 || iDataLen == 0) {
            return;
        }

        iOffset = 0;
        while (iOffset < iTLVLen) {
            if ((psTLVData[iOffset] & 0x1f) == 0x1f)// 130825 0x0f-->0x1f
            {
                usTempTag = psTLVData[iOffset] * 256 + psTLVData[iOffset + 1];
                iOffset += 2;
            } else {
                usTempTag = psTLVData[iOffset];
                iOffset += 1;
            }

            if ((psTLVData[iOffset] & 0x80) == 0x80) {
                iOffset++;
            }

            usTempLen = psTLVData[iOffset];
            if (usTag == usTempTag) {
                if (usTempLen != iDataLen) {
                    return;
                }

                System.arraycopy(psData, 0, psTLVData, iOffset + 1, usTempLen);
                break;
            }

            iOffset = iOffset + 1 + usTempLen;
        }
    }

    /**
     * 打印日志时获取当前的程序文件名、行号、方法名 输出格式为：[FileName | LineNumber | MethodName]
     *
     * @return
     */
    public static String getFileLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        StringBuffer toStringBuffer = new StringBuffer("[").append(traceElement.getFileName()).append(" | ")
                .append(traceElement.getLineNumber()).append(" | ").append(traceElement.getMethodName()).append("]");
        return toStringBuffer.toString();
    }

    // 当前文件名
    public static String _FILE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getFileName();
    }

    // 当前方法名
    public static String _FUNC_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getMethodName();
    }

    // 当前行号
    public static int _LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return traceElement.getLineNumber();
    }

    public static String _FUNC_LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return "[" + traceElement.getMethodName() + "__" + traceElement.getLineNumber() + "]";
    }

    public static String _FILE_FUNC_LINE_() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return "[" + traceElement.getFileName() + "__" + traceElement.getMethodName() + "__"
                + traceElement.getLineNumber() + "]";
    }

    // 当前时间
    public static String _TIME_() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(now);
    }

    /**
     * 从磁道2数据中获取主帐号
     *
     * @param track
     * @return
     * @date 2015年5月22日下午3:28:14
     * @example
     */
    public static String getPan(String track) {
        if (track == null)
            return null;

        int len = track.indexOf('=');
        if (len < 0) {
            len = track.indexOf('D');
            if (len < 0)
                return null;
        }

        if ((len < 13) || (len > 19))
            return null;
        return track.substring(0, len);
    }

    /**
     * 判定是否为IC卡
     *
     * @param track
     * @return
     */
    public static boolean isIcCard(String track) {
        if (track == null)
            return false;

        int index = track.indexOf('=');
        if (index < 0) {
            index = track.indexOf('D');
            if (index < 0)
                return false;
        }

        if (index + 6 > track.length())
            return false;

        if ("2".equals(track.substring(index + 5, index + 6)) || "6".equals(track.substring(index + 5, index + 6))) {
            return true;
        }
        return false;
    }

    /**
     * 获取有效期
     *
     * @param track
     * @return
     */
    public static String getExpDate(String track) {
        if (track == null)
            return null;

        int index = track.indexOf('=');
        if (index < 0) {
            index = track.indexOf('D');
            if (index < 0)
                return null;
        }

        if (index + 5 > track.length())
            return null;
        return track.substring(index + 1, index + 5);
    }


    public static <T> void moveToFirst(T[] x, int xLen, int index) {
        if (0 < index && index < xLen) {
            T[] y = (T[]) new Object[xLen];
            for (int i = 0, j = 1; i < xLen; i++) {
                if (i == index) {
                    y[0] = x[i];
                    continue;
                }
                y[j++] = x[i];
            }
            System.arraycopy(y, 0, x, 0, y.length);
        }
    }

    public static int memcmp(byte[] buf1, byte[] buf2, int Asc_len) {
        if (buf1 != null && buf2 != null && Asc_len > 0) {
            return memcmp(buf1, 0, buf2, 0, Asc_len);
        }

        return -1;
    }

    public static int memcmp(byte[] buf1, int start, byte[] buf2, int Asc_len) {
        return memcmp(buf1, start, buf2, 0, Asc_len);
    }

    public static int memcmp(byte[] buf1, byte[] buf2, int start, int Asc_len) {
        return memcmp(buf1, 0, buf2, start, Asc_len);
    }

    public static int memcmp(byte[] buf1, int start1, byte[] buf2, int start2,
                             int Asc_len) {
        int i = 0;
        for (i = 0; i < Asc_len && i < (buf1.length - start1)
                && i < (buf2.length - start2); i++) {
            if (buf1[i + start1] < buf2[i + start2]) {
                return -1;
            } else if (buf1[i + start1] > buf2[i + start2]) {
                return 1;
            } else {
            }
        }

        if ((i == Asc_len) || (buf1.length == buf2.length)) {
            return 0;
        } else if (buf1.length > buf2.length) {
            return 2;
        } else {
            return -2;
        }
    }

    public static String dateTimeFormat(String strDate, String strTime) {
        if (null == strDate || null == strTime)
            return null;

        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss"); //加上时间
        //必须捕获异常
        try {
            Date date = simpleDateFormat.parse(strDate + " " + strTime);
            return sDateFormat.format(date);
        } catch (ParseException px) {
            px.printStackTrace();
        }

        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        return formatDate.format(date);
    }

    public static String dateTimeFormat(String strSrc, boolean isDate) {
        if (null == strSrc)
            return null;

        //注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
        SimpleDateFormat simpleDateFormat;
        SimpleDateFormat sDateFormat;

        if (isDate) {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            sDateFormat = new SimpleDateFormat("yy/MM/dd"); //仅日期
        } else {
            simpleDateFormat = new SimpleDateFormat("HHmmss");
            sDateFormat = new SimpleDateFormat("HH:mm:ss"); //仅时间
        }
        //必须捕获异常
        try {
            Date date = simpleDateFormat.parse(strSrc);
            return sDateFormat.format(date);
        } catch (ParseException px) {
            px.printStackTrace();
        }
        Date date = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        return formatDate.format(date);
    }

    //List深拷贝
    public static <T> List<T> deepCopy11(List<T> src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        @SuppressWarnings("unchecked")
        List<T> dest = (List<T>) in.readObject();
        return dest;
    }

    //List深拷贝
    public static <E> List<E> deepCopy(List<E> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<E> dest = (List<E>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

}
