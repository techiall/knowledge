package top.techial.knowledge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeNodeDTO {
    private Long group;

    private String name;

    private Map<String, String> property;

    public KnowledgeNodeDTO(KnowledgeNode node) {
        this.group = node.getId();
        this.name = node.getName();
        this.property = node.getProperty();
    }
}
