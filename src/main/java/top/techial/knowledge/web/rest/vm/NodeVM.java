package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.valid.Insert;
import top.techial.knowledge.valid.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVM implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Insert.class)
    @NotEmpty(groups = Insert.class)
    private String name;

    private Set<String> labels;

    private Map<String, List<Property.PropertyDTO>> property;

    /**
     * parentId = null -> parent
     * parentId != null && parentId is exists -> child
     * parent != null && parentId is not exists -> parent
     */
    private Long parentId;

    @NotNull(groups = {Insert.class, Update.class})
    private Integer itemId;

    @NotNull(groups = {Insert.class, Update.class})
    private Record record;

    @Data
    public static class Record implements Serializable {

        private static final long serialVersionUID = 1L;

        private String operator;

        private String message;

    }
}
