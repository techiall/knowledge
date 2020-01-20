package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.ItemRepository;
import top.techial.knowledge.domain.Item;

import java.util.List;
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

    public List<Item> findByUserId(Integer id) {
        return itemRepository.findAllByAuthorId(id);
    }

    public void deleteByUserId(Integer id) {
        itemRepository.deleteByAuthorId(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public void deleteById(Integer id) {
        itemRepository.deleteById(id);
    }

    public void insert(Integer userId, Integer itemId) {
        itemRepository.insert(userId, itemId);
    }
}
