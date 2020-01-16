package top.techial.knowledge.exception;

/**
 * @author techial
 */
public class ItemException extends RuntimeException {
    public ItemException(Integer id) {
        super(String.format("itemId: [%s] not found.", id));
    }
}
