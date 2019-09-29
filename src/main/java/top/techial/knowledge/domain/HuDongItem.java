package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

/**
 * @author techial
 */
@NodeEntity
@Data
@Accessors(chain = true)
public class HuDongItem {

    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String title;

    private String image;

    private String detail;

    private String url;

    private String openTypeList;

    private String baseInfoKeyList;

    private String baseInfoValueList;

}
