package icu.etl.crypto;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import icu.etl.util.ResourcesUtils;
import icu.etl.util.StringUtils;

/**
 * DES对称加密算法
 *
 * @author jeremy8551@qq.com
 * @createtime 2018-11-26
 */
public class DESEncrypt {

    /**
     * 加密算法类型 DES
     */
    public final static String ALGORITHM = "DES";

    /**
     * 字符串转换格式
     */
    public final static String TRANSFORMATION = "DES/ECB/PKCS5Padding";

    /**
     * 对字符串进行加密
     *
     * @param str         待加密的字符串
     * @param charsetName 字符串的字符集编码
     * @param key         8字节64位的长度的字节数组密钥
     * @return 加密后十六进制的字符串
     */
    public static String encrypt(String str, String charsetName, byte[] key) {
        checkSecureKey(key);
        try {
            byte[] bytes = encrypt(str.getBytes(charsetName), key); // 对字符串加密
            return StringUtils.toHexString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(1, str), e);
        }
    }

    /**
     * 对十六进制的字符串进行解密
     *
     * @param str         十六进制的字符串
     * @param charsetName 解析后字符串的字符集编码
     * @param key         8字节64位长度的密钥字节数组
     * @return 解密后的字符串
     */
    public static String decrypt(String str, String charsetName, byte[] key) {
        checkSecureKey(key);
        try {
            byte[] bytes = StringUtils.parseHexString(str);
            byte[] decrypt = decrypt(bytes, key);
            return new String(decrypt, charsetName);
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(2, str), e);
        }
    }

    /**
     * 给字符串加密
     *
     * @param bytes 字节数组
     * @param key   8字节64位长度的字节数组
     * @return 返回加密后的字节数组
     */
    public static byte[] encrypt(byte[] bytes, byte[] key) {
        checkSecureKey(key);
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec keySpec = new DESKeySpec(key);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
            byte[] enMsgBytes = cipher.doFinal(bytes);
            return enMsgBytes;
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(3, StringUtils.toHexString(bytes)), e);
        }
    }

    /**
     * 解密
     *
     * @param bytes 加密后的字节数组
     * @param key   8字节64位长度的密钥字节数组
     * @return 返回解密后的字节数组
     */
    public static byte[] decrypt(byte[] bytes, byte[] key) {
        checkSecureKey(key);
        try {
            Cipher deCipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeyFactory deDecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            KeySpec deKeySpec = new DESKeySpec(key);
            SecretKey deSecretKey = deDecretKeyFactory.generateSecret(deKeySpec);
            deCipher.init(Cipher.DECRYPT_MODE, deSecretKey, new SecureRandom());
            byte[] deMsgBytes = deCipher.doFinal(bytes);
            return deMsgBytes;
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(4, StringUtils.toHexString(bytes)), e);
        }
    }

    /**
     * 校验密钥是否符合规范
     *
     * @param keys 密钥字节数组
     */
    private static void checkSecureKey(byte[] keys) {
        if (keys == null || keys.length < 8) { // 校验密钥长度是否符合规范
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(5, StringUtils.toHexString(keys)));
        }
    }
}
