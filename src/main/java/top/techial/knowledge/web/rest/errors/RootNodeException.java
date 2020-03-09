package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class RootNodeException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Root Node";

    public RootNodeException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }

    public RootNodeException(Integer integer) {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
