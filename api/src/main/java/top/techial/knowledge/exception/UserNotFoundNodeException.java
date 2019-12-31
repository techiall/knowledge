package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class UserNotFoundNodeException extends RuntimeException {
    public UserNotFoundNodeException(Integer userId, Long nodeId) {
        super(String.format("userId: [%s], nodeId: [%s]", userId, nodeId));
    }
}
