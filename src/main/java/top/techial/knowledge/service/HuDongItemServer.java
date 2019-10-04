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

    public long count() {
        return huDongItemRepository.count();
    }

    public HuDongItem save(HuDongItem huDongItems) {
        return huDongItemRepository.save(huDongItems);
    }

    public void deleteAll() {
        huDongItemRepository.deleteAll();
    }

    public HuDongItem findById(Long id) {
        return huDongItemRepository.findById(id).orElseThrow(NullPointerException::new);
    }

    public HuDongItem findByTitle(String title) {
        return huDongItemRepository.findFirstByTitle(title).orElseThrow(NullPointerException::new);
    }

    public Iterable<HuDongItem> saveAll(Iterable<HuDongItem> huDongItems) {
        return huDongItemRepository.saveAll(huDongItems);
    }
}
