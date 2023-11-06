package icu.etl.crypto;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import icu.etl.util.ResourcesUtils;
import icu.etl.util.StringUtils;

/**
 * 使用rsa 非对称加密算法加密字符串 <br>
 * <br>
 * 公钥加密，私钥解密；<br>
 * 私钥加签，公钥验签;
 *
 * @author jeremy8551@qq.com
 * @createtime 2018-11-28
 */
public class RSAEncrypt {

    /** RSA 非对称加密算法 */
    public final static String KEY_ALGORITHM = "RSA";

    /** 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间 */
    public final static int KEY_SIZE = 512;

    /** 公钥 */
    public final static String PUBLIC_KEY = "RSAPublicKey";

    /** 私钥 */
    public final static String PRIVATE_KEY = "RSAPrivateKey";

    private RSAEncrypt() {
    }

    /**
     * 初始化密钥对
     *
     * @return Map 甲方密钥的Map
     */
    public static Map<String, Object> initKey() throws Exception {
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器
        keyPairGenerator.initialize(KEY_SIZE);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 甲方私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 将密钥存储在map中
        Map<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * 私钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[] 加密数据
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

        // 数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 生成私钥
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 公钥解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[] 解密数据
     */
    public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 取得私钥
     *
     * @param keyMap 密钥map
     * @return byte[] 私钥
     */
    public static byte[] getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return key.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @param keyMap 密钥map
     * @return byte[] 公钥
     */
    public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return key.getEncoded();
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Map<String, Object> keyMap = RSAEncrypt.initKey();

        // 公钥
        byte[] publicKey = RSAEncrypt.getPublicKey(keyMap);

        // 私钥
        byte[] privateKey = RSAEncrypt.getPrivateKey(keyMap);

        // 公钥字符串
        String publicKeyHexStr = StringUtils.toHexString(publicKey);

        // 私钥字符串
        String privateKeyHexStr = StringUtils.toHexString(privateKey);

        System.out.println(ResourcesUtils.getCryptoMessage(9) + "/n" + publicKeyHexStr);
        System.out.println(ResourcesUtils.getCryptoMessage(10) + "/n" + privateKeyHexStr);

        System.out.println(ResourcesUtils.getCryptoMessage(11));
        String str = ResourcesUtils.getCryptoMessage(12);
        System.out.println("/n" + ResourcesUtils.getCryptoMessage(13));
        System.out.println(ResourcesUtils.getCryptoMessage(19) + str);
        // 甲方进行数据的加密
        byte[] code1 = RSAEncrypt.encryptByPrivateKey(str.getBytes(), privateKey);
        System.out.println(ResourcesUtils.getCryptoMessage(14) + StringUtils.toHexString(code1));
        System.out.println(ResourcesUtils.getCryptoMessage(15));
        // 乙方进行数据的解密
        byte[] decode1 = RSAEncrypt.decryptByPublicKey(code1, StringUtils.parseHexString(publicKeyHexStr));
        System.out.println(ResourcesUtils.getCryptoMessage(16) + new String(decode1) + "/n/n");

        System.out.println(ResourcesUtils.getCryptoMessage(17) + "/n/n");

        str = ResourcesUtils.getCryptoMessage(18);
        System.out.println(ResourcesUtils.getCryptoMessage(19) + str);

        // 乙方使用公钥对数据进行加密
        byte[] code2 = RSAEncrypt.encryptByPublicKey(str.getBytes(), publicKey);
        System.out.println(ResourcesUtils.getCryptoMessage(20));
        System.out.println(ResourcesUtils.getCryptoMessage(21) + StringUtils.toHexString(code2)); // 吧字节数组转为64位字符串

        System.out.println(ResourcesUtils.getCryptoMessage(22));
        System.out.println(ResourcesUtils.getCryptoMessage(23));

        // 甲方使用私钥对数据进行解密
        byte[] decode2 = RSAEncrypt.decryptByPrivateKey(code2, StringUtils.parseHexString(privateKeyHexStr));
        System.out.println(ResourcesUtils.getCryptoMessage(24) + new String(decode2));
    }
}
