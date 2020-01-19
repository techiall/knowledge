package top.techial.knowledge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author techial
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeRelationshipPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ancestor")
    private Long ancestor;

    @Id
    @Column(name = "descendant")
    private Long descendant;
}
