package top.techial.knowledge.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Entity
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeText implements Serializable {
    @Id
    private Long id;

    @Lob
    private String text;

    @CreationTimestamp
    private Instant createTime;

    @UpdateTimestamp
    private Instant updateTime;
}
