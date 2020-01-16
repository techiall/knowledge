package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.exception.ItemNotFoundNodeException;
import top.techial.knowledge.exception.NodeNotFoundException;
import top.techial.knowledge.mapper.KnowledgeNodeMapper;
import top.techial.knowledge.vo.NodeVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "user")
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final NodeRelationRepository nodeRelationRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository, NodeRelationRepository nodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.nodeRelationRepository = nodeRelationRepository;
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode update(Long id, Integer itemId, NodeVO nodeVO) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));

        if (!Objects.equals(itemId, node.getItemId())) {
            throw new ItemNotFoundNodeException(itemId, id);
        }
        node.setName(nodeVO.getName())
                .setLabels(nodeVO.getLabels())
                .setProperty(nodeVO.getProperty())
                .setItemId(node.getItemId());
        return knowledgeNodeRepository.save(node);
    }

    @CacheEvict(allEntries = true)
    public void save(KnowledgeNode node) {
        knowledgeNodeRepository.save(node);
    }

    @CacheEvict(allEntries = true)
    public KnowledgeNode save(Integer itemId, NodeVO nodeVO) {
        KnowledgeNode node = KnowledgeNodeMapper.INSTANCE.toKnowledgeNode(nodeVO);
        node.setItemId(itemId);

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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public KnowledgeNode findById(Long id) {
        return knowledgeNodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Object findByIdGraph(Long id) {
        KnowledgeNode knowledgeNode = knowledgeNodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));
        List<NodeRelation> list = nodeRelationRepository.findByStartNodeName(knowledgeNode.getName());
        if (log.isDebugEnabled()) {
            log.debug(list);
        }

        Map<String, Object> result = new HashMap<>(16);
        List<Object> nodes = new ArrayList<>(16);
        List<Object> links = new ArrayList<>(16);

        nodes.add(buildNodes(knowledgeNode.getId(), knowledgeNode.getName()));

        // 关系
        for (NodeRelation nodeRelation : list) {
            nodes.add(buildNodes(nodeRelation.getEndNode().getId(), nodeRelation.getEndNode().getName()));
            links.add(buildLinks(knowledgeNode.getId(), nodeRelation.getEndNode().getId(), nodeRelation.getProperty()));
        }

        // 子节点
        LinkedList<KnowledgeNode> queue = new LinkedList<>();
        queue.add(knowledgeNode);
        while (!queue.isEmpty()) {
            KnowledgeNode node = knowledgeNodeRepository
                    .findById(queue.removeFirst().getId())
                    .orElseThrow(() -> new NodeNotFoundException(id));

            for (KnowledgeNode childNode : node.getChildNodes()) {
                nodes.add(buildNodes(childNode.getId(), childNode.getName()));
                links.add(buildLinks(node.getId(), childNode.getId(),
                        Collections.singletonMap("relation", "child-relation")));
                queue.add(childNode);
            }
        }

        // 父节点
        KnowledgeNode tmpNode = knowledgeNode;
        while (tmpNode.getParentNodeId() != null) {
            KnowledgeNode node = knowledgeNodeRepository.findById(tmpNode.getParentNodeId())
                    .orElseThrow(() -> new NodeNotFoundException(id));
            nodes.add(buildNodes(node.getId(), node.getName()));
            links.add(buildLinks(node.getId(), tmpNode.getId(),
                    Collections.singletonMap("relation", "parent-relation")));
            tmpNode = node;
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

    @CacheEvict(allEntries = true)
    public KnowledgeNode updateName(Long id, Integer itemId, String name) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id)
                .orElseThrow(() -> new NodeNotFoundException(id));
        if (!Objects.equals(itemId, node.getItemId())) {
            throw new ItemNotFoundNodeException(itemId, id);
        }
        node.setName(name);
        return knowledgeNodeRepository.save(node);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1 + #p2", unless = "#result == null")
    public Page<KnowledgeNode> findByNameLike(String name, Integer itemId, Pageable pageable) {
        return knowledgeNodeRepository.findByNameLikeAndItemId(name, itemId, pageable);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Page<KnowledgeNode> findAllByNameLike(String name, Pageable pageable) {
        return knowledgeNodeRepository.findAllByNameLike(name, pageable);
    }

    @CacheEvict(allEntries = true)
    @Async
    public void deleteByIds(Set<Long> ids, Integer itemId) {
        ids.forEach(it -> deleteById(it, itemId));
    }

    @CacheEvict(allEntries = true)
    public void deleteById(Long id, Integer itemId) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        if (!Objects.equals(itemId, node.getItemId())) {
            throw new ItemNotFoundNodeException(itemId, id);
        }
        knowledgeNodeRepository.delete(node);
        if (node.getParentNodeId() == null) {
            return;
        }
        KnowledgeNode parent = knowledgeNodeRepository.findById(node.getParentNodeId()).orElse(null);
        if (parent == null) {
            return;
        }
        if (parent.getChildNodes().isEmpty()) {
            knowledgeNodeRepository.save(parent);
        }
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Page<NodeDTO> findAll(Integer itemId, Pageable pageable) {
        return knowledgeNodeRepository.findByItemIdAndParentNodeIdIsNull(itemId, pageable).map(KnowledgeNodeMapper.INSTANCE::toNodeDTO);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1 + #p2", unless = "#result == null")
    public List<NodeDTO> findByChildNode(Long id, Integer itemId, int depth) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id, depth)
                .orElseThrow(() -> new NodeNotFoundException(id));
        if (!Objects.equals(itemId, node.getItemId())) {
            throw new ItemNotFoundNodeException(itemId, id);
        }
        return node.getChildNodes().parallelStream().map(KnowledgeNodeMapper.INSTANCE::toNodeDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Map<String, List<NodeBaseDTO>> getChildAndParent(Long id, int depth) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id, depth)
                .orElseThrow(() -> new NodeNotFoundException(id));

        Map<String, List<NodeBaseDTO>> map = new HashMap<>();
        map.put("child", node.getChildNodes().parallelStream().map(KnowledgeNodeMapper.INSTANCE::toNodeBaseDTO)
                .collect(Collectors.toList()));

        KnowledgeNode tmpNode = node;
        List<KnowledgeNode> list = new ArrayList<>();
        while (tmpNode.getParentNodeId() != null) {
            KnowledgeNode tmp1 = knowledgeNodeRepository.findById(tmpNode.getParentNodeId())
                    .orElseThrow(() -> new NodeNotFoundException(id));
            list.add(tmp1);
            tmpNode = tmp1;
        }
        map.put("parent", list.parallelStream().map(KnowledgeNodeMapper.INSTANCE::toNodeBaseDTO).collect(Collectors.toList()));
        return map;
    }

    @CacheEvict(allEntries = true)
    public Long deleteByItemId(Integer id) {
        return knowledgeNodeRepository.deleteByItemId(id);
    }

}
