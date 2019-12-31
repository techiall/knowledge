package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class UserException extends RuntimeException {
    public UserException(Integer id) {
        super(String.format("userId: [%s] not found.", id));
    }
}
