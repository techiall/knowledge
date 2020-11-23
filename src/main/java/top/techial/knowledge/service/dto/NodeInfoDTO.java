package top.techial.knowledge.service.dto;

import top.techial.knowledge.domain.Property;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
public class NodeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Set<String> labels;

    private String name;

    private Integer itemId;

    private Long parentNodeId;

    private Map<String, List<Property.PropertyDTO>> property;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public Map<String, List<Property.PropertyDTO>> getProperty() {
        return property;
    }

    public void setProperty(Map<String, List<Property.PropertyDTO>> property) {
        this.property = property;
    }
}

