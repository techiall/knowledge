package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class RelationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private NodeBaseDTO node;

    private Map<String, String> property;

}
