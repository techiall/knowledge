package top.techial.knowledge.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Collections;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeVO {

    private String name;

    private Map<String, String> property;


    public KnowledgeNode toKnowledgeNode() {
        return new KnowledgeNode()
            .setName(this.name)
            .setLabels(Collections.singleton(this.name))
            .setProperty(property);
    }
}
