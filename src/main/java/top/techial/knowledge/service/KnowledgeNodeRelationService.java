package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public KnowledgeNodeRelation findById(Long id) {
        return knowledgeNodeRelationRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public Page<KnowledgeNodeRelation> findAll(Pageable pageable) {
        return knowledgeNodeRelationRepository.findAll(pageable);
    }

    public long count() {
        return knowledgeNodeRelationRepository.count();
    }
}
