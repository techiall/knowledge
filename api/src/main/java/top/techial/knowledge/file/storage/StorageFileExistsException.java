package top.techial.knowledge.file.storage;

public class StorageFileExistsException extends RuntimeException {

    private String fileName;

    private String hash;

    public StorageFileExistsException(String msg, String fileName, String hash) {
        super(msg + " " + fileName);
        this.fileName = fileName;
        this.hash = hash;
    }

    public StorageFileExistsException(String msg, Throwable cause, String fileName, String hash) {
        super(msg + " " + fileName, cause);
        this.fileName = fileName;
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHash() {
        return hash;
    }
}
