/*
 * ============================================================================
 * = COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or
 *    nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *    or disclosed except in accordance with the terms in that agreement.
 *        Copyright (C) 2019 -? PAX Technology, Inc. All rights reserved.
 * Revision History:
 * Date	                     Author	                        Action
 * 19-3-1 下午8:25  	     lk           	    Create/Add/Modify/Delete
 * ============================================================================
 */

package com.pax.order.util;



import com.pax.market.api.sdk.java.base.util.base64.Base64;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String decryptAESwithFixedKey(String content) throws Exception{
        final String aesKey = "61bf11bae0da3d5c"; // DEV MODE
//        final String aesKey = "f8de2b28d5bb91ba"; // PROD MODE

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7PADDING");
        SecretKeySpec secretKeySpec = new SecretKeySpec(aesKey.getBytes(),"AES");
        cipher.init(cipher.DECRYPT_MODE,secretKeySpec);
        byte[] encryptData = android.util.Base64.decode(content, android.util.Base64.DEFAULT);
        byte[] original = cipher.doFinal(encryptData);
        return new String(original,"UTF-8");
//        SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(),"AES/ECB/PKCS7PADDING");
//        try{
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(cipher.DECRYPT_MODE,keySpec);
//            byte[] original = cipher.doFinal(content);
//            return new String(original,"UTF-8");
//        }catch (Exception e){
//            AppLog.e("SecurityUtils error!!"+e);
//        }
//        return null;
    }

    public static String genHMAC(String data,String key){
        byte[] result = null;

        try {
            SecretKeySpec signinKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signinKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = Base64.encodeBase64(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
        if (null != result) {
            return new String(result);
        } else {
            return null;
        }

    }
}
