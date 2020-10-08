package top.techial.knowledge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import top.techial.knowledge.config.StorageProperties;
import top.techial.knowledge.web.rest.errors.StorageFileNotFoundException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author techial
 */
@Service
public class FileStorageService {
    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);
    private final Path rootLocation;

    public FileStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage());
            }
        }
        log.info("file storage path = {}", rootLocation);
    }

    public String upload(MultipartFile file) {
        var originalFilename = file.getOriginalFilename();
        if (file.isEmpty()) {
            throw new StorageFileNotFoundException();
        }

        if (originalFilename != null && originalFilename.contains("..")) {
            // This is a security check
            throw new StorageFileNotFoundException();
        }

        try {
            var digest = MessageDigest.getInstance("SHA-1");
            if (originalFilename != null && !originalFilename.isEmpty()) {
                digest.update(originalFilename.getBytes());
            }
            var temp = Files.createTempFile("temp-", null);
            try (InputStream in = file.getInputStream();
                 OutputStream out = Files.newOutputStream(temp)) {
                byte[] buf = new byte[8192];
                int n;
                while (-1 != (n = in.read(buf, 0, buf.length))) {
                    digest.update(buf, 0, n);
                    out.write(buf, 0, n);
                }
                var sha1 = String.format("%032X", new BigInteger(1, digest.digest()));
                var dest = rootLocation.resolve(sha1);
                if (dest.toFile().exists()) {
                    return sha1;
                }
                Files.move(temp, dest);
                return sha1;
            } catch (IOException e) {
                throw new StorageFileNotFoundException();
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new StorageFileNotFoundException();
        }
    }

    public Resource findByHash(String hash) {
        try {
            Resource resource = new UrlResource(rootLocation.resolve(hash).toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException();
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException();
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void delete(String hash) {
        FileSystemUtils.deleteRecursively(rootLocation.resolve(hash).toFile());
    }
}

