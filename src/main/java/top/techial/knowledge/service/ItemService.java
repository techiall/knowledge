package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.NodeRepository;

import java.util.List;

/**
 * @author techial
 */
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final NodeRepository nodeRepository;


    public ItemService(ItemRepository itemRepository, NodeRepository nodeRepository) {
        this.itemRepository = itemRepository;
        this.nodeRepository = nodeRepository;
    }

    public void deleteByUserId(Integer id) {
        List<Item> item = itemRepository.findAllByAuthorId(id);
        item.forEach(item1 -> itemRepository.deleteTmpByUserIdAndItemId(id, item1.getId()));
        itemRepository.deleteByAuthorId(id);
        item.parallelStream().map(Item::getId).forEach(nodeRepository::deleteAllByItemId);
    }

}
