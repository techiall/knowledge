package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class StorageFileNotFoundException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Storage File Not Found.";

    public StorageFileNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
