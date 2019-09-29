package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.HuDongItemRepository;
import top.techial.knowledge.domain.HuDongItem;

/**
 * @author techial
 */
@Service
public class HuDongItemServer {
    private final HuDongItemRepository huDongItemRepository;

    public HuDongItemServer(HuDongItemRepository huDongItemRepository) {
        this.huDongItemRepository = huDongItemRepository;
    }

    public HuDongItem save(HuDongItem huDongItems) {
        return huDongItemRepository.save(huDongItems);
    }
}
