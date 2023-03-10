package com.Sm4;

/**
 * @ClassName Sm2Util
 * @Description TODO
 * @Author QiBin
 * @Date 2022/6/10 22:25
 * @Version 1.0
 **/
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.Base64;

/**
 * @author WangJing
 * @Description SM2实现工具类
 * @date 2021/11/24 16:10
 */
public class Sm2Util {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 根据publicKey对原始数据data，使用SM2加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static byte[] encrypt(byte[] data, PublicKey publicKey) {
        ECPublicKeyParameters localECPublicKeyParameters = null;

        if (publicKey instanceof BCECPublicKey) {
            BCECPublicKey localECPublicKey = (BCECPublicKey) publicKey;
            ECParameterSpec localECParameterSpec = localECPublicKey.getParameters();
            ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(),
                    localECParameterSpec.getG(), localECParameterSpec.getN());
            localECPublicKeyParameters = new ECPublicKeyParameters(localECPublicKey.getQ(), localECDomainParameters);
        }
        SM2Engine localSM2Engine = new SM2Engine();
        localSM2Engine.init(true, new ParametersWithRandom(localECPublicKeyParameters, new SecureRandom()));
        byte[] arrayOfByte2;
        try {
            arrayOfByte2 = localSM2Engine.processBlock(data, 0, data.length);
            return arrayOfByte2;
        } catch (InvalidCipherTextException e) {

            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据privateKey对加密数据encodedata，使用SM2解密
     *
     * @param encodedata
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(byte[] encodedata, PrivateKey privateKey) {
        SM2Engine localSM2Engine = new SM2Engine();
        BCECPrivateKey sm2PriK = (BCECPrivateKey) privateKey;
        ECParameterSpec localECParameterSpec = sm2PriK.getParameters();
        ECDomainParameters localECDomainParameters = new ECDomainParameters(localECParameterSpec.getCurve(),
                localECParameterSpec.getG(), localECParameterSpec.getN());
        ECPrivateKeyParameters localECPrivateKeyParameters = new ECPrivateKeyParameters(sm2PriK.getD(),
                localECDomainParameters);
        localSM2Engine.init(false, localECPrivateKeyParameters);
        try {
            byte[] arrayOfByte3 = localSM2Engine.processBlock(encodedata, 0, encodedata.length);
            return arrayOfByte3;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 私钥签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] signByPrivateKey(byte[] data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        sig.initSign(privateKey);
        sig.update(data);
        byte[] ret = sig.sign();
        return ret;
    }

    /**
     * 公钥验签
     *
     * @param data
     * @param publicKey
     * @param signature
     * @return
     * @throws Exception
     */
    public static boolean verifyByPublicKey(byte[] data, PublicKey publicKey, byte[] signature) throws Exception {
        Signature sig = Signature.getInstance(GMObjectIdentifiers.sm2sign_with_sm3.toString(), BouncyCastleProvider.PROVIDER_NAME);
        sig.initVerify(publicKey);
        sig.update(data);
        boolean ret = sig.verify(signature);
        return ret;
    }


    private String testStr = "wangjing";

    java.security.PublicKey publicKey = null;
    java.security.PrivateKey privateKey = null;

    @Test
    public  void test() throws Exception {
        //生成公私钥对
        String[] keys = KeyUtils.generateSmKey();

        System.out.println("原始字符串：" + testStr);
        System.out.println("公钥：" + keys[0]);
        publicKey = KeyUtils.createPublicKey(keys[0]);

        System.out.println("私钥：" + keys[1]);
        privateKey = KeyUtils.createPrivateKey(keys[1]);

        System.out.println("");


        byte[] encrypt = Sm2Util.encrypt(testStr.getBytes(), publicKey);
        String encryptBase64Str = Base64.getEncoder().encodeToString(encrypt);
        System.out.println("加密数据：" + encryptBase64Str);

        byte[] decode = Base64.getDecoder().decode(encryptBase64Str);
        byte[] decrypt = Sm2Util.decrypt(decode, privateKey);
        System.out.println("解密数据：" + new String(decrypt));

        byte[] sign = Sm2Util.signByPrivateKey(testStr.getBytes(), privateKey);
        System.out.println("数据签名：" + Base64.getEncoder().encodeToString(sign));

        boolean b = Sm2Util.verifyByPublicKey(testStr.getBytes(), publicKey, sign);
        System.out.println("数据验签：" + b);
    }

}
