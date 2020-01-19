package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.dao.NodeRelationshipRepository;
import top.techial.knowledge.dao.NodeRepository;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.exception.NodeNotFoundException;
import top.techial.knowledge.mapper.NodeMapper;
import top.techial.knowledge.vo.NodeVO;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "node")
public class NodeService {
    private final NodeRepository nodeRepository;
    private final NodeRelationshipRepository nodeRelationshipRepository;

    public NodeService(NodeRepository nodeRepository, NodeRelationshipRepository nodeRelationshipRepository) {
        this.nodeRepository = nodeRepository;
        this.nodeRelationshipRepository = nodeRelationshipRepository;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node save(Node node) {
        return nodeRepository.save(node);
    }


    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteByItemId(Integer id) {
        nodeRepository.deleteAllByItemId(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Node findById(Long id) {
        return nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));
    }

    @CacheEvict(allEntries = true)
    public Node updateName(Long id, String name) {
        Node node = nodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id))
                .setName(name);
        return nodeRepository.save(node);
    }

    public Node save(NodeVO nodeVO) {
        Node node = NodeMapper.INSTANCE.toNode(nodeVO);
        node = nodeRepository.save(node);
        nodeRelationshipRepository.insertNode(node.getId(), nodeVO.getParentId());
        return node;
    }



}
