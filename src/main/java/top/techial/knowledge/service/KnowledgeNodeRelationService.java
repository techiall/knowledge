package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;
import top.techial.knowledge.vo.RelationVO;

import java.util.List;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeRelationService {
    private final KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;
    private final KnowledgeNodeRepository knowledgeNodeRepository;


    public KnowledgeNodeRelationService(KnowledgeNodeRelationRepository knowledgeNodeRelationRepository, KnowledgeNodeRepository knowledgeNodeRepository) {
        this.knowledgeNodeRelationRepository = knowledgeNodeRelationRepository;
        this.knowledgeNodeRepository = knowledgeNodeRepository;
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

    public KnowledgeNodeRelation save(RelationVO relationVO) {
        KnowledgeNode startNode = knowledgeNodeRepository.findFirstByName(relationVO.getStartNode()).orElseThrow(NullPointerException::new);
        KnowledgeNode endNode = knowledgeNodeRepository.findFirstByName(relationVO.getEndNodeName()).orElseThrow(NullPointerException::new);
        return knowledgeNodeRelationRepository.save(new KnowledgeNodeRelation()
            .setStartNode(startNode)
            .setEndNode(endNode)
            .setProperty(relationVO.getProperty()));
    }

    public KnowledgeNodeRelation updateById(Long id, RelationVO relationVO) {
        return knowledgeNodeRelationRepository.findById(id).orElseThrow(NullPointerException::new)
            .setProperty(relationVO.getProperty());
    }

    public void deleteById(Long id) {
        knowledgeNodeRelationRepository.deleteById(id);
    }
}
