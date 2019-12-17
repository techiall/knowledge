package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.vo.NodeVO;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface KnowledgeNodeMapper {

    KnowledgeNodeMapper INSTANCE = Mappers.getMapper(KnowledgeNodeMapper.class);

    @Mapping(target = "labels", expression = "java(labels(nodeVO.getLabels()))")
    @Mapping(source = "parentId", target = "parentNodeId")
    KnowledgeNode toKnowledgeNode(NodeVO nodeVO);

    @Mapping(expression = "java(childNodes(knowledgeNode))", target = "childNodes")
    NodeDTO toNodeDTO(KnowledgeNode knowledgeNode);

    NodeBaseDTO toNodeBaseDTO(KnowledgeNode knowledgeNode);

    NodeInfoDTO toNodeInfoDTO(KnowledgeNode knowledgeNode);

    default List<NodeDTO> childNodes(KnowledgeNode knowledgeNode) {
        return knowledgeNode.getChildNodes().stream()
            .sorted(Comparator.comparing(KnowledgeNode::getCreateTime))
            .map(this::toNodeDTO).collect(Collectors.toList());
    }

    default Set<String> labels(Set<String> nodeVO) {
        return nodeVO == null || nodeVO.isEmpty() ? new HashSet<>() : nodeVO;
    }
}
