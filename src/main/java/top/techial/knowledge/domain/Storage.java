package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
@Entity
@Accessors(chain = true)
public class Storage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String originalFilename;

    private String contentType;

    @Version
    private Long version;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;

}
