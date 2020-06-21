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

    public NodeBaseDTO setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public NodeBaseDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeBaseDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getChild() {
        return child;
    }

    public NodeBaseDTO setChild(Boolean child) {
        this.child = child;
        return this;
    }
}
