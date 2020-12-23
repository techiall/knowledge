package cc.techial.knowledge.service.mapper;

import cc.techial.knowledge.domain.Item;
import cc.techial.knowledge.domain.Node;
import cc.techial.knowledge.service.dto.NodeInfoDTO;
import cc.techial.knowledge.service.dto.NodeTreeDTO;
import cc.techial.knowledge.service.dto.SearchDTO;
import cc.techial.knowledge.web.rest.vm.NodeVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeMapper {

    @Mapping(source = "labels", target = "labels.labels")
    @Mapping(source = "property", target = "property.property")
    @Mapping(source = "itemId", target = "itemId")
    Node toNode(NodeVM nodeVM);

    @Mapping(source = "labels.labels", target = "labels")
    @Mapping(source = "property.property", target = "property")
    NodeInfoDTO toNodeInfoDTO(Node node);

    NodeTreeDTO toNodeTreeDTO(Node node);

    @Mapping(source = "item.name", target = "nodeItemName")
    @Mapping(source = "node.name", target = "nodeName")
    @Mapping(source = "node.id", target = "nodeId")
    SearchDTO toSearchDTO(Node node, Item item);

    default List<SimpleGrantedAuthority> toListSimpleGrantedAuthority(List<Long> nodes) {
        if (nodes == null) {
            return Collections.emptyList();
        }
        return nodes
                .parallelStream()
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
