package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.NodeTextRepository;
import top.techial.knowledge.domain.NodeText;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = {"user", "record", "node-relation", "knowledge-node"})
public class NodeTextService {
    private final NodeTextRepository nodeTextRepository;

    public NodeTextService(NodeTextRepository nodeTextRepository) {
        this.nodeTextRepository = nodeTextRepository;
    }

    @CacheEvict(allEntries = true)
    public NodeText save(NodeText nodeText) {
        return nodeTextRepository.save(nodeText);
    }

    @Async
    @CacheEvict(allEntries = true)
    public void save(String text, Long id) {
        NodeText nodeText = nodeTextRepository.findById(id).orElse(new NodeText());
        nodeText = nodeText.setId(id).setText(text);
        nodeTextRepository.save(nodeText);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public NodeText findById(Long id) {
        return nodeTextRepository.findById(id).orElse(new NodeText());
    }
}
