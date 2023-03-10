package com.Sm4;

/**
 * @ClassName Sm4Util
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/10 22:11
 * @Version 1.0
 **/
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * sm4加密算法工具类
 *
 * @author Marydon
 * @version 1.0
 * @explain sm4加密、解密与加密结果验证
 * 可逆算法
 * @creationTime 2018年7月6日上午11:46:59
 * @since
 */
public class Sm4Util {

    private static String key = "****";//密钥 16位
    //    private String mode ="CBC";
//    private String Padding = "PKCS5Padding";
    private static String iv = "***";  //16位


    /**
     * cbc加密
     *
     * @param plainTxt
     * @return
     */
    public static String encrypt(String plainTxt) {
        String cipherTxt = "";
        SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(CharsetUtil.CHARSET_UTF_8), iv.getBytes(CharsetUtil.CHARSET_UTF_8));
        byte[] encrypHex = sm4.encrypt(plainTxt);
        cipherTxt = Base64.encode(encrypHex);
        return cipherTxt;
    }

    /**
     * cbc解密
     *
     * @param cipherTxt
     * @return
     */
    public static String decrypt(String cipherTxt) {
        String plainTxt = "";
        try {
            SymmetricCrypto sm4 = new SM4(Mode.CBC, Padding.PKCS5Padding, key.getBytes(CharsetUtil.CHARSET_UTF_8), iv.getBytes(CharsetUtil.CHARSET_UTF_8));
            byte[] cipherHex = Base64.decode(cipherTxt.trim());
            plainTxt = sm4.decryptStr(cipherHex, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainTxt;
    }
    //测试
    public static void main(String[] args) {
        String content = "fisco bcos";
        // key必须是16位
        String key="1234567890123456";
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        String encryptHex = sm4.encryptHex(content);
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(encryptHex+"\r\n"+decryptStr);


        // System.out.println("加密="+encryptHex+"\n解密="+decryptStr);
//        System.out.println("加密后=" + encrypt);
//        System.out.println("解密=" + decryptStr);
    }

}