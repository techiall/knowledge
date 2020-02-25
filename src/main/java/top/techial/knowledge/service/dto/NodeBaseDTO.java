package top.techial.knowledge.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NodeBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parentNodeId;

    private Long id;

    private String name;

    private Boolean child;

}
