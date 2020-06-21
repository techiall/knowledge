package top.techial.knowledge.service.dto;

import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Property;

import java.io.Serializable;

/**
 * @author techial
 */
public class SearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private Labels labels;

    private Property property;

    private String nodeName;

    private String nodeItemName;

    private String text;

    public Long getNodeId() {
        return nodeId;
    }

    public SearchDTO setNodeId(Long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public Labels getLabels() {
        return labels;
    }

    public SearchDTO setLabels(Labels labels) {
        this.labels = labels;
        return this;
    }

    public Property getProperty() {
        return property;
    }

    public SearchDTO setProperty(Property property) {
        this.property = property;
        return this;
    }

    public String getNodeName() {
        return nodeName;
    }

    public SearchDTO setNodeName(String nodeName) {
        this.nodeName = nodeName;
        return this;
    }

    public String getNodeItemName() {
        return nodeItemName;
    }

    public SearchDTO setNodeItemName(String nodeItemName) {
        this.nodeItemName = nodeItemName;
        return this;
    }

    public String getText() {
        return text;
    }

    public SearchDTO setText(String text) {
        this.text = text;
        return this;
    }
}
