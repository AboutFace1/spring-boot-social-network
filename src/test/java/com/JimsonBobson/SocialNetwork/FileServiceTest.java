package com.JimsonBobson.SocialNetwork;

import com.JimsonBobson.SocialNetwork.service.FileService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.io.File;
import java.lang.reflect.Method;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class FileServiceTest {

    @Autowired
    private FileService fileService;

    @Value("${photo.upload.directory}")
    private String photoUploadDirectory;

    @Test
    public void testGetExtension() throws Exception {
        Method method = FileService.class.getDeclaredMethod("getFileExtension", String.class);
        method.setAccessible(true);

        Assert.assertEquals("should be png", "png", (String)method.invoke(fileService, "test.png"));
        Assert.assertEquals("should be doc", "doc", (String)method.invoke(fileService, "s.doc"));
        Assert.assertEquals("should be jpeg", "jpeg", (String)method.invoke(fileService, "file.jpeg"));
        Assert.assertNull("should be png", (String)method.invoke(fileService, "xyz"));
    }

    @Test
    public void testIsImageExtension() throws Exception {
        Method method = FileService.class.getDeclaredMethod("isImageExtension", String.class);
        method.setAccessible(true);

        Assert.assertTrue("png should be valid", (Boolean)method.invoke(fileService, "png"));
        Assert.assertTrue("PNG should be valid", (Boolean)method.invoke(fileService, "PNG"));
        Assert.assertTrue("jpg should be valid", (Boolean)method.invoke(fileService, "jpg"));
        Assert.assertTrue("jpeg should be valid", (Boolean)method.invoke(fileService, "jpeg"));
        Assert.assertTrue("gif should be valid", (Boolean)method.invoke(fileService, "gif"));
        Assert.assertTrue("GIF should be valid", (Boolean)method.invoke(fileService, "GIF"));
        Assert.assertFalse("doc should be invalid", (Boolean)method.invoke(fileService, "doc"));
        Assert.assertFalse("jpg3 should be invalid", (Boolean)method.invoke(fileService, "jpg3"));
        Assert.assertFalse("gi should be invalid", (Boolean)method.invoke(fileService, "gi"));

    }

    @Test
    public void testCreateDirectory() throws Exception {
        Method method = FileService.class.getDeclaredMethod("makeSubdirectory", String.class, String.class);
        method.setAccessible(true);

        for (int i = 0; i<1000; i++) {
            File created = (File)method.invoke(fileService, photoUploadDirectory, "photo");

            Assert.assertTrue("Directory should exist " + created.getAbsolutePath(), created.exists());
        }
    }
}
