package top.techial.knowledge.web.rest.vm;

import top.techial.knowledge.service.valid.Insert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author techial
 */
public class ItemVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean share;

    @NotNull(groups = Insert.class)
    @NotEmpty(groups = Insert.class)
    private String name;

    private String description;

    private String image;

    public Boolean getShare() {
        return share;
    }

    public ItemVM setShare(Boolean share) {
        this.share = share;
        return this;
    }

    public String getName() {
        return name;
    }

    public ItemVM setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ItemVM setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ItemVM setImage(String image) {
        this.image = image;
        return this;
    }
}
