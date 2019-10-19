package top.techial.knowledge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.List;
import java.util.Map;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LinksDTO {

    private Long source;

    private Long target;

    private Map<String, String> values;

    public LinksDTO(KnowledgeNode begin, KnowledgeNode end, Map<String, String> value) {
        this.source = begin.getId();
        this.target = end.getId();
        this.values = value;
    }
}
