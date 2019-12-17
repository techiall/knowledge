package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.RelationDTO;

@Mapper
public interface NodeRelationMapper {
    NodeRelationMapper INSTANCE = Mappers.getMapper(NodeRelationMapper.class);

    @Mapping(expression = "java(nodeRelation.endNode)", target = "node")
    RelationDTO toRelationDTO(NodeRelation nodeRelation);

    default NodeBaseDTO endNode(KnowledgeNode endNode) {
        return KnowledgeNodeMapper.INSTANCE.toNodeBaseDTO(endNode);
    }
}
