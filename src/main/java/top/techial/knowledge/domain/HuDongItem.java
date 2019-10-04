package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Set;

/**
 * @author techial
 */
@NodeEntity(label = "HudongItem")
@Data
@Accessors(chain = true)
public class HuDongItem {
    private Long id;

    @Index(unique = true)
    @Property(name = "title")
    private String title;

    @Property(name = "image")
    private String image;

    @Property(name = "detail")
    private String detail;

    @Property(name = "url")
    private String url;

    @Property(name = "openTypeList")
    private String openTypeList;

    @Property(name = "baseInfoKeyList")
    private String baseInfoKeyList;

    @Property(name = "baseInfoKeyList")
    private String baseInfoValueList;

    @Relationship(type = "RELATION")
    private List<NewNode> newNode;

    @Relationship(type = "RELATION")
    private List<HuDongItem> huDongItems;
}
