package cc.techial.knowledge.service.dto;

import cc.techial.knowledge.domain.Labels;
import cc.techial.knowledge.domain.Property;

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

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeItemName() {
        return nodeItemName;
    }

    public void setNodeItemName(String nodeItemName) {
        this.nodeItemName = nodeItemName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

