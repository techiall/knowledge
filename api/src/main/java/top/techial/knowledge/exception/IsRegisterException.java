package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class IsRegisterException extends RuntimeException {
    public IsRegisterException() {
        super("user exists");
    }
}
