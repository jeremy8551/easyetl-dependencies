package icu.etl.crypto;

import org.junit.Assert;
import org.junit.Test;

public class DESEncryptTest {
    private static String key = "测试密钥1"; // 8个字节64的密钥

    @Test
    public void test() throws Exception {
        String msg = "hello world. 你好，DES";
        System.out.println("加密前：" + msg);
        byte[] encryptBytes = DESEncrypt.encrypt(msg.getBytes(), key.getBytes());
        System.out.println("加密后：" + new String(encryptBytes));
        byte[] deMsgBytes = DESEncrypt.decrypt(encryptBytes, key.getBytes());
        System.out.println("解密后：" + new String(deMsgBytes));

        System.out.println(": " + DESEncrypt.decrypt(DESEncrypt.encrypt(msg, "UTF-8", key.getBytes()), "UTF-8", key.getBytes()));
        Assert.assertEquals(DESEncrypt.decrypt(DESEncrypt.encrypt(msg, "UTF-8", key.getBytes()), "UTF-8", key.getBytes()), msg);
        System.out.println(DESEncrypt.encrypt("000000", "UTF-8", "dlvandaz".getBytes())); // 加密用户密码
    }
}
