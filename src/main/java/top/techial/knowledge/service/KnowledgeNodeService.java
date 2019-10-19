package top.techial.knowledge.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.KnowledgeNodeRelationRepository;
import top.techial.knowledge.dao.KnowledgeNodeRepository;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.dto.KnowledgeNodeDTO;
import top.techial.knowledge.dto.LinksDTO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
public class KnowledgeNodeService {
    private final KnowledgeNodeRepository knowledgeNodeRepository;
    private final KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    public KnowledgeNodeService(KnowledgeNodeRepository knowledgeNodeRepository, KnowledgeNodeRelationRepository knowledgeNodeRelationRepository) {
        this.knowledgeNodeRepository = knowledgeNodeRepository;
        this.knowledgeNodeRelationRepository = knowledgeNodeRelationRepository;
    }

    public Iterable<KnowledgeNode> save(Iterable<KnowledgeNode> iterable) {
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
        KnowledgeNode node = knowledgeNodeRepository.findById(id).orElseThrow(NullPointerException::new);
        Map<String, Object> result = new HashMap<>(16);
        List<KnowledgeNodeDTO> list = new ArrayList<>();
        list.add(new KnowledgeNodeDTO(node));
        node.getRelations().stream().map(KnowledgeNodeDTO::new).forEach(list::add);
        result.put("nodes", list);
        List<LinksDTO> linksDTOS = new ArrayList<>();
        for (KnowledgeNode knowledgeNode : node.getRelations()) {
            linksDTOS.addAll(buildLinks(node, knowledgeNode));
        }
        result.put("links", linksDTOS);
        return result;
    }

    public List<LinksDTO> buildLinks(KnowledgeNode begin, KnowledgeNode end) {
        return knowledgeNodeRelationRepository.findByStartNodeIdAndEndNodeId(begin.getId(), end.getId()).stream()
            .map(it -> it.getProperty().entrySet().stream().map(
                map -> new LinksDTO(begin, end, Collections.singletonMap(map.getKey(), map.getValue()))))
            .flatMap(Function.identity()).collect(Collectors.toList());
    }

    public Page<KnowledgeNode> findByName(String name, Pageable pageable) {
        return knowledgeNodeRepository.findByNameLike(name, pageable);
    }

    public void deleteAll() {
        knowledgeNodeRepository.deleteAll();
    }
}
