package top.techial.knowledge.service;

import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.RelationDTO;
import top.techial.knowledge.exception.NodeNotFoundException;
import top.techial.knowledge.mapper.NodeRelationMapper;
import top.techial.knowledge.vo.ParentVO;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = "user")
public class NodeRelationService {
    private final NodeRelationRepository nodeRelationRepository;
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public NodeRelationService(
        NodeRelationRepository nodeRelationRepository,
        KnowledgeNodeRepository knowledgeNodeRepository,
        ThreadPoolTaskExecutor threadPoolTaskExecutor
    ) {
        this.nodeRelationRepository = nodeRelationRepository;
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #name", unless = "#result == null")
    public List<RelationDTO> findByStartNodeName(String name) {
        return nodeRelationRepository.findByStartNodeName(name)
            .parallelStream()
            .map(NodeRelationMapper.INSTANCE::toRelationDTO)
            .collect(Collectors.toList());
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id", unless = "#result == null")
    public NodeRelation findById(Long id) {
        return nodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #pageable", unless = "#result == null")
    public Page<NodeRelation> findAll(Pageable pageable) {
        return nodeRelationRepository.findAll(pageable);
    }

    @CacheEvict(allEntries = true)
    @SneakyThrows
    public NodeRelation save(RelationVO relationVO) {
        Future<KnowledgeNode> node1Future = threadPoolTaskExecutor.submit(() -> knowledgeNodeRepository
            .findFirstByName(relationVO.getStartNode()).orElseThrow(() -> new NodeNotFoundException(relationVO)));

        Future<KnowledgeNode> node2Future = threadPoolTaskExecutor.submit(() -> knowledgeNodeRepository
            .findFirstByName(relationVO.getEndNode()).orElseThrow(() -> new NodeNotFoundException(relationVO)));

        return nodeRelationRepository.save(new NodeRelation()
            .setStartNode(node1Future.get())
            .setEndNode(node2Future.get())
            .setProperty(relationVO.getProperty()));
    }

    @CacheEvict(allEntries = true)
    @SneakyThrows
    public NodeRelation updateById(Long id, RelationVO relationVO) {
        NodeRelation relation = nodeRelationRepository.findById(id)
            .orElseThrow(() -> new NodeNotFoundException(id));

        Future<KnowledgeNode> node1Future = threadPoolTaskExecutor.submit(() -> knowledgeNodeRepository
            .findFirstByName(relationVO.getStartNode()).orElseThrow(() -> new NodeNotFoundException(relationVO)));

        Future<KnowledgeNode> node2Future = threadPoolTaskExecutor.submit(() -> knowledgeNodeRepository
            .findFirstByName(relationVO.getEndNode()).orElseThrow(() -> new NodeNotFoundException(relationVO)));

        relation.setStartNode(node1Future.get()).setEndNode(node2Future.get()).setProperty(relationVO.getProperty());
        return nodeRelationRepository.save(relation);
    }

    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {
        nodeRelationRepository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode updateParent(ParentVO parentVO) {
        KnowledgeNode node = knowledgeNodeRepository.findFirstByName(parentVO.getSrcParentName())
            .orElseThrow(() -> new NodeNotFoundException(parentVO));

        if (node.getChildNodes().isEmpty()) {
            throw new IllegalArgumentException();
        }

        KnowledgeNode childNode = knowledgeNodeRepository.findFirstByName(parentVO.getChildName())
            .orElseThrow(() -> new NodeNotFoundException(parentVO));

        node.getChildNodes().remove(childNode);
        knowledgeNodeRepository.save(node);

        KnowledgeNode node1 = knowledgeNodeRepository.findFirstByName(parentVO.getNewParentName())
            .orElseThrow(() -> new NodeNotFoundException(parentVO));

        node1.getChildNodes().add(childNode);
        return knowledgeNodeRepository.save(node1);
    }
}
