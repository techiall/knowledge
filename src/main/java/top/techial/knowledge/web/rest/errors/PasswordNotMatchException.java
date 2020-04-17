package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class PasswordNotMatchException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Password not match";

    public PasswordNotMatchException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
