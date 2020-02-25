package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class NotFoundException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 404;
    private static final String DEFAULT_MSG = "Not Found";

    public NotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
