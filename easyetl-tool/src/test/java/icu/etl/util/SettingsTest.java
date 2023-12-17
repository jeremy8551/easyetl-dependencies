package icu.etl.util;

import java.text.DateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import icu.etl.collection.Matrix;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author jeremy8551@qq.com
 * @createtime 2023/9/27
 */
public class SettingsTest {

    @Test
    public void testGetJvmJavaHome() {
        assertTrue(Settings.getJavaHome().isDirectory());
    }

    @Test
    public void testgetGroupID() {
//        Assert.assertEquals(ClassUtils.getPackageName(ClassUtils.class, 2), Settings.getGroupID());
    }

    /**
     * 打印当前操作系统支持的国家，语言和地区信息
     */
    public static void printLocal() {
        Locale list[] = DateFormat.getAvailableLocales();
        Matrix<String> m = new Matrix<String>(list.length, 2);
        for (int i = 0; i < list.length; i++) {
            m.set(i, 0, list[i].getDisplayName());
            m.set(i, 1, list[i].toString());
        }
        m.sortRow(true, new int[]{0}, new StringComparator());
        System.out.println(m.toString(ArrayUtils.asList("语言（国家/地区）", "码值"), ArrayUtils.asList("left", "left"), null));
    }

    /**
     * 打印输出JVM中的环境变量
     */
    public static void printEnvironment() {
        Map<String, String> env = System.getenv();
        Set<String> names = env.keySet();
        Matrix<String> m = new Matrix<String>(names.size(), 2);
        int i = 0;
        for (String key : names) {
            m.set(i, 0, key);
            m.set(i, 1, StringUtils.escapeLineSeparator(env.get(key)));
            i++;
        }
        m.sortRow(true, new int[]{0}, new StringComparator());
        System.out.println(m.toString(ArrayUtils.asList("环境变量名", "变量值"), ArrayUtils.asList("left", "left"), null));
    }

    /**
     * JavaUtils 系统属性
     */
    public static void printProperties() {
        Properties config = System.getProperties();
        Set<String> names = CollectionUtils.stringPropertyNames(config);
        Matrix<String> m = new Matrix<String>(names.size(), 2);
        int i = 0;
        for (String name : names) {
            m.set(i, 0, name);
            m.set(i, 1, StringUtils.escapeLineSeparator(config.getProperty(name)));
            i++;
        }
        m.sortRow(true, new int[]{0}, new StringComparator());
        System.out.println(m.toString(ArrayUtils.asList("属性名", "属性值"), ArrayUtils.asList("left", "left"), null));
    }

    /**
     * Ascii码
     */
    public static void printAscii() {
        Matrix<String> m = new Matrix<String>(256, 5);
        int i = 0;
        m.setRow(i++, "控制字符", "0", "0", "NUL", "空字符");
        m.setRow(i++, "控制字符", "1", "1", "SOH", "标题起始 (Ctrl/A)");
        m.setRow(i++, "控制字符", "2", "2", "STX", "文本起始 (Ctrl/B)");
        m.setRow(i++, "控制字符", "3", "3", "ETX", "文本结束 (Ctrl/C)");
        m.setRow(i++, "控制字符", "4", "4", "EOT", "传输结束 (Ctrl/D)");
        m.setRow(i++, "控制字符", "5", "5", "ENQ", "询问 (Ctrl/E)");
        m.setRow(i++, "控制字符", "6", "6", "ACK", "认可 (Ctrl/F)");
        m.setRow(i++, "控制字符", "7", "7", "BEL", "铃 (Ctrl/G)");
        m.setRow(i++, "控制字符", "8", "8", "BS", "退格 (Ctrl/H)");
        m.setRow(i++, "控制字符", "9", "9", "HT", "水平制表栏 (Ctrl/I)");
        m.setRow(i++, "控制字符", "10", "0A", "LF", "换行 (Ctrl/J)");
        m.setRow(i++, "控制字符", "11", "0B", "VT", "垂直制表栏 (Ctrl/K)");
        m.setRow(i++, "控制字符", "12", "0C", "FF", "换页 (Ctrl/L)");
        m.setRow(i++, "控制字符", "13", "0D", "CR", "回车 (Ctrl/M)");
        m.setRow(i++, "控制字符", "14", "0E", "SO", "移出 (Ctrl/N)");
        m.setRow(i++, "控制字符", "15", "0F", "SI", "移入 (Ctrl/O)");
        m.setRow(i++, "控制字符", "16", "10", "DLE", "数据链接丢失 (Ctrl/P)");
        m.setRow(i++, "控制字符", "17", "11", "DC1", "设备控制 1 (Ctrl/Q)");
        m.setRow(i++, "控制字符", "18", "12", "DC2", "设备控制 2 (Ctrl/R)");
        m.setRow(i++, "控制字符", "19", "13", "DC3", "设备控制 3 (Ctrl/S)");
        m.setRow(i++, "控制字符", "20", "14", "DC4", "设备控制 4 (Ctrl/T)");
        m.setRow(i++, "控制字符", "21", "15", "NAK", "否定接受 (Ctrl/U)");
        m.setRow(i++, "控制字符", "22", "16", "SYN", "同步闲置符 (Ctrl/V)");
        m.setRow(i++, "控制字符", "23", "17", "ETB", "传输块结束 (Ctrl/W)");
        m.setRow(i++, "控制字符", "24", "18", "CAN", "取消 (Ctrl/X)");
        m.setRow(i++, "控制字符", "25", "19", "EM", "媒体结束 (Ctrl/Y)");
        m.setRow(i++, "控制字符", "26", "1A", "SUB", "替换 (Ctrl/Z)");
        m.setRow(i++, "控制字符", "27", "1B", "ESC", "换码符");
        m.setRow(i++, "控制字符", "28", "1C", "FS", "文件分隔符");
        m.setRow(i++, "控制字符", "29", "1D", "GS", "组分隔符");
        m.setRow(i++, "控制字符", "30", "1E", "RS", "记录分隔符");
        m.setRow(i++, "控制字符", "31", "1F", "US", "单位分隔符");
        m.setRow(i++, "特殊和数字字符", "32", "20", "SP", "空格");
        m.setRow(i++, "特殊和数字字符", "33", "21", "!", "感叹号");
        m.setRow(i++, "特殊和数字字符", "34", "22", "\"", "引号 (双引号)");
        m.setRow(i++, "特殊和数字字符", "35", "23", "#", "数字符号");
        m.setRow(i++, "特殊和数字字符", "36", "24", "$", "美元符");
        m.setRow(i++, "特殊和数字字符", "37", "25", "%", "百分号");
        m.setRow(i++, "特殊和数字字符", "38", "26", "&", "和号");
        m.setRow(i++, "特殊和数字字符", "39", "27", "'", "省略号 (单引号)");
        m.setRow(i++, "特殊和数字字符", "40", "28", "(", "左圆括号");
        m.setRow(i++, "特殊和数字字符", "41", "29", ")", "右圆括号");
        m.setRow(i++, "特殊和数字字符", "42", "2A", "*", "星号");
        m.setRow(i++, "特殊和数字字符", "43", "2B", "+", "加号");
        m.setRow(i++, "特殊和数字字符", "44", "2C", ",", "逗号");
        m.setRow(i++, "特殊和数字字符", "45", "2D", "-", "连字号或减号");
        m.setRow(i++, "特殊和数字字符", "46", "2E", ".", "句点或小数点");
        m.setRow(i++, "特殊和数字字符", "47", "2F", "/", "斜杠");
        m.setRow(i++, "特殊和数字字符", "48", "30", "0", "零");
        m.setRow(i++, "特殊和数字字符", "49", "31", "1", "1");
        m.setRow(i++, "特殊和数字字符", "50", "32", "2", "2");
        m.setRow(i++, "特殊和数字字符", "51", "33", "3", "3");
        m.setRow(i++, "特殊和数字字符", "52", "34", "4", "4");
        m.setRow(i++, "特殊和数字字符", "53", "35", "5", "5");
        m.setRow(i++, "特殊和数字字符", "54", "36", "6", "6");
        m.setRow(i++, "特殊和数字字符", "55", "37", "7", "7");
        m.setRow(i++, "特殊和数字字符", "56", "38", "8", "8");
        m.setRow(i++, "特殊和数字字符", "57", "39", "9", "9");
        m.setRow(i++, "特殊和数字字符", "58", "3A", ":", "冒号");
        m.setRow(i++, "特殊和数字字符", "59", "3B", ";", "分号");
        m.setRow(i++, "特殊和数字字符", "60", "3C", "<", "小于");
        m.setRow(i++, "特殊和数字字符", "61", "3D", "=", "等于");
        m.setRow(i++, "特殊和数字字符", "62", "3E", ">", "大于");
        m.setRow(i++, "特殊和数字字符", "63", "3F", "?", "问号");
        m.setRow(i++, "字母字符", "64", "40", "@", "商业 at 符号");
        m.setRow(i++, "字母字符", "65", "41", "A", "大写字母 A");
        m.setRow(i++, "字母字符", "66", "42", "B", "大写字母 B");
        m.setRow(i++, "字母字符", "67", "43", "C", "大写字母 C");
        m.setRow(i++, "字母字符", "68", "44", "D", "大写字母 D");
        m.setRow(i++, "字母字符", "69", "45", "E", "大写字母 E");
        m.setRow(i++, "字母字符", "70", "46", "F", "大写字母 F");
        m.setRow(i++, "字母字符", "71", "47", "G", "大写字母 G");
        m.setRow(i++, "字母字符", "72", "48", "H", "大写字母 H");
        m.setRow(i++, "字母字符", "73", "49", "I", "大写字母 I");
        m.setRow(i++, "字母字符", "74", "4A", "J", "大写字母 J");
        m.setRow(i++, "字母字符", "75", "4B", "K", "大写字母 K");
        m.setRow(i++, "字母字符", "76", "4C", "L", "大写字母 L");
        m.setRow(i++, "字母字符", "77", "4D", "M", "大写字母 M");
        m.setRow(i++, "字母字符", "78", "4E", "N", "大写字母 N");
        m.setRow(i++, "字母字符", "79", "4F", "O", "大写字母 O");
        m.setRow(i++, "字母字符", "80", "50", "P", "大写字母 P");
        m.setRow(i++, "字母字符", "81", "51", "Q", "大写字母 Q");
        m.setRow(i++, "字母字符", "82", "52", "R", "大写字母 R");
        m.setRow(i++, "字母字符", "83", "53", "S", "大写字母 S");
        m.setRow(i++, "字母字符", "84", "54", "T", "大写字母 T");
        m.setRow(i++, "字母字符", "85", "55", "U", "大写字母 U");
        m.setRow(i++, "字母字符", "86", "56", "V", "大写字母 V");
        m.setRow(i++, "字母字符", "87", "57", "W", "大写字母 W");
        m.setRow(i++, "字母字符", "88", "58", "X", "大写字母 X");
        m.setRow(i++, "字母字符", "89", "59", "Y", "大写字母 Y");
        m.setRow(i++, "字母字符", "90", "5A", "Z", "大写字母 Z");
        m.setRow(i++, "字母字符", "91", "5B", "[", "左中括号");
        m.setRow(i++, "字母字符", "92", "5C", "\\", "反斜杠");
        m.setRow(i++, "字母字符", "93", "5D", "]", "右中括号");
        m.setRow(i++, "字母字符", "94", "5E", "^", "音调符号");
        m.setRow(i++, "字母字符", "95", "5F", "_", "下划线");
        m.setRow(i++, "字母字符", "96", "60", "`", "重音符");
        m.setRow(i++, "字母字符", "97", "61", "a", "小写字母 a");
        m.setRow(i++, "字母字符", "98", "62", "b", "小写字母 b");
        m.setRow(i++, "字母字符", "99", "63", "c", "小写字母 c");
        m.setRow(i++, "字母字符", "100", "64", "d", "小写字母 d");
        m.setRow(i++, "字母字符", "101", "65", "e", "小写字母 e");
        m.setRow(i++, "字母字符", "102", "66", "f", "小写字母 f");
        m.setRow(i++, "字母字符", "103", "67", "g", "小写字母 g");
        m.setRow(i++, "字母字符", "104", "68", "h", "小写字母 h");
        m.setRow(i++, "字母字符", "105", "69", "i", "小写字母 i");
        m.setRow(i++, "字母字符", "106", "6A", "j", "小写字母 j");
        m.setRow(i++, "字母字符", "107", "6B", "k", "小写字母 k");
        m.setRow(i++, "字母字符", "108", "6C", "l", "小写字母 l");
        m.setRow(i++, "字母字符", "109", "6D", "m", "小写字母 m");
        m.setRow(i++, "字母字符", "110", "6E", "n", "小写字母 n");
        m.setRow(i++, "字母字符", "111", "6F", "o", "小写字母 o");
        m.setRow(i++, "字母字符", "112", "70", "p", "小写字母 p");
        m.setRow(i++, "字母字符", "113", "71", "q", "小写字母 q");
        m.setRow(i++, "字母字符", "114", "72", "r", "小写字母 r");
        m.setRow(i++, "字母字符", "115", "73", "s", "小写字母 s");
        m.setRow(i++, "字母字符", "116", "74", "t", "小写字母 t");
        m.setRow(i++, "字母字符", "117", "75", "u", "小写字母 u");
        m.setRow(i++, "字母字符", "118", "76", "v", "小写字母 v");
        m.setRow(i++, "字母字符", "119", "77", "w", "小写字母 w");
        m.setRow(i++, "字母字符", "120", "78", "x", "小写字母 x");
        m.setRow(i++, "字母字符", "121", "79", "y", "小写字母 y");
        m.setRow(i++, "字母字符", "122", "7A", "z", "小写字母 z");
        m.setRow(i++, "字母字符", "123", "7B", "{", "左大括号");
        m.setRow(i++, "字母字符", "124", "7C", "|", "垂直线");
        m.setRow(i++, "字母字符", "125", "7D", "}", "右大括号 (ALTMODE)");
        m.setRow(i++, "字母字符", "126", "7E", "~", "代字号 (ALTMODE)");
        m.setRow(i++, "字母字符", "127", "7F", "DEL", "擦掉 (DELETE)");
        m.setRow(i++, "控制字符", "128", "80", "　", "[保留]");
        m.setRow(i++, "控制字符", "129", "81", "　", "[保留]");
        m.setRow(i++, "控制字符", "130", "82", "　", "[保留]");
        m.setRow(i++, "控制字符", "131", "83", "　", "[保留]");
        m.setRow(i++, "控制字符", "132", "84", "IND", "索引");
        m.setRow(i++, "控制字符", "133", "85", "NEL", "下一行");
        m.setRow(i++, "控制字符", "134", "86", "SSA", "被选区域起始");
        m.setRow(i++, "控制字符", "135", "87", "ESA", "被选区域结束");
        m.setRow(i++, "控制字符", "136", "88", "HTS", "水平制表符集");
        m.setRow(i++, "控制字符", "137", "89", "HTJ", "对齐的水平制表符集");
        m.setRow(i++, "控制字符", "138", "8A", "VTS", "垂直制表符集");
        m.setRow(i++, "控制字符", "139", "8B", "PLD", "部分行向下");
        m.setRow(i++, "控制字符", "140", "8C", "PLU", "部分行向上");
        m.setRow(i++, "控制字符", "141", "8D", "RI", "反向索引");
        m.setRow(i++, "控制字符", "142", "8E", "SS2", "单移 2");
        m.setRow(i++, "控制字符", "143", "8F", "SS3", "单移 3");
        m.setRow(i++, "控制字符", "144", "90", "DCS", "设备控制字符串");
        m.setRow(i++, "控制字符", "145", "91", "PU1", "专用 1");
        m.setRow(i++, "控制字符", "146", "92", "PU2", "专用 2");
        m.setRow(i++, "控制字符", "147", "93", "STS", "设置传输状态");
        m.setRow(i++, "控制字符", "148", "94", "CCH", "取消字符");
        m.setRow(i++, "控制字符", "149", "95", "MW", "消息等待");
        m.setRow(i++, "控制字符", "150", "96", "SPA", "保护区起始");
        m.setRow(i++, "控制字符", "151", "97", "EPA", "保护区结束");
        m.setRow(i++, "控制字符", "152", "98", " ", "[保留]");
        m.setRow(i++, "控制字符", "153", "99", " ", "[保留]");
        m.setRow(i++, "控制字符", "154", "9A", " ", "[保留]");
        m.setRow(i++, "控制字符", "155", "9B", "CSI", "控制序列引导符");
        m.setRow(i++, "控制字符", "156", "9C", "ST", "字符串终止符");
        m.setRow(i++, "控制字符", "157", "9D", "OSC", "操作系统命令");
        m.setRow(i++, "控制字符", "158", "9E", "PM", "秘密消息");
        m.setRow(i++, "控制字符", "159", "9F", "APC", "应用程序");
        m.setRow(i++, "控制字符", "160", "A0", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "161", "A1", "?", "反向感叹号");
        m.setRow(i++, "控制字符", "162", "A2", "￠", "分币符");
        m.setRow(i++, "控制字符", "163", "A3", "￡", "英磅符");
        m.setRow(i++, "控制字符", "164", "A4", "　", "[保留] 2");
        m.setRow(i++, "控制字符", "165", "A5", "￥", "人民币符");
        m.setRow(i++, "控制字符", "166", "A6", "　", "[保留] 2");
        m.setRow(i++, "控制字符", "167", "A7", "§", "章节符");
        m.setRow(i++, "控制字符", "168", "A8", "¤", "通用货币符号 2");
        m.setRow(i++, "控制字符", "169", "A9", "?", "版权符号");
        m.setRow(i++, "控制字符", "170", "AA", "a", "阴性顺序指示符");
        m.setRow(i++, "控制字符", "171", "AB", "?", "左角引号");
        m.setRow(i++, "控制字符", "172", "AC", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "173", "AD", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "174", "AE", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "175", "AF", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "176", "B0", "°", "温度符");
        m.setRow(i++, "控制字符", "177", "B1", "±", "加/减号");
        m.setRow(i++, "控制字符", "178", "B2", "2", "上标 2");
        m.setRow(i++, "控制字符", "179", "B3", "3", "上标 3");
        m.setRow(i++, "控制字符", "180", "B4", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "181", "B5", "μ", "微符");
        m.setRow(i++, "控制字符", "182", "B6", "?", "段落符，pilcrow");
        m.setRow(i++, "控制字符", "183", "B7", "·", "中点");
        m.setRow(i++, "控制字符", "184", "B8", "　", "[保留] 2");
        m.setRow(i++, "控制字符", "185", "B9", "1", "上标 1");
        m.setRow(i++, "控制字符", "186", "BA", "o", "阳性顺序指示符");
        m.setRow(i++, "控制字符", "187", "BB", "?", "右角引号");
        m.setRow(i++, "控制字符", "188", "BC", "?", "分数四分之一");
        m.setRow(i++, "控制字符", "189", "BD", "?", "分数二分之一");
        m.setRow(i++, "控制字符", "190", "BE", "　", "[保留] 2");
        m.setRow(i++, "控制字符", "191", "BF", "?", "反向问号");
        m.setRow(i++, "控制字符", "192", "C0", "à", "带重音符的大写字母 A");
        m.setRow(i++, "控制字符", "193", "C1", "á", "带尖锐重音的大写字母 A");
        m.setRow(i++, "控制字符", "194", "C2", "?", "带音调符号的大写字母 A");
        m.setRow(i++, "控制字符", "195", "C3", "?", "带代字号的大写字母 A");
        m.setRow(i++, "控制字符", "196", "C4", "?", "带元音变音 (分音符号) 的大写字母 A");
        m.setRow(i++, "控制字符", "197", "C5", "?", "带铃声的大写字母 A ");
        m.setRow(i++, "控制字符", "198", "C6", "?", "大写字母 AE 双重元音");
        m.setRow(i++, "控制字符", "199", "C7", "?", "带变音符号的大写字母 C");
        m.setRow(i++, "控制字符", "200", "C8", "è", "带重音符的大写字母 E ");
        m.setRow(i++, "控制字符", "201", "C9", "é", "带尖锐重音的大写字母 E ");
        m.setRow(i++, "控制字符", "202", "CA", "ê", "带音调符号的大写字母 E ");
        m.setRow(i++, "控制字符", "203", "CB", "?", "带元音变音 (分音符号) 的大写字母 E");
        m.setRow(i++, "控制字符", "204", "CC", "ì", "带重音符的大写字母 I ");
        m.setRow(i++, "控制字符", "205", "CD", "í", "带尖锐重音的大写字母 I ");
        m.setRow(i++, "控制字符", "206", "CE", "?", "带音调符号的大写字母 I ");
        m.setRow(i++, "控制字符", "207", "CF", "?", "带元音变音 (分音符号) 的大写字母 I");
        m.setRow(i++, "控制字符", "208", "D0", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "209", "D1", "?", "带代字号的大写字母 N ");
        m.setRow(i++, "控制字符", "210", "D2", "ò", "带重音符的大写字母 O  ");
        m.setRow(i++, "控制字符", "211", "D3", "ó", "带尖锐重音的大写字母 O ");
        m.setRow(i++, "控制字符", "212", "D4", "?", "带音调符号的大写字母 O  ");
        m.setRow(i++, "控制字符", "213", "D5", "?", "带代字号的大写字母 O  ");
        m.setRow(i++, "控制字符", "214", "D6", "?", "带元音变音 (分音符号) 的大写字母 O");
        m.setRow(i++, "控制字符", "215", "D7", "OE", "大写字母 OE 连字 2");
        m.setRow(i++, "控制字符", "216", "D8", "?", "带斜杠的大写字母 O ");
        m.setRow(i++, "控制字符", "217", "D9", "ù", "带重音符的大写字母 U ");
        m.setRow(i++, "控制字符", "218", "DA", "ú", "带尖锐重音的大写字母 U ");
        m.setRow(i++, "控制字符", "219", "DB", "?", "带音调符号的大写字母 U ");
        m.setRow(i++, "控制字符", "220", "DC", "ü", "带元音变音 (分音符号) 的大写字母 U");
        m.setRow(i++, "控制字符", "221", "DD", "Y", "带元音变音 (分音符号) 的大写字母 Y");
        m.setRow(i++, "控制字符", "222", "DE", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "223", "DF", "?", "德语高调小写字母 s");
        m.setRow(i++, "控制字符", "224", "E0", "à", "带重音符的小写字母 a ");
        m.setRow(i++, "控制字符", "225", "E1", "á", "带尖锐重音的小写字母 a ");
        m.setRow(i++, "控制字符", "226", "E2", "a", "带音调符号的小写字母 a ");
        m.setRow(i++, "控制字符", "227", "E3", "?", "带代字号的小写字母 a ");
        m.setRow(i++, "控制字符", "228", "E4", "?", "带元音变音 (分音符号) 的小写字母 a");
        m.setRow(i++, "控制字符", "229", "E5", "?", "带铃声的小写字母 a ");
        m.setRow(i++, "控制字符", "230", "E6", "?", "小写字母 ae 双重元音");
        m.setRow(i++, "控制字符", "231", "E7", "?", "带变音符号的小写字母 c");
        m.setRow(i++, "控制字符", "232", "E8", "è", "带重音符的小写字母 e ");
        m.setRow(i++, "控制字符", "233", "E9", "é", "带尖锐重音的小写字母 e ");
        m.setRow(i++, "控制字符", "234", "EA", "ê", "带音调符号的小写字母 e ");
        m.setRow(i++, "控制字符", "235", "EB", "?", "带元音变音 (分音符号) 的小写字母 e");
        m.setRow(i++, "控制字符", "236", "EC", "ì", "带重音符的小写字母 i ");
        m.setRow(i++, "控制字符", "237", "ED", "í", "带尖锐重音的小写字母 i ");
        m.setRow(i++, "控制字符", "238", "EE", "?", "带音调符号的小写字母 i ");
        m.setRow(i++, "控制字符", "239", "EF", "?", "带元音变音 (分音符号) 的小写字母 i");
        m.setRow(i++, "控制字符", "240", "F0", " ", "[保留] 2");
        m.setRow(i++, "控制字符", "241", "F1", "?", "带代字号的小写字母 n ");
        m.setRow(i++, "控制字符", "242", "F2", "ò", "带重音符的小写字母 o ");
        m.setRow(i++, "控制字符", "243", "F3", "ó", "带尖锐重音的小写字母 o ");
        m.setRow(i++, "控制字符", "244", "F4", "?", "带音调符号的小写字母 o ");
        m.setRow(i++, "控制字符", "245", "F5", "?", "带代字号的小写字母 o ");
        m.setRow(i++, "控制字符", "246", "F6", "?", "带元音变音 (分音符号) 的小写字母 o");
        m.setRow(i++, "控制字符", "247", "F7", "oe", "小写字母 oe 连字 2");
        m.setRow(i++, "控制字符", "248", "F8", "?", "带斜杠的小写字母 o ");
        m.setRow(i++, "控制字符", "249", "F9", "ù", "带重音符的小写字母 u ");
        m.setRow(i++, "控制字符", "250", "FA", "ú", "带尖锐重音的小写字母 u ");
        m.setRow(i++, "控制字符", "251", "FB", "?", "带音调符号的小写字母 u ");
        m.setRow(i++, "控制字符", "252", "FC", "ü", "带元音变音 (分音符号) 的小写字母 u");
        m.setRow(i++, "控制字符", "253", "FD", "?", "带元音变音 (分音符号) 的小写字母 y 2");
        m.setRow(i++, "控制字符", "254", "FE", "　", "[保留] 2");
        m.setRow(i++, "控制字符", "255", "FF", "　", "[保留] 2	");

        System.out.println(m.toString(ArrayUtils.asList("字符类型", "十进制", "十六进制", "MCS字符或缩写", "字符名"), ArrayUtils.asList("left", "left", "left", "left", "left"), null));
    }

    /**
     * 打印虚拟机基础参数与环境变量
     */
    @Test
    public void test() {
        printLocal();
        printEnvironment();
        printProperties();
        printAscii();
    }

}
