package top.techial.knowledge.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.repository.ItemRepository;

import java.util.List;
import java.util.Optional;

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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Optional<Long> findRootNodeId(Integer id) {
        return itemRepository.findRootNodeId(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Page<Item> findByUserId(Integer id, Pageable pageable) {
        return itemRepository.findAllByAuthorId(id, pageable);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public List<Item> findByUserId(Integer id) {
        return itemRepository.findAllByAuthorId(id);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteByUserId(Integer id) {
        itemRepository.deleteByAuthorId(id);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteById(Integer id) {
        itemRepository.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    public void insert(Integer userId, Integer itemId) {
        itemRepository.insert(userId, itemId);
    }

    @CacheEvict(allEntries = true)
    public void deleteByUserIdAndItemId(Integer id, List<Item> item) {
        item.forEach(item1 -> itemRepository.delete(id, item1.getId()));
    }

    @CacheEvict(allEntries = true)
    public void deleteByUserIdAndItemId(Integer userId, Integer itemId) {
        itemRepository.delete(userId, itemId);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Page<Item> findByShare(Boolean share, Pageable pageable) {
        return itemRepository.findByShare(share, pageable);
    }
}
