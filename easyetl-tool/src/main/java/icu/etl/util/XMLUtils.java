package icu.etl.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML工具
 *
 * @author jeremy8551@qq.com
 * @createtime 2017-02-04
 */
public class XMLUtils {

    public XMLUtils() {
    }

    /**
     * 返回 xml 配置文件对象
     *
     * @param in xml信息
     * @return
     */
    public static Document getXmlDocument(InputStream in) {
        try {
            DocumentBuilderFactory xml = DocumentBuilderFactory.newInstance();
            xml.setIgnoringComments(true);
            xml.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = xml.newDocumentBuilder();
            Document doc = documentBuilder.parse(in);
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(1), e);
        }
    }

    /**
     * 返回 xml 配置文件根节点
     *
     * @param uri 资源文件定位符，如: /xxx.xml
     * @return 根元素
     */
    public static Element getRoot(String uri) {
        return getRoot(XMLUtils.class.getResourceAsStream(uri));
    }

    /**
     * 返回 XML 配置文件根节点
     *
     * @param file XML配置文件
     * @return
     */
    public static Element getRoot(File file) {
        try {
            DocumentBuilderFactory xml = DocumentBuilderFactory.newInstance();
            xml.setIgnoringComments(true);
            xml.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = xml.newDocumentBuilder();
            Document doc = builder.parse(file);
            Element root = doc.getDocumentElement();
            return root;
        } catch (IOException e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(2, e.toString()));
        } catch (Exception ex) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(3, ex.toString()));
        }
    }

    /**
     * 返回 xml 配置文件根节点
     *
     * @param in xml输入流
     * @return
     */
    public static Element getRoot(InputStream in) {
        try {
            DocumentBuilderFactory xml = DocumentBuilderFactory.newInstance();
            xml.setIgnoringComments(true);
            xml.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = xml.newDocumentBuilder();
            Document doc = builder.parse(in);
            Element root = doc.getDocumentElement();
            return root;
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(1), e);
        }
    }

    /**
     * 查找节点中的属性值，如果找不到用默认值替代
     *
     * @param node       节点
     * @param name       属性名
     * @param defaultVal 默认值
     * @return 属性值
     */
    public static String getAttribute(Node node, String name, String defaultVal) {
        try {
            String value = getAttribute(node, name);
            return StringUtils.isBlank(value) ? defaultVal : value;
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 查找节点中的属性值
     *
     * @param node 节点
     * @param name 属性名
     * @return 属性值
     */
    public static String getAttribute(Node node, String name) {
        try {
            return StringUtils.trimBlank(node.getAttributes().getNamedItem(name).getNodeValue());
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(4, node.getNodeName(), name), e);
        }
    }

    /**
     * 查询节点的所有属性
     *
     * @param node 节点
     * @return 属性值
     */
    public static Map<String, String> getAttributes(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        int length = attributes.getLength();
        HashMap<String, String> map = new HashMap<String, String>(length);
        for (int i = 0; i < length; i++) {
            Node item = attributes.item(i);
            String name = StringUtils.trim(item.getNodeName());
            String value = item.getNodeValue();
            map.put(name, value);
        }
        return map;
    }

    /**
     * 获取属性值并转换成整数
     *
     * @param node 节点
     * @param name 属性名
     * @return
     */
    public static int getAttributeAsInt(Node node, String name) {
        String value = getAttribute(node, name);
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(5, node.getNodeName(), name, value));
        }
    }

    /**
     * 获取属性值并转换成整数
     *
     * @param node       节点
     * @param name       属性名
     * @param defaultVal 默认值
     * @return
     */
    public static int getAttributeAsInt(Node node, String name, int defaultVal) {
        String value = getAttribute(node, name, null);
        if (StringUtils.isBlank(value)) {
            return defaultVal;
        }

        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new RuntimeException(ResourcesUtils.getXmlMessage(5, node.getNodeName(), name, value));
        }
    }

    /**
     * 获取属性值并转换成 boolean
     *
     * @param node       节点
     * @param name       属性名
     * @param defaultVal 默认值
     * @return
     */
    public static boolean getAttributeAsBoolean(Node node, String name, String defaultVal) {
        String value = getAttribute(node, name, null);
        if (StringUtils.isNotBlank(value)) {
            return Boolean.parseBoolean(StringUtils.removeBlank(value));
        } else {
            return Boolean.parseBoolean(StringUtils.removeBlank(defaultVal));
        }
    }

    /**
     * 判断属性值是否为空
     *
     * @param node 节点
     * @param name 属性名
     * @return true-为空
     */
    public static boolean isAttributeBlank(Node node, String name) {
        Node item = node.getAttributes().getNamedItem(name);
        if (item == null) {
            return true;
        } else {
            try {
                return StringUtils.isBlank(item.getNodeValue());
            } catch (Exception e) {
                throw new RuntimeException(ResourcesUtils.getXmlMessage(5, node.getNodeName(), name), e);
            }
        }
    }

    /**
     * 计算在[节点node]下有多少个子节点名字=[nodeName]
     *
     * @param node     节点
     * @param nodeName 子节点名
     * @return
     */
    public static int getNodeNumber(Node node, String nodeName) {
        int count = 0;
        NodeList nodeList = node.getChildNodes();
        int length = nodeList.getLength();
        for (int i = 0; i < length; i++) {
            Node item = nodeList.item(i);
            if (nodeName.equalsIgnoreCase(item.getNodeName())) {
                count++;
            }
        }
        return count;
    }

    /**
     * 转义xml中特殊字符
     * {@literal 1010 < 1020 == 1010 \&lt; 1020 }
     * {@literal 1010 > 1020 == 1010 \&gt; 1020 }
     * {@literal "string" == &quot;string&quot; }
     *
     * @param str 字符串
     * @return
     */
    public static String escape(String str) {
        if (str == null) {
            return null;
        } else {
            StringBuilder buf = new StringBuilder(str.length() + 10);
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                switch (c) {
                    case '&':
                        buf.append("&amp;");
                        break;

                    case '<':
                        buf.append("&lt;");
                        break;

                    case '>':
                        buf.append("&gt;");
                        break;

                    case '\"':
                        buf.append("&quot;");
                        break;

                    case '\'':
                        buf.append("&apos;");
                        break;

                    default:
                        buf.append(c);
                        break;
                }
            }
            return str.length() == buf.length() ? str : buf.toString();
        }
    }

    /**
     * 反转义xml中特殊字符
     * {@literal &nb == &amp;nb }
     * {@literal 1010 &lt; 1020 == 1010 < 1020 }
     * {@literal 1010 &gt; 1020 == 1010 > 1020 }
     * {@literal &quot;string&quot; == "string" }
     *
     * @param str 字符串
     * @return
     */
    public static String unescape(String str) {
        if (str == null) {
            return null;
        } else {
            StringBuilder buf = new StringBuilder(str.length());
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '&') {
                    int next = i + 1;
                    if (next < str.length()) {

                        // 转义 &amp;
                        if (str.startsWith("amp;", next)) {
                            buf.append('&');
                            i += "amp;".length();
                            continue;
                        }

                        // 转义 &lt;
                        if (str.startsWith("lt;", next)) {
                            buf.append('<');
                            i += "lt;".length();
                            continue;
                        }

                        // 转义 &gt;
                        if (str.startsWith("gt;", next)) {
                            buf.append('>');
                            i += "gt;".length();
                            continue;
                        }

                        // 转义 &apos;
                        if (str.startsWith("apos;", next)) {
                            buf.append('\'');
                            i += "apos;".length();
                            continue;
                        }

                        // 转义 &quot;
                        if (str.startsWith("quot;", next)) {
                            buf.append('"');
                            i += "quot;".length();
                            continue;
                        }
                    }

                    buf.append(c);
                } else {
                    buf.append(c);
                }
            }
            return str.length() == buf.length() ? str : buf.toString();
        }
    }

    /**
     * 从xml信息中读取头信息, 如: {@literal <?xml version=\"1.0\" encoding=\"UTF-8\"?> }
     *
     * @param xml xml信息
     * @return null表示不存在xml报文头
     */
    public static String getXmlHeadEncoding(String xml) {
        if (xml == null) {
            return null;
        }

        int begin = StringUtils.indexOf(xml, "<?xml", 0, true);
        if (begin == -1) {
            return null;
        }

        int end = StringUtils.indexOf(xml, "?>", begin + 4, true);
        if (end == -1) {
            String errMsg = ResourcesUtils.getXmlMessage(9, "?>");
            throw new RuntimeException(ResourcesUtils.getXmlMessage(13, errMsg, xml));
        }

        String content = StringUtils.trimBlank(xml.substring(begin + 5, end)); // 截取xml头信息
        List<Property> list = new ArrayList<Property>();
        XMLUtils.splitXmlProperties(content, list);
        for (Property node : list) {
            if ("encoding".equalsIgnoreCase(node.getKey())) {
                return StringUtils.trimBlank((Object) node.getValue());
            }
        }
        return null;
    }

    /**
     * 从xml信息中读取头信息, 如: {@literal <?array version="1.0" encoding="UTF-8"?> }
     *
     * @param array xml的字节数组
     * @return null表示不存在xml报文头
     */
    public static String getXmlHeadEncoding(byte[] array) {
        try {
            Document doc = XMLUtils.getXmlDocument(new ByteArrayInputStream(array));
            String encoding = doc.getXmlEncoding();
            if (StringUtils.isNotBlank(encoding)) {
                return encoding;
            }
        } catch (Exception e) { // 忽略解析错误
        }

        try {
            String charsetName = XMLUtils.getXmlHeadEncoding(new String(array, CharsetName.UTF_8));
            if (StringUtils.isNotBlank(charsetName)) {
                return charsetName;
            }
        } catch (Exception e) {
        }

        try {
            String charsetName = XMLUtils.getXmlHeadEncoding(new String(array, CharsetName.GBK));
            if (StringUtils.isNotBlank(charsetName)) {
                return charsetName;
            }
        } catch (Exception e) {
        }

        try {
            String charsetName = XMLUtils.getXmlHeadEncoding(new String(array, CharsetName.ISO_8859_1));
            if (StringUtils.isNotBlank(charsetName)) {
                return charsetName;
            }
        } catch (Exception e) {
        }

        try {
            String charsetName = XMLUtils.getXmlHeadEncoding(new String(array, StringUtils.CHARSET));
            if (StringUtils.isNotBlank(charsetName)) {
                return charsetName;
            }
        } catch (Exception e) {
        }

        try {
            String charsetName = XMLUtils.getXmlHeadEncoding(new String(array));
            if (StringUtils.isNotBlank(charsetName)) {
                return charsetName;
            }
        } catch (Exception e) {
        }

        return null;
    }

    /**
     * 分隔XML中的属性信息, 保存属性名与属性值到集合list
     *
     * @param xml XML属性信息, 如: version =\"1.0\" encoding = \"UTF-8\" name=\"名字\" v1 = 1 v2= v3=3 v4= 4"
     */
    public static List<Property> splitXmlProperties(String xml) {
        List<Property> list = new ArrayList<Property>();
        XMLUtils.splitXmlProperties(xml, list);
        return list;
    }

    /**
     * 分隔XML中的属性信息, 保存属性名与属性值到集合list
     *
     * @param xml  XML属性信息, 如: version =\"1.0\" encoding = \"UTF-8\" name=\"名字\" v1 = 1 v2= v3=3 v4= 4"
     * @param list XML属性集合，用于存储解析后的所有字段
     */
    public static void splitXmlProperties(String xml, Collection<Property> list) {
        if (xml == null) {
            return;
        }

        String str = StringUtils.trimBlank(xml);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // 属性值开始的位置
            if (c == '=') {
                int from = StringUtils.indexOfNotBlank(str, i + 1, -1);
                if (from == -1) {
                    break;
                }

                if (StringUtils.inArray(str.charAt(from), '\'', '\"')) { // 引号属性值开始的位置
                    int end = XMLUtils.indexXmlPropertyValueEndPos(str, from);
                    if (end == -1) {
                        String errMsg = ResourcesUtils.getXmlMessage(10, c);
                        throw new RuntimeException(ResourcesUtils.getXmlMessage(13, errMsg, str));
                    }

                    String val = StringUtils.unquotation(StringUtils.trimBlank(str.substring(i + 1, end + 1)));
                    setXmlLastPropertyValue(str, list, val, false);

                    i = end;
                    continue;
                } else { // 无引号属性值开始位置
                    int end = XMLUtils.getXmlPropertyValue(str, from);
                    if (end == -1) { // 无属性值时进入下一个属性判断
                        i = from - 1;
                        continue;
                    } else {
                        String val = str.substring(i + 1, end + 1); // 截取属性值
                        setXmlLastPropertyValue(str, list, val, false); // 设置xml属性的属性值
                        i = end;
                        continue;
                    }
                }
            }

            // 忽略空白字符
            else if (Character.isWhitespace(c)) {
                continue;
            }

            // 属性值开始
            else if (c == '\'' || c == '\"') {
                int end = XMLUtils.indexXmlPropertyValueEndPos(str, i);
                if (end == -1) {
                    String errMsg = ResourcesUtils.getXmlMessage(10, c);
                    throw new RuntimeException(ResourcesUtils.getXmlMessage(13, errMsg, str));
                }

                String val = StringUtils.unquotation(StringUtils.trimBlank(str.substring(i, end + 1)));
                setXmlLastPropertyValue(str, list, val, true);

                i = end;
                continue;
            }

            // 属性名开始位置
            else {
                int end = XMLUtils.indexXmlPropertyNameEndPos(str, i + 1); // 搜索XML属性名结尾字符的位置
                String name = str.substring(i, end + 1); // 属性名
                list.add(new Property(name, null)); // 增加属性

                i = end;
                continue;
            }
        }
    }

    /**
     * remove &lt;!DOCTYPE .. &gt; from xml content
     *
     * @param xml
     * @return
     */
    public static String removeXmlDoctype(String xml) {
        if (xml == null) {
            return xml;
        }

        String head = "<!DOCTYPE";
        int begin = xml.indexOf(head); // index of String start position
        if (begin == -1) {
            return xml;
        }

        int index = xml.indexOf(">", begin + head.length());
        if (index < 0) {
            throw new RuntimeException(xml);
        } else {
            String newXml = StringUtils.remove(xml, begin, index + 1);
            return XMLUtils.removeXmlDoctype(newXml);
        }
    }

    /**
     * 搜索属性名结束位置
     *
     * @param str  xml内容
     * @param from 属性名开始的位置
     * @return 不会有-1的返回值
     */
    private static int indexXmlPropertyNameEndPos(String str, int from) {
        for (int i = from; i < str.length(); i++) {
            char c = str.charAt(i);
            // 空白字符或等号或引号的前一个字符是属性名最后一个字符
            if (Character.isWhitespace(c) || c == '=' || c == '\'' || c == '\"') {
                return i - 1;
            }
        }
        return str.length() - 1;
    }

    /**
     * 搜索属性值结尾字符所在位置
     *
     * @param str  xml内容
     * @param from 属性值开始位置, 单引号或双引号的起始位置
     * @return
     */
    private static int indexXmlPropertyValueEndPos(String str, int from) {
        char bc = str.charAt(from); // 单引号或双引号
        for (int i = from + 1; i < str.length(); i++) {
            if (str.charAt(i) == bc) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将属性值val 保存到最后一个属性中
     *
     * @param str          xml内容
     * @param list         属性集合
     * @param val          属性值
     * @param ignoreNoName true表示属性值无对应属性名时忽略不会抛出异常, 否则抛出异常
     */
    protected static void setXmlLastPropertyValue(String str, Collection<Property> list, String val, boolean ignoreNoName) {
        if (list.isEmpty()) {
            if (ignoreNoName) {
                return;
            } else {
                String errMsg = ResourcesUtils.getXmlMessage(11, val);
                throw new RuntimeException(ResourcesUtils.getXmlMessage(13, errMsg, str));
            }
        }

        Property property = CollectionUtils.lastElement(list);
        if (property.getValue() != null) {
            if (ignoreNoName) {
                return;
            } else {
                String errMsg = ResourcesUtils.getXmlMessage(11, val);
                throw new RuntimeException(ResourcesUtils.getXmlMessage(13, errMsg, str));
            }
        }
        property.setValue(val); // 设置属性
    }

    /**
     * 从指定位置开始搜索xml查找属性值结束位置
     * v1=1 v2 = v3 = 3" v4 = 4' v5=
     *
     * @param str  xml内容
     * @param from 属性值开始位置
     * @return -1表示无属性值 大于0表示属性值结束位置
     */
    private static int getXmlPropertyValue(String str, int from) {
        for (int i = from; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isWhitespace(c)) {
                int nextStr = StringUtils.indexOfNotBlank(str, i, -1); // 搜索属性值结尾位置的字符
                return nextStr != -1 && str.charAt(nextStr) == '=' ? -1 : i - 1; // 如果下一个字符串以等号开始则属性值不存在
            } else if (c == '\"' || c == '\'') {
                return i;
            } else if (c == '=') {
                return -1;
            }
        }
        return from;
    }

}
