package top.techial.knowledge.service.dto;


import java.io.Serializable;

public class ParentChildDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long descendant;
    private Long ancestor;

    public Long getDescendant() {
        return descendant;
    }

    public ParentChildDTO setDescendant(Long descendant) {
        this.descendant = descendant;
        return this;
    }

    public Long getAncestor() {
        return ancestor;
    }

    public ParentChildDTO setAncestor(Long ancestor) {
        this.ancestor = ancestor;
        return this;
    }
}
