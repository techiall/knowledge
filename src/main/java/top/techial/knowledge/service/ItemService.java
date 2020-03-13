package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.repository.ItemRepository;

import java.util.List;

/**
 * @author techial
 */
@CacheConfig(cacheNames = "common")
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @CacheEvict(allEntries = true)
    public void deleteByUserIdAndItemId(Integer id, List<Item> item) {
        item.forEach(item1 -> itemRepository.deleteTmpByUserIdAndItemId(id, item1.getId()));
    }

}
