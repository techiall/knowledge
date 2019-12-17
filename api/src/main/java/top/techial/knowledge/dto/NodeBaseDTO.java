package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parentNodeId;

    private Long id;

    private String name;

}
