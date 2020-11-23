package top.techial.knowledge.web.rest.vm;

import org.springframework.lang.Nullable;
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

    @Nullable
    private String description;

    @Nullable
    private String image;

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    public void setImage(@Nullable String image) {
        this.image = image;
    }
}
