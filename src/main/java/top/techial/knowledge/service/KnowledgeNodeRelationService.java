package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.List;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeRelationService {
    private final KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    public KnowledgeNodeRelationService(KnowledgeNodeRelationRepository knowledgeNodeRelationRepository) {
        this.knowledgeNodeRelationRepository = knowledgeNodeRelationRepository;
    }

    public Iterable<KnowledgeNodeRelation> saveAll(Iterable<KnowledgeNodeRelation> buildNodeRelation) {
        return knowledgeNodeRelationRepository.saveAll(buildNodeRelation);
    }

    public List<KnowledgeNodeRelation> findFirstByStartNodeName(String name) {
        return knowledgeNodeRelationRepository.findFirstByStartNodeName(name);
    }
}
