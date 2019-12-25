package top.techial.knowledge.exception;

public class StorageFileExistsException extends RuntimeException {

    public StorageFileExistsException(String msg, String fileName) {
        super(msg + " " + fileName);
    }

}
