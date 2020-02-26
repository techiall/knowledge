package top.techial.knowledge.web.rest.errors;

/**
 * @author techial
 */
public class NodeNotFoundException extends ClientErrorException {
    private static final Integer DEFAULT_CODE = 400;
    private static final String DEFAULT_MSG = "Node Not Found";

    public NodeNotFoundException() {
        super(DEFAULT_CODE, DEFAULT_MSG);
    }
}
