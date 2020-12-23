package cc.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class UserNotFoundException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "User Not Found";

    public UserNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
