package cc.techial.knowledge.service.dto;

import java.io.Serializable;

public class ParentChildDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long descendant;
    private Long ancestor;

    public Long getDescendant() {
        return descendant;
    }

    public void setDescendant(Long descendant) {
        this.descendant = descendant;
    }

    public Long getAncestor() {
        return ancestor;
    }

    public void setAncestor(Long ancestor) {
        this.ancestor = ancestor;
    }
}
