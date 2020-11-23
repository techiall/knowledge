package top.techial.knowledge.service.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author techial
 */
public class NodeTreeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private List<NodeTreeDTO> child;

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

    public List<NodeTreeDTO> getChild() {
        return child;
    }

    public void setChild(List<NodeTreeDTO> child) {
        this.child = child;
    }
}

