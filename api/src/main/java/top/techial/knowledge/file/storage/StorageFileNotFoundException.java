package top.techial.knowledge.file.storage;

public class StorageFileNotFoundException extends RuntimeException {

    public StorageFileNotFoundException(String msg) {
        super(msg);
    }

    public StorageFileNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
