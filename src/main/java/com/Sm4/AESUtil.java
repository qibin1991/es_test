package com.Sm4;

/**
 * @ClassName AESUtil
 * @Description TODO
 * @Author QiBin
 * @Date 2022/8/11 17:33
 * @Version 1.0
 **/
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /***
     * aes-128-gcm 加密
     * @params msg 为加密信息 password为32位的16进制key
     * @return 返回base64编码，也可以返回16进制编码
     **/
    public static String Encrypt(String msg, String password) {
        try {
            byte[] sSrc = msg.getBytes("UTF-8"); //修改添加字符集
            byte[] sKey = AESUtil.parseHexStr2Byte(password);
            SecretKeySpec skeySpec = new SecretKeySpec(sKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            //这边是获取一个随机的iv 默认为12位的
            byte[] iv = cipher.getIV();
            //执行加密
            byte[] encryptData = cipher.doFinal(sSrc);
            //这边进行拼凑 为 iv + 加密后的内容
            byte[] message = new byte[12 + sSrc.length + 16];
            System.arraycopy(iv, 0, message, 0, 12);
            System.arraycopy(encryptData, 0, message, 12, encryptData.length);

            return Base64.getEncoder().encodeToString(message);
        } catch (Exception ex) {
            return null;
        }
    }

    /***
     * aes-128-gcm 解密
     * @return msg 返回字符串
     */
    public static String Decrypt(String serect, String password) {
        try {
            byte[] sSrc = Base64.getDecoder().decode(serect);
            byte[] sKey = AESUtil.parseHexStr2Byte(password);

            GCMParameterSpec iv = new GCMParameterSpec(128, sSrc, 0, 12);
            Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");
            SecretKey key2 = new SecretKeySpec(sKey, "AES");

            cipher.init(Cipher.DECRYPT_MODE, key2, iv);

            //这边和nodejs不同的一点是 不需要移除后面的16位
            byte[] decryptData = cipher.doFinal(sSrc, 12, sSrc.length - 12);

            return new String(decryptData);
        } catch (Exception ex) {
            return null;
        }
    }

    public static void main(String[] args) {
        String testMsg = "{'ai':'test-accountId','name':'username','idNum':'371321199012310912'}";
        String testPwd = "10210b07c5cf31b30f722f9b5896de5c";
        String enc = AESUtil.Encrypt(testMsg, testPwd);
        System.out.println("加密结果 " + testPwd);
        String dec = AESUtil.Decrypt(enc, testPwd);
        System.out.println("解密结果 " + dec);
    }
}