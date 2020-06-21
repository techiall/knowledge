package top.techial.knowledge.domain;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String originalFilename;

    private String contentType;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;

    public String getId() {
        return id;
    }

    public Storage setId(String id) {
        this.id = id;
        return this;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public Storage setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Storage setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Storage setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Storage setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }
}
