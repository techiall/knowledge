package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.*;

/**
 * @author techial
 */
@Service
@Log4j2
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository, KnowledgeNodeRelationRepository knowledgeNodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.knowledgeNodeRelationRepository = knowledgeNodeRelationRepository;
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
        List<KnowledgeNodeRelation> list = knowledgeNodeRelationRepository.findFirstByStartNodeName(knowledgeNode.getName());
        log.debug(list);

        Map<String, Object> result = new HashMap<>(16);
        List<Object> nodes = new ArrayList<>(16);
        List<Object> links = new ArrayList<>(16);

        nodes.add(buildNodes(knowledgeNode.getId(), knowledgeNode.getName(), knowledgeNode.getProperty(), knowledgeNode.getLabels()));
        for (KnowledgeNodeRelation knowledgeNodeRelation : list) {
            nodes.add(buildNodes(knowledgeNodeRelation.getEndNode().getId(),
                knowledgeNodeRelation.getEndNode().getName(),
                knowledgeNodeRelation.getEndNode().getProperty(),
                knowledgeNode.getLabels()));
            links.add(buildLinks(knowledgeNode.getId(), knowledgeNodeRelation.getEndNode().getId(), knowledgeNodeRelation.getProperty()));
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
