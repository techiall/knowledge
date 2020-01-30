package top.techial.knowledge.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = {"child"})
@ToString(exclude = {"child"})
public class NodeTreeDTO {

    private Long id;

    private String name;

    private List<NodeTreeDTO> child;
}

