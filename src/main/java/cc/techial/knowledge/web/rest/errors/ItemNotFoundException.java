package cc.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class ItemNotFoundException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Item Not Found";

    public ItemNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
