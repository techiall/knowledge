package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class NodeNotFoundException extends RuntimeException {
    public NodeNotFoundException(Long id) {
        super(String.format("nodeId: [%s] not found.", id));
    }

    public NodeNotFoundException() {
        super();
    }

    public NodeNotFoundException(Object object) {
        super(String.format("params: [%s]", object));
    }

}
