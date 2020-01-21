package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class UserNotFoundItemException extends RuntimeException {
    public UserNotFoundItemException(Integer userId, Integer nodeId) {
        super(String.format("userId: [%s], itemId: [%s]", userId, nodeId));
    }
}
