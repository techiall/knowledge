package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.dao.NodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;

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

    public KnowledgeNode save(KnowledgeNode knowledgeNode) {
        return knowledgeNodeRepository.save(knowledgeNode);
    }

    public KnowledgeNode findById(Long id) {
        return knowledgeNodeRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        knowledgeNodeRepository.deleteById(id);
    }

    public Object findByIdGraph(Long id) {
        KnowledgeNode knowledgeNode = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        List<NodeRelation> list = nodeRelationRepository.findFirstByStartNodeName(knowledgeNode.getName());
        log.debug(list);

        Map<String, Object> result = new HashMap<>(16);
        List<Object> nodes = new ArrayList<>(16);
        List<Object> links = new ArrayList<>(16);

        nodes.add(buildNodes(knowledgeNode.getId(), knowledgeNode.getName(), knowledgeNode.getProperty(), knowledgeNode.getLabels()));
        for (NodeRelation nodeRelation : list) {
            nodes.add(buildNodes(nodeRelation.getEndNode().getId(),
                nodeRelation.getEndNode().getName(),
                nodeRelation.getEndNode().getProperty(),
                knowledgeNode.getLabels()));
            links.add(buildLinks(knowledgeNode.getId(), nodeRelation.getEndNode().getId(), nodeRelation.getProperty()));
        }
        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    private Map<String, Object> buildNodes(Long id, String name, Map<String, String> property, Collection<String> labels) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("id", id);
        map.put("name", name);
        map.put("property", property);
        map.put("labels", labels);
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

    public KnowledgeNode findByName(String name) {
        return knowledgeNodeRepository.findFirstByName(name).orElseThrow(NullPointerException::new);
    }

    public void deleteAll() {
        knowledgeNodeRepository.deleteAll();
    }

    public Page<KnowledgeNode> findAll(Pageable pageable) {
        return knowledgeNodeRepository.findAll(pageable);
    }
}
