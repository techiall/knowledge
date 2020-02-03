package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class UsernameIsRegisterException extends RuntimeException {
    public UsernameIsRegisterException(String username) {
        super(String.format("userId: [%s] register.", username));
    }
}
