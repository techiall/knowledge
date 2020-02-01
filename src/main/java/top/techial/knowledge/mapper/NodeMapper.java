package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.dto.NodeTreeDTO;
import top.techial.knowledge.vo.NodeVO;

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
    Node toNode(NodeVO nodeVO);

    @Mapping(source = "labels.labels", target = "labels")
    @Mapping(source = "property.property", target = "property")
    NodeInfoDTO toNodeInfoDTO(Node node);

    NodeTreeDTO toNodeTreeDTO(Node node);

    default List<SimpleGrantedAuthority> toListSimpleGrantedAuthority(List<Long> nodes) {
        return nodes
                .parallelStream()
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
