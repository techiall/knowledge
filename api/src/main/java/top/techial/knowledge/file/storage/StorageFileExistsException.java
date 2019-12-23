package top.techial.knowledge.file.storage;

public class StorageFileExistsException extends RuntimeException {

    public StorageFileExistsException(String msg, String fileName) {
        super(msg + " " + fileName);
    }

}
