package top.techial.knowledge.file.storage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FileStorageServiceImplTest {
    @Autowired
    private FileStorageService fileStorageService;


    @Test
    public void deleteAll() {
//        fileStorageService.deleteAll();
    }

    @Test
    public void delete() {
        fileStorageService.delete("test");
    }

    @Test
    public void store() throws IOException {
        MultipartFile file = new MockMultipartFile("admin.pdf",
            new FileInputStream(Paths.get("/Users/techial/Downloads/admin.pdf").toFile()));
        fileStorageService.store(file);
    }
}