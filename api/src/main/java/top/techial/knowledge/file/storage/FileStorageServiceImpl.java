package top.techial.knowledge.file.storage;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.stream.Stream;

/**
 * @author techial
 */
@Service
@Log4j2
public class FileStorageServiceImpl implements FileStorageService {
    private final Path rootLocation;

    public FileStorageServiceImpl(StorageProperties storageProperties) {
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
        log.info("file storage path = {}", rootLocation.toString());
    }

    @Override
    public String store(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file" + originalFilename);
        }

        if (originalFilename != null && originalFilename.contains("..")) {
            // This is a security check
            throw new StorageException("Cannot store file with relative path outside current directory " + originalFilename);
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            if (originalFilename != null && !originalFilename.isEmpty()) {
                digest.update(originalFilename.getBytes());
            }
            Path temp = Files.createTempFile("temp-", null);
            try (
                InputStream in = file.getInputStream();
                OutputStream out = Files.newOutputStream(temp)
            ) {
                byte[] buf = new byte[8192];
                int n;
                while (-1 != (n = in.read(buf, 0, buf.length))) {
                    digest.update(buf, 0, n);
                    out.write(buf, 0, n);
                }
                String sha1 = String.format("%032X", new BigInteger(1, digest.digest()));
                Path dest = rootLocation.resolve(sha1);
                if (dest.toFile().exists()) {
                    Files.delete(temp);
                    throw new StorageFileExistsException("The file already existed ", originalFilename);
                } else {
                    Files.move(temp, dest);
                }
                return sha1;
            } catch (IOException e) {
                throw new StorageException("Failed to store file " + originalFilename, e);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            throw new StorageException("Failed to create temp file ", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Resource loadAsResource(String hash) {
        try {
            Resource resource = new UrlResource(rootLocation.resolve(hash).toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + hash);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + hash, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String hash) {
        FileSystemUtils.deleteRecursively(rootLocation.resolve(hash).toFile());
    }
}

