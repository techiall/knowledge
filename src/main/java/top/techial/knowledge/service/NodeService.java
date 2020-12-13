package top.techial.knowledge.service;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.repository.NodeRelationshipRepository;
import top.techial.knowledge.repository.NodeRepository;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.repository.search.NodeSearchRepository;
import top.techial.knowledge.service.dto.NodeBaseDTO;
import top.techial.knowledge.service.dto.NodeInfoDTO;
import top.techial.knowledge.service.dto.NodeTreeDTO;
import top.techial.knowledge.service.dto.ParentChildDTO;
import top.techial.knowledge.service.mapper.NodeMapper;
import top.techial.knowledge.web.rest.errors.ItemNotFoundException;
import top.techial.knowledge.web.rest.errors.NodeNotFoundException;
import top.techial.knowledge.web.rest.errors.RootNodeException;
import top.techial.knowledge.web.rest.vm.NodeVM;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
public class NodeService {
    private static final Logger log = LoggerFactory.getLogger(NodeService.class);

    private static final String INDEX = "nodes";

    private final NodeRepository nodeRepository;
    private final NodeRelationshipRepository nodeRelationshipRepository;
    private final RecordService recordService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final RecordRepository recordRepository;
    private final NodeSearchRepository nodeSearchRepository;
    private final ItemRepository itemRepository;
    private final NodeMapper nodeMapper;

    public NodeService(NodeRepository nodeRepository,
                       NodeRelationshipRepository nodeRelationshipRepository,
                       RecordService recordService,
                       NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                       ThreadPoolTaskExecutor threadPoolTaskExecutor,
                       RecordRepository recordRepository,
                       NodeSearchRepository nodeSearchRepository,
                       ItemRepository itemRepository,
                       NodeMapper nodeMapper) {
        this.nodeRepository = nodeRepository;
        this.nodeRelationshipRepository = nodeRelationshipRepository;
        this.recordService = recordService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.recordRepository = recordRepository;
        this.nodeSearchRepository = nodeSearchRepository;
        this.itemRepository = itemRepository;
        this.nodeMapper = nodeMapper;
    }

    private static Map<String, Object> buildNodes(Long id, String name) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    private static Map<String, Object> buildLinks(Long sourceId, Long targetId, String relation) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("source", sourceId);
        map.put("target", targetId);
        map.put("relation", relation);
        return map;
    }

    @Transactional
    public Node saveItemRoot(Node node) {
        node = nodeRepository.save(node);
        nodeRelationshipRepository.insertNode(node.getId());
        return node;
    }

    private Node nodeRelationshipSave(NodeVM nodeVM, Long itemRootNodeId) {
        nodeVM.setProperty(buildProperty(nodeVM.getProperty()));
        var node = nodeMapper.toNode(nodeVM);
        node = nodeRepository.save(node);
        if (nodeVM.getParentId() != null) {
            nodeRelationshipRepository.insertNode(node.getId(), nodeVM.getParentId());
        } else {
            nodeRelationshipRepository.insertNode(node.getId(), itemRootNodeId);
        }
        return node;
    }

    private Map<String, List<Property.PropertyDTO>> buildProperty(Map<String, List<Property.PropertyDTO>> property) {
        if (property == null || property.values() == null || property.values().isEmpty()) {
            return Collections.emptyMap();
        }
        property.entrySet().forEach(stringListEntry -> {
            List<Long> list = stringListEntry
                    .getValue()
                    .stream()
                    .filter(propertyDTO -> propertyDTO != null && propertyDTO.getId() != null)
                    .map(Property.PropertyDTO::getId)
                    .collect(Collectors.toList());
            stringListEntry.setValue(findByIds(list));
        });
        return property;
    }

    public List<Property.PropertyDTO> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        // language=sql
        var value = "select n.id, n.name from node n where id in (:ids)";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        return namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(Property.PropertyDTO.class));
    }

    public Node findById(Long id) {
        itemRepository.findItemIdByRootId(id).orElseThrow(RootNodeException::new);
        Optional<Node> node = nodeRepository.findById(id);
        node.map(Node::getProperty).ifPresent(it -> it.setProperty(buildProperty(it.getProperty())));
        return node.orElseThrow(NodeNotFoundException::new);
    }

    public List<NodeInfoDTO> findByNameLike(String name, int itemId) {
        var searchQuery = new NativeSearchQueryBuilder()
                .withIndices(INDEX)
                .withQuery(QueryBuilders.boolQuery()
                                        .must(QueryBuilders.matchQuery("name", name))
                                        .must(QueryBuilders.termQuery("itemId", itemId))
                )
                .withHighlightFields(new HighlightBuilder.Field(name))
                .withPageable(PageRequest.of(0, 10))
                .build();
        return nodeSearchRepository.search(searchQuery).map(nodeMapper::toNodeInfoDTO)
                                   .getContent();
    }

    public Page<Node> findContentByNameLike(String name, Pageable pageable) {
        var itemIds = itemRepository.findByShare(true)
                                    .parallelStream()
                                    .map(Item::getId)
                                    .collect(Collectors.toList());

        var searchQuery = new NativeSearchQueryBuilder()
                .withIndices(INDEX)
                .withQuery(QueryBuilders.boolQuery()
                                        .must(QueryBuilders.matchQuery("name", name))
                                        .must(QueryBuilders.termsQuery("itemId", itemIds))
                )
                .withHighlightFields(new HighlightBuilder.Field(name))
                .withPageable(pageable)
                .build();
        return nodeSearchRepository.search(searchQuery);
    }

    public List<NodeBaseDTO> findByChildNode(long id, int depth) {
        // language=sql
        var value = "select noderelati1_.ancestor as parentNodeId, noderelati1_.descendant as id,\n" +
                "(select count(*) - 1 from node_relationship k where k.ancestor = noderelati1_.descendant) as child,\n" +
                "node0_.`name` as name\n" +
                "from `node` node0_ inner join node_relationship noderelati1_ on (node0_.id = noderelati1_.descendant)\n" +
                "where noderelati1_.ancestor = (:ancestor) " +
                "and noderelati1_.ancestor != noderelati1_.descendant and noderelati1_.distance <= (:depth)";

        Map<String, Object> map = new HashMap<>();
        map.put("ancestor", id);
        map.put("depth", depth);
        return namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(NodeBaseDTO.class));
    }

    private Map<Long, Node> buildChildNodeData(long id) {
        return nodeRepository.findChildNode(id)
                             .parallelStream()
                             .collect(Collectors.toMap(Node::getId, Function.identity()));
    }

    private Map<Long, Node> buildParentNodeData(long id) {
        return nodeRepository.findParentNode(id)
                             .parallelStream()
                             .collect(Collectors.toMap(Node::getId, Function.identity()));
    }

    private Map<Long, List<ParentChildDTO>> buildChildNode(long id) {
        // language=sql
        var value = "select nr.descendant as descendant, nr.ancestor as ancestor\n" +
                "from node_relationship as nr inner join (select n_child.id\n" +
                "from node n_child join node_relationship nr_child on (n_child.id = nr_child.descendant)\n" +
                "where nr_child.ancestor = (:id)) as node on nr.descendant = node.id\n" +
                "where nr.distance = 1 and nr.descendant != (:id)";
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id),
                                                new BeanPropertyRowMapper<>(ParentChildDTO.class))
                                         .parallelStream()
                                         .collect(Collectors.groupingBy(ParentChildDTO::getAncestor));
    }

    private Map<Long, List<ParentChildDTO>> buildParentNode(long id) {
        // language=sql
        var value = "select nr.descendant as descendant, nr.ancestor as ancestor\n" +
                "from node_relationship as nr inner join (select n_child.id\n" +
                "from node n_child join node_relationship nr_child on (n_child.id = nr_child.ancestor)\n" +
                "where nr_child.descendant = (:id)) as node on nr.descendant = node.id\n" +
                "where nr.distance = 1";
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id),
                                                new BeanPropertyRowMapper<>(ParentChildDTO.class))
                                         .parallelStream()
                                         .collect(Collectors.groupingBy(ParentChildDTO::getDescendant));
    }

    private List<NodeBaseDTO> findParent(long id) {
        // language=sql
        var value = "select node0_.id as id, node0_.name as name, true as child,\n" +
                "(select n.ancestor from node_relationship n where n.descendant = node0_.id and n.distance = 1) as parentNodeId\n" +
                "from `node` node0_ inner join node_relationship noderelati1_ on (node0_.id = noderelati1_.ancestor)\n" +
                "where noderelati1_.descendant = (:descendant)\n";
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("descendant", id),
                                                new BeanPropertyRowMapper<>(NodeBaseDTO.class))
                                         .parallelStream()
                                         .filter(it -> it.getParentNodeId() != null)
                                         .collect(Collectors.toList());
    }

    private NodeTreeDTO depthGetChild(long id) {
        Map<Long, List<ParentChildDTO>> childNode = buildChildNode(id);
        Map<Long, Node> childNodeData = buildChildNodeData(id);
        Set<Long> flag = new HashSet<>();

        var nodeTreeDTO = nodeMapper.toNodeTreeDTO(childNodeData.get(id));
        List<ParentChildDTO> child = childNode.get(id);

        getChild(nodeTreeDTO, child, flag, childNodeData, childNode);

        return nodeTreeDTO;
    }

    private void getChild(NodeTreeDTO node,
                          List<ParentChildDTO> list, Set<Long> flag,
                          Map<Long, Node> childNodeData,
                          Map<Long, List<ParentChildDTO>> childNode) {
        List<NodeTreeDTO> childList = new ArrayList<>();

        if (list == null) {
            return;
        }
        for (ParentChildDTO parentChildDTO : list) {
            if (flag.contains(parentChildDTO.getDescendant())) {
                continue;
            }
            flag.add(parentChildDTO.getDescendant());
            NodeTreeDTO n = nodeMapper.toNodeTreeDTO(childNodeData.get(parentChildDTO.getDescendant()));
            getChild(n, childNode.get(parentChildDTO.getDescendant()), flag, childNodeData, childNode);
            childList.add(n);
        }
        node.setChild(childList);
    }

    public Map<String, Object> getChildAndParent(long id) throws ExecutionException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        Future<NodeTreeDTO> child = threadPoolTaskExecutor.submit(() -> depthGetChild(id));
        Future<List<NodeBaseDTO>> parent = threadPoolTaskExecutor.submit(() -> findParent(id));
        map.put("child", child.get());
        map.put("parent", parent.get());
        return map;
    }

    /**
     * relation
     */
    private void nodeRelation(long id,
                              List<Map<String, Object>> links,
                              List<Map<String, Object>> nodes) {
        // relation
        var node1 = findById(id);
        nodes.add(buildNodes(node1.getId(), node1.getName()));
        node1.getProperty().getProperty().forEach((key, value) -> value.forEach(it -> {
            links.add(buildLinks(node1.getId(), it.getId(), key));
            nodes.add(buildNodes(it.getId(), it.getName()));
        }));
    }

    /**
     * child
     */
    private void nodeChild(long id,
                           LinkedList<Node> queue,
                           long rootNode,
                           List<Map<String, Object>> links,
                           List<Map<String, Object>> nodes) {
        Map<Long, List<ParentChildDTO>> childNode = buildChildNode(id);
        Map<Long, Node> childNodeData = buildChildNodeData(id);
        queue.add(childNodeData.get(id));
        while (!queue.isEmpty()) {
            var first = queue.pollFirst();

            List<ParentChildDTO> list = childNode.get(first.getId());
            if (list == null) {
                continue;
            }
            for (ParentChildDTO it : list) {
                var child = childNodeData.get(it.getDescendant());
                if (Objects.equals(rootNode, child.getId())) {
                    continue;
                }
                queue.add(child);
                links.add(buildLinks(first.getId(), it.getDescendant(), "child"));
                nodes.add(buildNodes(child.getId(), child.getName()));
            }
        }
    }

    /**
     * node parent
     */
    private void nodeParent(long id,
                            LinkedList<Node> queue,
                            long rootNode,
                            List<Map<String, Object>> links,
                            List<Map<String, Object>> nodes) {
        Map<Long, Node> parentNodeData = buildParentNodeData(id);
        Map<Long, List<ParentChildDTO>> parentNode = buildParentNode(id);
        queue.add(parentNodeData.get(id));
        while (!queue.isEmpty()) {
            var node = queue.pollFirst();
            List<ParentChildDTO> list = parentNode.get(node.getId());
            if (list == null) {
                continue;
            }
            for (ParentChildDTO it : list) {
                var parent = parentNodeData.get(it.getAncestor());
                if (Objects.equals(rootNode, parent.getId())) {
                    continue;
                }
                queue.add(parent);
                links.add(buildLinks(node.getId(), it.getAncestor(), "parent"));
                nodes.add(buildNodes(parent.getId(), parent.getName()));
            }
        }
    }

    public Map<String, Object> findByIdGraph(long id) {

        LinkedList<Node> queue = new LinkedList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        List<Map<String, Object>> nodes = new ArrayList<>();

        // Item root node
        var rootNode = nodeRepository.findItemRootNodeId(id);

        List<Future<?>> futures = new ArrayList<>();
        // relation
        futures.add(threadPoolTaskExecutor.submit(() -> nodeRelation(id, links, nodes)));

        // child
        futures.add(threadPoolTaskExecutor.submit(() -> nodeChild(id, queue, rootNode, links, nodes)));

        // parent
        futures.add(threadPoolTaskExecutor.submit(() -> nodeParent(id, queue, rootNode, links, nodes)));

        futures.parallelStream().forEach(it -> {
            try {
                it.get();
            } catch (InterruptedException | ExecutionException e) {
                if (log.isErrorEnabled()) {
                    log.error("", e);
                }
                Thread.currentThread().interrupt();
            }
        });

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    public void deleteIdsAndRelationship(Set<Long> ids) {
        nodeRepository.deleteByIdIn(ids);
        if (!ids.isEmpty()) {
            nodeRelationshipRepository.deleteByNodeIdIn(ids);
        }
    }

    public void move(long id, long target) {
        Map<String, Long> result = new HashMap<>();
        result.put("id", id);
        result.put("target", target);
        // language=sql
        var value = "delete from node_relationship\n" +
                "WHERE descendant IN (select tmp1.descendant from " +
                "(SELECT d.descendant FROM node_relationship d WHERE d.ancestor = :id) as tmp1)\n" +
                "  and ancestor IN (select tmp2.ancestor from (SELECT a.ancestor FROM node_relationship a " +
                "WHERE a.descendant = :id AND a.ancestor != a.descendant) as tmp2)\n";
        namedParameterJdbcTemplate.update(value, result);

        // language=sql
        value = "INSERT INTO node_relationship(ancestor, descendant, distance) SELECT super.ancestor, sub.descendant,\n" +
                "CASE WHEN super.ancestor = :target AND sub.descendant = :id THEN :target ELSE 0 END pc\n" +
                "FROM node_relationship super CROSS JOIN node_relationship sub\n" +
                "WHERE super.descendant = :target AND sub.ancestor = :id\n";
        namedParameterJdbcTemplate.update(value, result);
    }

    public Node update(long id, NodeVM nodeVM, int userId) {
        final Node node = findById(id);
        Optional<NodeVM> vmOptional = Optional.of(nodeVM);

        vmOptional.map(NodeVM::getName).filter(it -> !it.isEmpty()).ifPresent(it -> {
            node.setName(nodeVM.getName());
            recordService.save(node.getId(), userId,
                               nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        });

        vmOptional.map(NodeVM::getLabels).ifPresent(it -> {
            Labels labels = new Labels();
            labels.setLabels(nodeVM.getLabels());
            node.setLabels(labels);
            recordService.save(node.getId(), userId, nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        });

        vmOptional.map(NodeVM::getProperty).ifPresent(it -> {
            Property property = new Property();
            property.setProperty(buildProperty(nodeVM.getProperty()));
            node.setProperty(property);
            recordService.save(node.getId(), userId, nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
        });

        var tmp = nodeRepository.save(node);
        nodeSearchRepository.index(tmp);
        return node;
    }

    @Transactional
    public Map<String, Object> save(int id, NodeVM nodeVM) {
        var itemId = itemRepository.findRootNodeId(nodeVM.getItemId())
                                   .orElseThrow(ItemNotFoundException::new);

        var node = nodeRepository.findByItemIdAndName(nodeVM.getName().trim(), nodeVM.getItemId())
                                 .orElse(null);
        boolean flag = false;
        if (node == null) {
            node = nodeRelationshipSave(nodeVM, itemId);
            nodeSearchRepository.index(node);
            recordService.save(node.getId(), id,
                               nodeVM.getRecord().getOperator(), nodeVM.getRecord().getMessage());
            flag = true;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("node", nodeMapper.toNodeInfoDTO(node));
        map.put("new", flag);
        return map;
    }

    private void deleteIdAndRelationship(long id) {
        itemRepository.findItemIdByRootId(id).orElseThrow(RootNodeException::new);
        nodeRepository.deleteById(id);
        nodeRelationshipRepository.deleteByNodeId(id);
    }

    public void deleteById(long id) {
        deleteIdAndRelationship(id);
        nodeSearchRepository.deleteById(id);
        recordRepository.deleteByNodeId(id);
    }
}
