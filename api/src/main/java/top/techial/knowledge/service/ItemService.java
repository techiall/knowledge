package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.ItemRepository;
import top.techial.knowledge.domain.Item;

import java.util.Optional;

/**
 * @author techial
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }
}
