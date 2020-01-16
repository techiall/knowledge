package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class ItemNotFoundNodeException extends RuntimeException {
    public ItemNotFoundNodeException(Integer userId, Long nodeId) {
        super(String.format("itemId: [%s], nodeId: [%s]", userId, nodeId));
    }
}
