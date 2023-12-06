package icu.etl.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 网络工具类
 *
 * @author jeremy8551@qq.com
 * @createtime 2013-10-18
 */
public class NetUtils {

    public NetUtils() {
    }

    /**
     * 将 IP 地址转为主机对象
     *
     * @param ip IP地址
     * @return
     * @throws IOException
     */
    public static InetAddress toAddress(String ip) throws IOException {
        if (StringUtils.isBlank(ip)) {
            throw new NullPointerException(ip);
        }

        String[] array = ip.split("\\.");
        byte[] bytes = new byte[4];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (Integer.parseInt(array[i]) & 0xff);
        }
        return InetAddress.getByAddress(bytes);
    }

    /**
     * 将主机名转为主机的 IP 地址信息
     *
     * @param hostname 主机名
     * @return
     */
    public static String toHostAddress(String hostname) {
        try {
            return InetAddress.getByName(hostname).getHostAddress();
        } catch (Throwable e) {
            return hostname;
        }
    }

    /**
     * 判断字符串是否是合法的 mac 地址
     *
     * @param str
     * @return
     */
    public static boolean isMacAddress(String str) {
        if (str == null) {
            return false;
        }

        String[] groups = StringUtils.split(str.toLowerCase(), ':');
        if (groups.length != 6) { // 必须是8段
            return false;
        }

        for (String group : groups) {
            if (group.length() != 2) {
                return false;
            }

            for (int i = 0; i < group.length(); i++) {
                char c = group.charAt(i);
                if (!StringUtils.isNumber(c) && "abcdef".indexOf(c) == -1) {
                    return false;
                }
            }
        }
        return true;

    }

    /**
     * 判断字符串参数是否是一个合法的ip地址
     *
     * @param str
     * @return
     */
    public static boolean isIP(String str) {
        return isIPv4(str) || isIPv6(str);
    }

    /**
     * 判断 IPv4 地址是否正确
     *
     * @param ip
     * @return
     */
    public static boolean isIPv4(String ip) {
        if (ip == null) {
            return false;
        }

        String[] seg = StringUtils.split(ip, '.');
        if (seg.length != 4) { // 必须是8段
            return false;
        }

        for (String str : seg) {
            if (!StringUtils.isNumber(str)) { // 如果不是数字
                return false;
            }

            int n = Integer.parseInt(str);
            if (n < 0 || n > 255) { // 必须在 0-255 之间
                return false;
            }
        }
        return true;
    }

    /**
     * 判断 IPv6 地址是否正确
     *
     * @param ip
     * @return
     */
    public static boolean isIPv6(String ip) {
        if (ip == null) {
            return false;
        }

        String[] groups = StringUtils.split(ip.toLowerCase(), ':');
        if (groups.length <= 1 || groups.length > 8) { // 必须是8段
            return false;
        }

        /**
         * IPv6地址为128位长，但通常写作8组，每组为四个十六进制数的形式。
         * 如果四个数字都是零，可以被省略
         * 同时前导的零可以省略
         * 遵从这些规则，如果因为省略而出现了两个以上的冒号的话，可以压缩为一个，但这种零压缩在地址中只能出现一次。
         *
         * 2001:0DB8:0000:0000:0000:0000:1428:57ab
         * 2001:0DB8:0000:0000:0000::1428:57ab
         * 2001:0DB8:0:0:0:0:1428:57ab
         * 2001:0DB8:0::0:1428:57ab
         * 2001:0DB8::1428:57ab
         */
        for (String group : groups) {
            if (group.length() > 4) { // 只能是0-4之间的位数
                return false;
            }

            for (int i = 0; i < group.length(); i++) {
                char c = group.charAt(i);
                if (!StringUtils.isNumber(c) && "abcdef".indexOf(c) == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断字符串参数是否是host
     *
     * @param str
     * @return
     */
    public static boolean isHost(String str) {
        return str != null && (str.indexOf('.') != -1 || isLocalHost(str));
    }

    /**
     * 判断字符串参数是否是有效的端口号, 从 0 到 65535 之间的整数
     *
     * @param str
     * @return
     */
    public static boolean isPort(String str) {
        int port = -1;
        return str != null && ((port = StringUtils.parseInt(str, -1)) != -1) && port >= 0 && port <= 65535;
    }

    /**
     * 判断是否是本地机器的HOST值
     *
     * @param host
     * @return
     */
    public static boolean isLocalHost(String host) {
        return StringUtils.inArrayIgnoreCase(host, "127.0.0.1", "localhost", "local");
    }

    /**
     * 解析content_Range字段值
     *
     * @param range 数组0=文件起始位置(单位：字节) 数组1=文件终止位置(单位：字节) 数组2=文件长度(单位：字节)
     * @return
     */
    public static long[] parseContentRange(String range) {
        long[] array = new long[3];

        int b = range.indexOf(' ');
        String str = range.substring(b + 1);
        b = str.indexOf('-');
        array[0] = Long.parseLong(str.substring(0, b));

        str = range.substring(b + 1);
        b = str.indexOf('/');
        if (b == 0) {
            array[1] = -1;
        } else {
            array[1] = Long.parseLong(str.substring(0, b));
        }

        array[2] = Long.parseLong(str.substring(b + 1));
        return array;
    }

    /**
     * 解析 content_type 值中的字符集信息
     *
     * @param contentType text/html;charset=GB2312
     *                    application/soap+xml; charset=UTF-8
     * @return
     */
    public static String parseContentTypeCharset(String contentType) {
        if (StringUtils.isBlank(contentType)) {
            return null;
        }

        int begin = StringUtils.indexOf(contentType, "charset", 0, true);
        if (begin == -1) {
            return null;
        }

        begin = contentType.indexOf("=", begin);
        if (begin == -1) {
            return null;
        }

        begin = StringUtils.indexOfNotBlank(contentType, begin + 1, -1);
        if (begin == -1) {
            return null;
        }

        String[] array = StringUtils.split(contentType.substring(begin), ';');
        return StringUtils.trimBlank(array[0]);
    }

    /**
     * 关闭 Socket 连接
     *
     * @param socket
     */
    public static void closeSocketQuietly(Socket socket) {
        if (socket != null) {
            try {
                socket.shutdownInput();
            } catch (IOException e) {
            }

            try {
                socket.shutdownOutput();
            } catch (IOException e) {
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化日期字符串为日期
     *
     * @param val
     * @return
     */
    public static Date format(String val) {
        Date d = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
            d = sf.parse(val);
        } catch (ParseException e) {
            try {
                SimpleDateFormat sf = new SimpleDateFormat("EEE, dd-MMM-yy HH:mm:ss Z", Locale.US);
                d = sf.parse(val);
            } catch (ParseException e1) {
                try {
                    SimpleDateFormat sf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);
                    d = sf.parse(val);
                } catch (ParseException e2) {
                    throw new RuntimeException("Date string format error: " + val);
                }
            }
        }
        return d;
    }

    /**
     * 在 uri 后面添加参数
     *
     * @param uri   定位符, {@literal http://x.x.x.x:xx/uri }
     * @param param 参数, key=value
     * @return
     */
    public static String joinUriParams(String uri, String param) {
        if (uri == null || "#".equals(uri) || StringUtils.isBlank(param)) {
            return uri;
        } else {
            return uri + (uri.lastIndexOf('?') != -1 ? "&" : "?") + param;
        }
    }

    /**
     * 按数组中先后顺序合并多个 uri 字符串
     *
     * @param uris
     * @return
     */
    public static String joinUri(String... uris) {
        if (uris == null) {
            throw new NullPointerException();
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < uris.length; i++) {
            String uri = uris[i]; // .replace('\\', '/');
            if (uri == null) {
                continue;
            }

            char c = 0;
            while (uri.length() > 0 && (c = uri.charAt(0)) != '/' && StringUtils.isSymbol(c)) {
                uri = uri.substring(1);
            }

            if (buf.length() > 0 && StringUtils.inArray(buf.charAt(buf.length() - 1), '/', '\\') && (uri.startsWith("/") || uri.startsWith("\\"))) {
                buf.append(uri.substring(1));
            } else if (buf.length() > 0 && !buf.toString().endsWith("/") && !uri.startsWith("/")) {
                buf.append('/').append(uri);
            } else {
                buf.append(uri);
            }
        }

        return buf.toString();
    }

    /**
     * 下载文件
     *
     * @param response HttpServletResponse对象
     * @param file     下载文件
     * @param filename 下载后显示文件名
     * @throws IOException
     */
    private static void downFile(HttpServletResponse response, File file, String filename) throws IOException {
        response.setContentType("APPLICATION/OCTET-STREAM");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + StringUtils.defaultString(filename, file.getName()) + "\"");
        ServletOutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream(file);
        IO.write(in, out);
    }

    /**
     * 下载文件
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param file     下载文件
     * @param filename 下载后显示文件名
     * @throws IOException
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response, File file, String filename) throws IOException {
        String name = encodeFilename(request, StringUtils.defaultString(filename, file.getName()));
        downFile(response, file, name);
    }

    /**
     * 根据浏览器类型设置文件编码
     *
     * @param request  HttpServletRequest对象
     * @param filename 文件名
     * @return
     * @throws IOException
     */
    public static String encodeFilename(HttpServletRequest request, String filename) throws IOException {
        if (request == null) {
            throw new NullPointerException();
        }
        if (filename == null) {
            throw new NullPointerException();
        }

        String header = request.getHeader("User-Agent");
        if (header == null || header.toLowerCase().contains("msie")) {
            return URLEncoder.encode(filename, CharsetName.UTF_8);
        } else {
            byte[] array = StringUtils.toBytes(StringUtils.removeBlank(filename), StringUtils.CHARSET);
            return StringUtils.toString(array, CharsetName.ISO_8859_1);
        }
    }

}
