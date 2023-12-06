package icu.etl.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class MessySequenceTest {

    @Test
    public void test() throws IOException {
        File dir = FileUtils.getTempDir(MessySequenceTest.class);
        File file = new File(dir, "file.txt");

        System.out.println(file.getAbsolutePath());
        Assert.assertTrue(FileUtils.createFile(file));

        String c = "\uD83D\uDE42"; // UTF-8 不支持的字符🙂

        // 写入文件
        String content = "abcDEFGH123" + c + "d" + c + "f" + c + "qwer~!@#$%^&*()_+hg";
        FileUtils.write(file, "UTF-8", false, content);

        // 读取文件
        MessySequence ms = new MessySequence(content, "UTF-8");
        Assert.assertFalse(ms.canEncode('\uD83D'));
        Assert.assertTrue(ms.canEncode('A'));
        System.out.println(ms.toMessyString());
        System.out.println(ms.getSource());
        System.out.println(ms.highlights('^'));
        System.out.println(ms.replace('■'));
        System.out.println(ms.toString());
        Assert.assertEquals("           ^^ ^^ ^^                   ", ms.highlights('^'));
    }
}
