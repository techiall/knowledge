package top.techial.knowledge.service.dto;

import java.util.List;

/**
 * @author techial
 */
public class NodeTreeDTO {

    private Long id;

    private String name;

    private List<NodeTreeDTO> child;

    public Long getId() {
        return id;
    }

    public NodeTreeDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public NodeTreeDTO setName(String name) {
        this.name = name;
        return this;
    }

    public List<NodeTreeDTO> getChild() {
        return child;
    }

    public NodeTreeDTO setChild(List<NodeTreeDTO> child) {
        this.child = child;
        return this;
    }
}

