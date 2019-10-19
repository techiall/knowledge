package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
    }
}
