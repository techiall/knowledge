package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.RelationDTO;

@Mapper
public interface NodeRelationMapper {

    NodeRelationMapper INSTANCE = Mappers.getMapper(NodeRelationMapper.class);

    @Mapping(expression = "java(endNode(nodeRelation))", target = "node")
    RelationDTO toRelationDTO(NodeRelation nodeRelation);

    default NodeBaseDTO endNode(NodeRelation relation) {
        return KnowledgeNodeMapper.INSTANCE.toNodeBaseDTO(relation.getEndNode());
    }
}
