package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class UsernameIsRegisterException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Username Register";

    public UsernameIsRegisterException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
