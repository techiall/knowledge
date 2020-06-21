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

    public NodeInfoDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public NodeInfoDTO setLabels(Set<String> labels) {
        this.labels = labels;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeInfoDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getItemId() {
        return itemId;
    }

    public NodeInfoDTO setItemId(Integer itemId) {
        this.itemId = itemId;
        return this;
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public NodeInfoDTO setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
        return this;
    }

    public Map<String, List<Property.PropertyDTO>> getProperty() {
        return property;
    }

    public NodeInfoDTO setProperty(Map<String, List<Property.PropertyDTO>> property) {
        this.property = property;
        return this;
    }
}

