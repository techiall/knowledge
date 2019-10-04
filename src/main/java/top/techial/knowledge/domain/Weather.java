package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author techial
 */
@NodeEntity
@Data
@Accessors(chain = true)
public class Weather {

    @Index(unique = true)
    private String title;
}
