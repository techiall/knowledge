package top.techial.knowledge.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.service.dto.NodeInfoDTO;
import top.techial.knowledge.service.dto.NodeTreeDTO;
import top.techial.knowledge.web.rest.vm.NodeVM;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeMapper {
    NodeMapper INSTANCE = Mappers.getMapper(NodeMapper.class);

    @Mapping(source = "labels", target = "labels.labels")
    @Mapping(source = "property", target = "property.property")
    @Mapping(source = "itemId", target = "itemId")
    Node toNode(NodeVM nodeVM);

    @Mapping(source = "labels.labels", target = "labels")
    @Mapping(source = "property.property", target = "property")
    NodeInfoDTO toNodeInfoDTO(Node node);

    NodeTreeDTO toNodeTreeDTO(Node node);

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