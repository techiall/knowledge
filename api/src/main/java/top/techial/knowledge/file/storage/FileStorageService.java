package top.techial.knowledge.file.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author techial
 */
public interface FileStorageService {
    String store(MultipartFile file);

    Stream<Path> loadAll();

    Resource loadAsResource(String hash);

    void deleteAll();

    void delete(String hash);
}
