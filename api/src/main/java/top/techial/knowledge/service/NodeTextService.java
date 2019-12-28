package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.dao.NodeTextRepository;
import top.techial.knowledge.domain.NodeText;

/**
 * @author techial
 */
@Service
@CacheConfig(cacheNames = "user")
public class NodeTextService {
    private final NodeTextRepository nodeTextRepository;

    public NodeTextService(NodeTextRepository nodeTextRepository) {
        this.nodeTextRepository = nodeTextRepository;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public NodeText save(NodeText nodeText) {
        return nodeTextRepository.save(nodeText);
    }

    @CacheEvict(allEntries = true)
    @Transactional
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
