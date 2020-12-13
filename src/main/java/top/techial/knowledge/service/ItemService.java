package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.NodeRepository;
import top.techial.knowledge.repository.UserRepository;
import top.techial.knowledge.repository.search.NodeSearchRepository;
import top.techial.knowledge.web.rest.errors.ItemNotFoundException;
import top.techial.knowledge.web.rest.errors.UserNotFoundException;
import top.techial.knowledge.web.rest.vm.ItemVM;

import java.util.Optional;

/**
 * @author techial
 */
@Service
public class ItemService {

    private final NodeSearchRepository nodeSearchRepository;
    private final ItemRepository itemRepository;
    private final NodeRepository nodeRepository;
    private final NodeService nodeService;
    private final UserRepository userRepository;

    public ItemService(NodeSearchRepository nodeSearchRepository,
                       ItemRepository itemRepository,
                       NodeRepository nodeRepository,
                       NodeService nodeService,
                       UserRepository userRepository) {
        this.nodeSearchRepository = nodeSearchRepository;
        this.itemRepository = itemRepository;
        this.nodeRepository = nodeRepository;
        this.nodeService = nodeService;
        this.userRepository = userRepository;
    }

    public void deleteByUserId(int id) {
        var item = itemRepository.findAllByAuthorId(id);
        item.forEach(it -> itemRepository.deleteTmpByUserIdAndItemId(id, it.getId()));
        itemRepository.deleteByAuthorId(id);
        item.parallelStream().map(Item::getId).forEach(nodeRepository::deleteAllByItemId);
    }

    public Item update(int id, ItemVM itemVM) {
        return itemRepository.findById(id).map(item -> {
            final var itemVMOptional = Optional.of(itemVM);
            itemVMOptional.map(ItemVM::getDescription).ifPresent(item::setDescription);
            itemVMOptional.map(ItemVM::getShare).ifPresent(item::setShare);
            itemVMOptional.map(ItemVM::getImage).ifPresent(item::setImage);
            itemVMOptional.map(ItemVM::getName).ifPresent(item::setName);
            return itemRepository.save(item);
        }).orElseThrow(ItemNotFoundException::new);
    }

    public Item save(int id, Item item) {
        var node = new Node();
        node.setName("root");
        node = nodeService.saveItemRoot(node);

        item.setAuthor(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
        item.setRootNode(node);
        item = itemRepository.save(item);

        node.setItemId(item.getId());
        nodeRepository.save(node);
        itemRepository.insert(id, item.getId());

        return item;
    }

    public void deleteById(int id, int userId) {
        itemRepository.deleteTmpByUserIdAndItemId(userId, id);
        itemRepository.deleteById(id);
        nodeSearchRepository.deleteByItemId(id);
        nodeRepository.deleteAllByItemId(id);
    }
}
