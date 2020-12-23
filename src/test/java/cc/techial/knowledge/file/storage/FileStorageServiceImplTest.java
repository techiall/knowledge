package cc.techial.knowledge.file.storage;

import cc.techial.knowledge.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FileStorageServiceImplTest {
    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void deleteAll() {
        fileStorageService.deleteAll();
    }

    @Test
    public void delete() {
        fileStorageService.delete("test");
    }

    @Test
    public void store() throws IOException {
        MultipartFile file = new MockMultipartFile("admin.pdf",
                                                   new FileInputStream(Paths.get("/Users/techial/Downloads/admin.pdf").toFile()));
        fileStorageService.upload(file);
    }
}
