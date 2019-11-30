package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.vo.NodeVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "knowledge-node")
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final NodeRelationRepository nodeRelationRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository, NodeRelationRepository nodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.nodeRelationRepository = nodeRelationRepository;
    }

    @CacheEvict(allEntries = true)
    public Iterable<KnowledgeNode> saveAll(Iterable<KnowledgeNode> iterable) {
        return knowledgeNodeRepository.saveAll(iterable);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode update(Long id, NodeVO nodeVO) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        node.setName(nodeVO.getName())
            .setLabels(nodeVO.getLabels())
            .setProperty(nodeVO.getProperty());
        return knowledgeNodeRepository.save(node);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode save(KnowledgeNode node) {
        return knowledgeNodeRepository.save(node);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode save(NodeVO nodeVO) {
        KnowledgeNode node = nodeVO.toKnowledgeNode();

        KnowledgeNode findNode = knowledgeNodeRepository.findFirstByName(node.getName()).orElse(null);
        if (findNode != null) {
            return findNode;
        }

        if (node.getParentNodeId() == null) {
            return knowledgeNodeRepository.save(node);
        }

        KnowledgeNode parentNode = knowledgeNodeRepository.findById(node.getParentNodeId()).orElse(null);
        if (parentNode == null) {
            return knowledgeNodeRepository.save(node);
        }
        node = knowledgeNodeRepository.save(node);
        parentNode.getChildNodes().add(node);
        knowledgeNodeRepository.save(parentNode);
        return node;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id", unless = "#result == null")
    public KnowledgeNode findById(Long id) {
        return knowledgeNodeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @CacheEvict(allEntries = true)
    public void deleteById(Long id) {
        knowledgeNodeRepository.deleteById(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id", unless = "#result == null")
    public Object findByIdGraph(Long id) {
        KnowledgeNode knowledgeNode = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        List<NodeRelation> list = nodeRelationRepository.findByStartNodeName(knowledgeNode.getName());
        if (log.isDebugEnabled()) {
            log.debug(list);
        }

        Map<String, Object> result = new HashMap<>(16);
        List<Object> nodes = new ArrayList<>(16);
        List<Object> links = new ArrayList<>(16);

        nodes.add(buildNodes(knowledgeNode.getId(), knowledgeNode.getName()));
        for (NodeRelation nodeRelation : list) {
            nodes.add(buildNodes(nodeRelation.getEndNode().getId(), nodeRelation.getEndNode().getName()));
            links.add(buildLinks(knowledgeNode.getId(), nodeRelation.getEndNode().getId(), nodeRelation.getProperty()));
        }

        LinkedList<KnowledgeNode> queue = new LinkedList<>();
        queue.add(knowledgeNode);
        while (!queue.isEmpty()) {
            KnowledgeNode node = knowledgeNodeRepository
                .findById(queue.removeFirst().getId())
                .orElseThrow(NullPointerException::new);

            for (KnowledgeNode childNode : node.getChildNodes()) {
                nodes.add(buildNodes(childNode.getId(), childNode.getName()));
                links.add(buildLinks(node.getId(), childNode.getId(),
                    Collections.singletonMap("relation", "parent-child-relation")));
                queue.add(childNode);
            }
        }

        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    private Map<String, Object> buildNodes(Long id, String name) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    private Map<String, Object> buildLinks(Long sourceId, Long targetId, Map<String, String> property) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("source", sourceId);
        map.put("target", targetId);
        map.put("property", property);
        return map;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #name + #pageable", unless = "#result == null")
    public Page<KnowledgeNode> findByNameLike(String name, Pageable pageable) {
        return knowledgeNodeRepository.findByNameLike(name, pageable);
    }

    @CacheEvict(allEntries = true)
    public void deleteAll() {
        knowledgeNodeRepository.deleteAll();
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #pageable + #depth", unless = "#result == null")
    public Page<NodeDTO> findAll(Pageable pageable, int depth) {
        return knowledgeNodeRepository.findAllByParentNodeIdIsNull(pageable, depth).map(NodeDTO::new);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName", unless = "#result == null")
    public long count() {
        return knowledgeNodeRepository.count();
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName", unless = "#result == null")
    public Optional<KnowledgeNode> findByName(String name) {
        return knowledgeNodeRepository.findFirstByName(name);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode updateName(Long id, String name) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        if (node.getLabels() != null && !node.getLabels().isEmpty()) {
            node.getLabels().remove(node.getName());
            node.getLabels().add(name);
        }
        node.setName(name);
        return knowledgeNodeRepository.save(node);
    }


    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #id + #depth", unless = "#result == null")
    public Set<NodeDTO> findByChildNode(Long id, int depth) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id, depth).orElseThrow(NullPointerException::new);
        return node.getChildNodes().stream().map(NodeDTO::new).collect(Collectors.toSet());
    }

    @CacheEvict(allEntries = true)
    public void deleteChildId(Long parentId, Long childId) {
        KnowledgeNode child = knowledgeNodeRepository.findById(childId).orElseThrow(NullPointerException::new);
        knowledgeNodeRepository.delete(child);
        KnowledgeNode parent = knowledgeNodeRepository.findById(parentId).orElseThrow(NullPointerException::new);
        if (parent.getChildNodes().isEmpty()) {
            knowledgeNodeRepository.save(parent);
        }
    }

    @CacheEvict(allEntries = true)
    public void deleteByIds(Set<Long> ids) {
        knowledgeNodeRepository.deleteByIdIn(ids);
    }
}
