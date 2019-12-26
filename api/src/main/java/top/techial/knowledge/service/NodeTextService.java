package top.techial.knowledge.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.NodeTextRepository;
import top.techial.knowledge.domain.NodeText;

/**
 * @author techial
 */
@Service
public class NodeTextService {
    private final NodeTextRepository nodeTextRepository;

    public NodeTextService(NodeTextRepository nodeTextRepository) {
        this.nodeTextRepository = nodeTextRepository;
    }

    public NodeText save(NodeText nodeText) {
        return nodeTextRepository.save(nodeText);
    }

    @Async
    public void save(String text, Long id) {
        NodeText nodeText = nodeTextRepository.findById(id).orElse(new NodeText());
        nodeText = nodeText.setId(id).setText(text);
        nodeTextRepository.save(nodeText);
    }

    public NodeText findById(Long id) {
        return nodeTextRepository.findById(id).orElse(new NodeText());
    }
}
