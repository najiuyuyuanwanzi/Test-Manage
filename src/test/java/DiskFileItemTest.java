package org.apache.commons.fileupload.disk;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class DiskFileItemTest {

    private File tempDir;

    @Before
    public void setUp() {
        tempDir = new File(System.getProperty("java.io.tmpdir"));
    }

    // ========== 测试1：写入数据并验证大小 ==========
    @Test
    public void testWriteDataAndVerifySize() throws IOException {
        DiskFileItem item1 = new DiskFileItem("file", "text/plain", false, "test1.txt", 1024, tempDir);
        String content1 = "This is a test";
        try (OutputStream os = item1.getOutputStream()) {
            os.write(content1.getBytes());
        }
        long expectedSize = content1.length();
        long actualSize = item1.getSize();
        assertEquals("文件大小不匹配", expectedSize, actualSize);
    }

    // ========== 测试2：验证 get() 返回内容 ==========
    @Test
    public void testGetData() throws IOException {
        DiskFileItem item1 = new DiskFileItem("file", "text/plain", false, "test1.txt", 1024, tempDir);
        String content1 = "This is a test";
        try (OutputStream os = item1.getOutputStream()) {
            os.write(content1.getBytes());
        }
        byte[] actualContent = item1.get();
        assertArrayEquals("文件内容不匹配", content1.getBytes(), actualContent);
    }

    // ========== 测试3：isInMemory() 缺陷测试 ==========
    @Test
    public void testIsInMemory() throws IOException {
        DiskFileItem item2 = new DiskFileItem("bigfile", "application/octet-stream", false, "big.bin", 10, tempDir);
        byte[] bigData = new byte[100];
        Arrays.fill(bigData, (byte) 1);
        try (OutputStream os = item2.getOutputStream()) {
            os.write(bigData);
        }
        assertFalse("isInMemory() 方法返回结果不正确", item2.isInMemory());
    }

    // ========== 测试4：getName() 方法 ==========
    @Test
    public void testGetName() {
        DiskFileItem item1 = new DiskFileItem("file", "text/plain", false, "test1.txt", 1024, tempDir);
        assertEquals("文件名不匹配", "test1.txt", item1.getName());
    }

    // ========== 测试5：write() 方法验证 ==========
    @Test
    public void testWriteToFile() throws Exception {
        File destinationFile = new File(tempDir, "destination_test.txt");
        DiskFileItem item3 = new DiskFileItem("file", "text/plain", false, "test3.txt", 1024, tempDir);
        String content3 = "Another test file";
        try (OutputStream os = item3.getOutputStream()) {
            os.write(content3.getBytes());
        }

        item3.write(destinationFile);
        assertTrue("目标文件不存在", destinationFile.exists());
    }
}
