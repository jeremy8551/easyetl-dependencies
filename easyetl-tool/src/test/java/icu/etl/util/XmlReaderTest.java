package icu.etl.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() throws IOException {
        Assert.assertEquals("UTF-8", XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));

        String xml = new String(IO.read(ClassUtils.getResourceAsStream("/cn/china.xml")));
        String encoding = XMLUtils.getXmlHeadEncoding(xml);
        Assert.assertEquals("utf-8".toUpperCase(), StringUtils.toCase(encoding, false, null));
    }

    @Test
    public void testgetXmlHeadEncoding() throws IOException {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        Assert.assertEquals("UTF-8", XMLUtils.getXmlHeadEncoding(xml.getBytes("UTF-8")));
        Assert.assertEquals("UTF-8", XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?><table></table>".getBytes("UTF-8")));
        Assert.assertEquals("UTF-8", XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?><table></table>".getBytes("utf-8")));
        Assert.assertEquals("UTF-8", XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"UTF-8\"?><table></table>".getBytes("ISO-8859-1")));
        Assert.assertNull(XMLUtils.getXmlHeadEncoding("<table></table>".getBytes("ISO-8859-1")));
        Assert.assertNull(XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"\"?><table></table>".getBytes("ISO-8859-1")));
        Assert.assertNull(XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" ?><table></table>".getBytes("ISO-8859-1")));
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        Assert.assertNull(XMLUtils.getXmlHeadEncoding("<?xml version=\"1.0\" encoding=\"\"?><table></table>".getBytes("ISO-8859-1")));

    }

    @Test
    public void testescape() {
        Assert.assertNull(XMLUtils.escape(null));
        Assert.assertEquals("", XMLUtils.escape(""));
        Assert.assertEquals("&lt;&amp;&apos;&quot;&gt;", XMLUtils.escape("<&'\">"));
        Assert.assertEquals("&lt;&amp;&apos;&quot;&gt;&lt;&amp;&apos;&quot;&gt;", XMLUtils.escape("<&'\"><&'\">"));
        Assert.assertEquals("<&'\">", XMLUtils.unescape("&lt;&amp;&apos;&quot;&gt;"));
        Assert.assertEquals("<&'\"><&'\"><&'\">", XMLUtils.unescape("&lt;&amp;&apos;&quot;&gt;&lt;&amp;&apos;&quot;&gt;&lt;&amp;&apos;&quot;&gt;"));

        Assert.assertEquals("a<b&c'd\"e>b", XMLUtils.unescape("a&lt;b&amp;c&apos;d&quot;e&gt;b"));
    }

}
