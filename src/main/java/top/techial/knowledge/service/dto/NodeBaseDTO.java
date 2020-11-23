package top.techial.knowledge.service.dto;

import java.io.Serializable;

/**
 * @author techial
 */
public class NodeBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long parentNodeId;

    private Long id;

    private String name;

    private Boolean child;

    public NodeBaseDTO() {
    }

    public NodeBaseDTO(Long parentNodeId, Long id, String name, Boolean child) {
        this.parentNodeId = parentNodeId;
        this.id = id;
        this.name = name;
        this.child = child;
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getChild() {
        return child;
    }

    public void setChild(Boolean child) {
        this.child = child;
    }
}
