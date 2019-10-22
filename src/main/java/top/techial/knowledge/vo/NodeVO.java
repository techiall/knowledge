package top.techial.knowledge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class NodeVO {
    private String table;

    private String name;

    private Set<String> labels;

    private Map<String, String> property;

    public KnowledgeNode toKnowledgeNode() {
        return new KnowledgeNode()
            .setName(this.name)
            .setLabels(labels == null || labels.isEmpty() ? Collections.singleton(this.name) : labels)
            .setLabels(Collections.singleton(this.table))
            .setProperty(property);
    }
}
