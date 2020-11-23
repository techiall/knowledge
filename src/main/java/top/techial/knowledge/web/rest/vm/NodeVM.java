package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.lang.Nullable;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.service.valid.Insert;
import top.techial.knowledge.service.valid.Update;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVM implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(groups = Insert.class)
    @NotEmpty(groups = Insert.class)
    private String name;

    @Nullable
    private Set<String> labels;

    @Nullable
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(@Nullable Set<String> labels) {
        this.labels = labels;
    }

    @Nullable
    public Map<String, List<Property.PropertyDTO>> getProperty() {
        return property;
    }

    public void setProperty(@Nullable Map<String, List<Property.PropertyDTO>> property) {
        this.property = property;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public static class Record implements Serializable {

        private static final long serialVersionUID = 1L;

        private String operator;

        private String message;

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
