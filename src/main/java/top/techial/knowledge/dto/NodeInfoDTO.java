package top.techial.knowledge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class NodeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Set<String> labels;
    private String name;
    private Integer itemId;
    private Map<String, String> property;
}
