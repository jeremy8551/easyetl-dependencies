package icu.etl.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

import icu.etl.util.FileUtils;
import icu.etl.util.IO;
import icu.etl.util.Numbers;
import icu.etl.util.ResourcesUtils;
import icu.etl.util.Terminate;

/**
 * MD5工具类 <br>
 * 可生成文件生成md5码值 <br>
 * 可生成字符串的md5码值 <br>
 *
 * @author jeremy8551@qq.com
 * @createtime 2018-11-27
 */
public class MD5Encrypt {

    /**
     * 默认使用16进制生成文件的MD5码值
     *
     * @param file 文件
     * @param obj  用于判断是否终止生成MD5值，可以是null
     * @return
     */
    public static String encrypt(File file, Terminate obj) {
        return MD5Encrypt.encrypt(file, 16, obj); // 使用16进制生成文件的md5码值
    }

    /**
     * 生成文件的MD5码
     *
     * @param file  数据文件
     * @param radix 转为的进制数,目前可用的参数有: 16 , 32, 128
     * @param obj   用于判断是否终止生成MD5值，可以是null
     * @return
     */
    public static String encrypt(File file, int radix, Terminate obj) {
        FileUtils.checkPermission(file, true, false);
        if (!Numbers.inArray(radix, 16, 32, 128)) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(6, radix));
        }

        FileInputStream in = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();

            in = new FileInputStream(file); // 从文件中读取二进制字节数组
            byte[] array = new byte[1024];
            int len = -1;
            while ((len = in.read(array)) != -1) {
                if (obj != null && obj.isTerminate()) {
                    break;
                }

                md.update(array, 0, len);
            }

            // 生成十六进制的MD5数值
            byte[] bytes = md.digest();
            StringBuilder buf = new StringBuilder(50);
            for (int i = 0; i < bytes.length; i++) {
                if (obj != null && obj.isTerminate()) {
                    break;
                }

                int value = ((int) bytes[i]) & 0xff;
                if (value < 16) {
                    buf.append('0');
                }
                buf.append(Integer.toHexString(value));
            }
            return buf.toString();
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(7, file.getAbsolutePath()), e);
        } finally {
            IO.close(in);
        }
    }

    /**
     * 生成字符串str的MD5码
     *
     * @param str 字符串参数
     * @param obj 用于判断是否终止生成MD5值，可以是null
     * @return
     */
    public static String encrypt(String str, Terminate obj) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // 创建一个消息生成器
            md.reset();
            md.update(str.getBytes("UTF-8"));
            byte[] array = md.digest();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (obj != null && obj.isTerminate()) {
                    break;
                }

                if (Integer.toHexString(0xFF & array[i]).length() == 1) { // 如果是0L的话默认在数字前面补4个零字符
                    buf.append("0").append(Integer.toHexString(0xFF & array[i]));
                } else {
                    buf.append(Integer.toHexString(0xFF & array[i]));
                }
            }
            return buf.toString().toUpperCase(); // 生成MD5码
        } catch (Throwable e) {
            throw new RuntimeException(ResourcesUtils.getCryptoMessage(8, str), e);
        }
    }

}