package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.dao.KnowledgeNodeRepository;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeRelationService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    public KnowledgeNodeRelationService(KnowledgeNodeRepository knowledgeNodeRepository, KnowledgeNodeRelationRepository knowledgeNodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.knowledgeNodeRelationRepository = knowledgeNodeRelationRepository;
    }

}
