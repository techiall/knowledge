package top.techial.knowledge.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author techial
 */
public class NodeRelationshipPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ancestor")
    private Long ancestor;

    @Id
    @Column(name = "descendant")
    private Long descendant;

    public NodeRelationshipPK() {
    }

    public NodeRelationshipPK(Long ancestor, Long descendant) {
        this.ancestor = ancestor;
        this.descendant = descendant;
    }

    public Long getAncestor() {
        return ancestor;
    }

    public void setAncestor(Long ancestor) {
        this.ancestor = ancestor;
    }

    public Long getDescendant() {
        return descendant;
    }

    public void setDescendant(Long descendant) {
        this.descendant = descendant;
    }
}
