package com.sztus.azeroth.microservice.customer.client.object.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/*
 * 加密工具类
 */
public class EncryptUtil {

    private static final String ENCODE_RULES = "@RBfintech";

    private static final String ENCODING = "UTF-8"; // 编码

    private static final String ALGORITHM = "AES"; //算法

    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"; // 默认的加密算法，ECB模式

    /**
     *  AES加密，用于纯属给前端的字段加密
     */
    public static String encrypt(String encryptText, String secretKey) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(128);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(secretKey.getBytes(),ALGORITHM));
        byte[] b = cipher.doFinal(encryptText.getBytes(ENCODING));
        //采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);
    }

    /**
     * AES解密，用于传输给前端的字段解密
     */
    public static String decrypt(String decryptText, String secretKey) throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(128);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey.getBytes(), ALGORITHM));
        byte[] b = cipher.doFinal(Base64.decodeBase64(decryptText));
        //采用base64算法进行转码,避免出现中文乱码
        return new String(b);
    }

    /**
     * AES加密算法，用于数据库加密，秘钥指定不可改
     * @param content
     * @return
     */
    public static String AESEncode(String content) {
        try {
            if (!StringUtils.isEmpty(content)) {
                KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(ENCODE_RULES.getBytes());
                keygen.init(128, random);
                SecretKey original_key = keygen.generateKey();
                byte[] raw = original_key.getEncoded();
                SecretKey key = new SecretKeySpec(raw, ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] byteEncode = content.getBytes(ENCODING);
                byte[] byteAES = cipher.doFinal(byteEncode);
                return new BASE64Encoder().encode(byteAES);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * AES解密算法，用于数据库字段解密，秘钥指定不可改
     * @param content
     * @return
     */
    public static String AESDecode(String content) {
        try {
            if (!StringUtils.isEmpty(content)) {
                KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(ENCODE_RULES.getBytes());
                keygen.init(128, random);
                SecretKey original_key = keygen.generateKey();
                byte[] raw = original_key.getEncoded();
                SecretKey key = new SecretKeySpec(raw, ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] byteContent = new BASE64Decoder().decodeBuffer(content);
                byte[] byteDecode = cipher.doFinal(byteContent);
                return new String(byteDecode, ENCODING);
            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IOException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String ssn = "beal@sztus.com";
        System.out.println(AESEncode(ssn).length());
        System.out.println(AESEncode(ssn));
    }
}