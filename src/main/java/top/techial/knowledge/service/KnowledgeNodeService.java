package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.domain.KnowledgeNode;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
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
}
