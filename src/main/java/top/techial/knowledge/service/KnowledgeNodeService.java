package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> result = new HashMap<>(16);
        List<KnowledgeNodeRelation> list = knowledgeNodeRelationRepository.findByStartNodeId(knowledgeNode.getId());
        Map<String, Object> nodes = new HashMap<>(16);
        Map<String, Object> links = new HashMap<>(16);
        log.debug(links);

        nodes.put("id", knowledgeNode.getId());
        nodes.put("labels", knowledgeNode.getLabels());
        nodes.put("name", knowledgeNode.getName());
        nodes.putAll(knowledgeNode.getProperty());
        for (KnowledgeNodeRelation knowledgeNodeRelation : list) {
            nodes.put("id", knowledgeNodeRelation.getEndNode().getId());
            nodes.put("labels", knowledgeNodeRelation.getEndNode().getLabels());
            nodes.put("name", knowledgeNodeRelation.getEndNode().getName());
            nodes.putAll(knowledgeNodeRelation.getEndNode().getProperty());
            links.putAll(buildLinks("source", knowledgeNode.getId(), "target", knowledgeNodeRelation.getEndNode().getId()));
        }
        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    private Map<String, Object> buildLinks(String source, Long sourceId, String target, Long targetId) {
        Map<String, Object> map = new HashMap<>();
        map.put(source, sourceId);
        map.put(target, targetId);
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
