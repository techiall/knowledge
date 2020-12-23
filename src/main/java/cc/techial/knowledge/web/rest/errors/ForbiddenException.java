package cc.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class ForbiddenException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 403;
    private static final String DEFAULT_MSG = "Forbidden";

    public ForbiddenException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
