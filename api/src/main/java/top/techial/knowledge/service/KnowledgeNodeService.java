package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
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

/**
 * @author techial
 */
@Service
@Log4j2
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final NodeRelationRepository nodeRelationRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository, NodeRelationRepository nodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.nodeRelationRepository = nodeRelationRepository;
    }

    public Iterable<KnowledgeNode> saveAll(Iterable<KnowledgeNode> iterable) {
        return knowledgeNodeRepository.saveAll(iterable);
    }

    public KnowledgeNode update(Long id, NodeVO nodeVO) {
        KnowledgeNode node = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        node.setName(nodeVO.getName())
            .setLabels(nodeVO.getLabels())
            .setProperty(nodeVO.getProperty());
        return knowledgeNodeRepository.save(node);
    }

    public KnowledgeNode save(NodeVO nodeVO) {
        KnowledgeNode node = nodeVO.toKnowledgeNode();
        if (nodeVO.getParentId() == null) {
            node.setIsParentNode(true);
            return knowledgeNodeRepository.save(node);
        } else {
            KnowledgeNode parentNode = knowledgeNodeRepository.findById(nodeVO.getParentId()).orElse(null);
            if (parentNode == null) {
                node.setIsParentNode(true);
                return knowledgeNodeRepository.save(node);
            }
            parentNode.setIsParentNode(true);
            parentNode.getChildNodes().add(node);
            knowledgeNodeRepository.save(parentNode);
            return knowledgeNodeRepository.save(node);
        }
    }

    public KnowledgeNode findById(Long id) {
        return knowledgeNodeRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        knowledgeNodeRepository.deleteById(id);
    }

    public Object findByIdGraph(Long id) {
        KnowledgeNode knowledgeNode = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        List<NodeRelation> list = nodeRelationRepository.findByStartNodeName(knowledgeNode.getName());
        log.debug(list);

        Map<String, Object> result = new HashMap<>(16);
        List<Object> nodes = new ArrayList<>(16);
        List<Object> links = new ArrayList<>(16);

        nodes.add(buildNodes(knowledgeNode.getId(), knowledgeNode.getName()));
        for (NodeRelation nodeRelation : list) {
            nodes.add(buildNodes(nodeRelation.getEndNode().getId(),
                nodeRelation.getEndNode().getName()
            ));
            links.add(buildLinks(knowledgeNode.getId(), nodeRelation.getEndNode().getId(), nodeRelation.getProperty()));
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

    public Page<KnowledgeNode> findByNameLike(String name, Pageable pageable) {
        return knowledgeNodeRepository.findByNameLike(name, pageable);
    }

    public void deleteAll() {
        knowledgeNodeRepository.deleteAll();
    }

    public Page<KnowledgeNode> findAll(Pageable pageable) {
        return knowledgeNodeRepository.findAll(pageable);
    }

    public long count() {
        return knowledgeNodeRepository.count();
    }

    public Page<NodeDTO> findAllByIsParentNode(Pageable pageable) {
        return knowledgeNodeRepository.findByIsParentNode(true, pageable).map(NodeDTO::new);
    }

    public Optional<KnowledgeNode> findByName(String name) {
        return knowledgeNodeRepository.findFirstByName(name);
    }

}
