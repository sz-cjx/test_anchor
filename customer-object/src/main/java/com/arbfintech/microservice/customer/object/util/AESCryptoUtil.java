package com.arbfintech.microservice.customer.object.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESCryptoUtil {

    private static String encodeRules = "@RBfintech";

    private static String encodeType = "AES";

    private static String unicodeType = "utf-8";

    /**
     * 加密
     * @param content
     * @return
     */
    public static String AESEncrypt(String content) {
        try {
            if (!StringUtils.isEmpty(content)) {
                KeyGenerator keygen = KeyGenerator.getInstance(encodeType);
                keygen.init(128, new SecureRandom(encodeRules.getBytes()));
                SecretKey original_key = keygen.generateKey();
                byte[] raw = original_key.getEncoded();
                SecretKey key = new SecretKeySpec(raw, encodeType);
                Cipher cipher = Cipher.getInstance(encodeType);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] byte_encode = content.getBytes(unicodeType);
                byte[] byte_AES = cipher.doFinal(byte_encode);
                return Base64.getEncoder().encodeToString(byte_AES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 解密
     * @param content
     * @return
     */
    public static String AESDecrypt(String content) {
        try {
            if (!StringUtils.isEmpty(content)) {
                KeyGenerator keygen = KeyGenerator.getInstance(encodeType);
                keygen.init(128, new SecureRandom(encodeRules.getBytes()));
                SecretKey original_key = keygen.generateKey();
                byte[] raw = original_key.getEncoded();
                SecretKey key = new SecretKeySpec(raw, encodeType);
                Cipher cipher = Cipher.getInstance(encodeType);
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] byte_content = Base64.getDecoder().decode(content);
                byte[] byte_decode = cipher.doFinal(byte_content);
                return new String(byte_decode, unicodeType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject decryptData(JSONObject dataJson) {
        if (CollectionUtils.isEmpty(dataJson)) {
            return null;
        }

        JSONObject decodedData = new JSONObject(){{
            putAll(dataJson);
        }};
        for (String field : CustomerFieldKey.getEncodeFieldList()) {
            if (dataJson.containsKey(field) && StringUtils.isNotEmpty(dataJson.getString(field))) {
                decodedData.put(field, AESDecrypt(dataJson.getString(field)));
            }
        }
        return decodedData;
    }

    public static JSONObject encryptData(JSONObject dataJson) {
        if (CollectionUtils.isEmpty(dataJson)) {
            return null;
        }

        JSONObject decodedData = new JSONObject(){{
            putAll(dataJson);
        }};
        for (String field : CustomerFieldKey.getEncodeFieldList()) {
            if (dataJson.containsKey(field) && StringUtils.isNotEmpty(dataJson.getString(field))) {
                decodedData.put(field, AESEncrypt(dataJson.getString(field)));
            }
        }
        return decodedData;
    }
}
