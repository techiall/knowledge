package top.techial.knowledge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Collections;
import java.util.Map;

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

    private Map<String, String> property;

    public KnowledgeNode toKnowledgeNode() {
        KnowledgeNode k = new KnowledgeNode();
        k.setName(this.name);
        k.setLabels(Collections.singleton(this.table));
        k.setProperty(property);
        return k;
    }
}
