package top.techial.knowledge.web.rest.vm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    public String getName() {
        return name;
    }

    public NodeVM setName(String name) {
        this.name = name;
        return this;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public NodeVM setLabels(Set<String> labels) {
        this.labels = labels;
        return this;
    }

    public Map<String, List<Property.PropertyDTO>> getProperty() {
        return property;
    }

    public NodeVM setProperty(Map<String, List<Property.PropertyDTO>> property) {
        this.property = property;
        return this;
    }

    public Long getParentId() {
        return parentId;
    }

    public NodeVM setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public NodeVM setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public Record getRecord() {
        return record;
    }

    public NodeVM setRecord(Record record) {
        this.record = record;
        return this;
    }

    public static class Record implements Serializable {

        private static final long serialVersionUID = 1L;

        private String operator;

        private String message;

        public String getOperator() {
            return operator;
        }

        public Record setOperator(String operator) {
            this.operator = operator;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Record setMessage(String message) {
            this.message = message;
            return this;
        }
    }
}
