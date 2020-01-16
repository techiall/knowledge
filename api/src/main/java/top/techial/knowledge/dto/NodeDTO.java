package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parentNodeId;

    private Long id;

    private String name;

    private List<NodeDTO> childNodes;

}
