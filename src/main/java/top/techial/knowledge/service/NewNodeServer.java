package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.NewNodeRepository;
import top.techial.knowledge.domain.NewNode;

/**
 * @author techial
 */
@Service
@Log4j2
public class NewNodeServer {
    private final NewNodeRepository newNodeRepository;

    public NewNodeServer(NewNodeRepository newNodeRepository) {
        this.newNodeRepository = newNodeRepository;
    }

    public NewNode save(NewNode newNode) {
        return newNodeRepository.save(newNode);
    }

    public Iterable<NewNode> saveAll(Iterable<NewNode> newNodes) {
        return newNodeRepository.saveAll(newNodes);
    }

    public long count() {
        return newNodeRepository.count();
    }

    public NewNode findByTitle(String title) {
        return newNodeRepository.findFirstByTitle(title).orElseThrow(NullPointerException::new);
    }
}
