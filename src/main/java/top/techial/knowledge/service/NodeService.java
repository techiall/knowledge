package top.techial.knowledge.service;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.techial.knowledge.dao.NodeRelationshipRepository;
import top.techial.knowledge.dao.NodeRepository;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeTreeDTO;
import top.techial.knowledge.dto.SearchDTO;
import top.techial.knowledge.exception.NodeNotFoundException;
import top.techial.knowledge.mapper.NodeMapper;
import top.techial.knowledge.vo.NodeVO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Service
@Log4j2
@CacheConfig(cacheNames = "node")
public class NodeService {
    private final NodeRepository nodeRepository;
    private final NodeRelationshipRepository nodeRelationshipRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public NodeService(
            NodeRepository nodeRepository,
            NodeRelationshipRepository nodeRelationshipRepository,
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        this.nodeRepository = nodeRepository;
        this.nodeRelationshipRepository = nodeRelationshipRepository;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node saveItemRoot(Node node) {
        node = nodeRepository.save(node);
        nodeRelationshipRepository.insertNode(node.getId());
        return node;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node save(Node node) {
        return nodeRepository.save(node);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node save(NodeVO nodeVO, Long itemRootNodeId) {
        Node node = NodeMapper.INSTANCE.toNode(nodeVO);
        node = nodeRepository.save(node);
        if (nodeVO.getParentId() != null) {
            nodeRelationshipRepository.insertNode(node.getId(), nodeVO.getParentId());
        } else {
            nodeRelationshipRepository.insertNode(node.getId(), itemRootNodeId);
        }
        return node;
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteByItemId(Integer id) {
        nodeRepository.deleteAllByItemId(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Node findById(Long id) {
        return nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public List<NodeBaseDTO> findByNameLike(String name, Integer itemId) {
        // language=sql
        String value = "select id, name from node where name like (:name) and item_id = (:itemId)" +
                "order by update_time desc limit 15;";
        RowMapper<NodeBaseDTO> rowMapper = BeanPropertyRowMapper.newInstance(NodeBaseDTO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        map.put("itemId", itemId);
        return namedParameterJdbcTemplate.query(value, map, rowMapper);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public List<SearchDTO> findContentByNameLike(String name, Pageable pageable) {
        // language=sql
        String value = "select i.name as itemName, n.name as nodeName, u.nick_name as nodeAuthorNickName, n.text as text,\n" +
                "n.id as nodeId, n.labels as labels, n.property as property\n" +
                "from node n inner join item i on n.item_id = i.id inner join user u on i.author_id = u.id\n" +
                "where n.name like :name and i.share = true order by n.update_time desc limit :limit offset :page";
        BeanPropertyRowMapper<SearchDTO> rowMapper = BeanPropertyRowMapper.newInstance(SearchDTO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        map.put("limit", pageable.getPageSize());
        map.put("page", pageable.getOffset());
        return namedParameterJdbcTemplate.query(value, map, rowMapper);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public List<NodeBaseDTO> findByNameLike(String name) {
        // language=sql
        String value = "select n.id, n.name from node n inner join item i on n.item_id = i.id\n" +
                "where n.name like (:name) and i.share = true order by n.update_time desc limit 10";
        RowMapper<NodeBaseDTO> rowMapper = BeanPropertyRowMapper.newInstance(NodeBaseDTO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        return namedParameterJdbcTemplate.query(value, map, rowMapper);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public List<NodeBaseDTO> findByChildNode(Long id, int depth) {
        // language=sql
        String value = "select noderelati1_.ancestor as parentNodeId, noderelati1_.descendant as id,\n" +
                "(select count(*) - 1 from node_relationship k where k.ancestor = noderelati1_.descendant) as child,\n" +
                "node0_.`name` as name\n" +
                "from `node` node0_ inner join node_relationship noderelati1_ on (node0_.id = noderelati1_.descendant)\n" +
                "where noderelati1_.ancestor = (:ancestor) " +
                "and noderelati1_.ancestor != noderelati1_.descendant and noderelati1_.distance <= (:depth)";
        RowMapper<NodeBaseDTO> rowMapper = BeanPropertyRowMapper.newInstance(NodeBaseDTO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("ancestor", id);
        map.put("depth", depth);
        return namedParameterJdbcTemplate.query(value, map, rowMapper);
    }

    @Data
    @Accessors(chain = true)
    public static class ParentChildDTO {
        private Long descendant;
        private Long ancestor;
    }

    private Map<Long, Node> buildChildNodeData(Long id) {
        return nodeRepository.findChildNode(id)
                .parallelStream()
                .collect(Collectors.toMap(Node::getId, Function.identity()));
    }

    private Map<Long, Node> buildParentNodeData(Long id) {
        return nodeRepository.findParentNode(id)
                .parallelStream()
                .collect(Collectors.toMap(Node::getId, Function.identity()));
    }

    private Map<Long, List<ParentChildDTO>> buildChildNode(Long id) {
        // language=sql
        String value = "select nr.descendant as descendant, nr.ancestor as ancestor\n" +
                "from node_relationship as nr inner join (select n_child.id\n" +
                "from node n_child join node_relationship nr_child on (n_child.id = nr_child.descendant)\n" +
                "where nr_child.ancestor = (:id)) as node on nr.descendant = node.id\n" +
                "where nr.distance = 1 and nr.descendant != (:id)";
        RowMapper<ParentChildDTO> rowMapper = BeanPropertyRowMapper.newInstance(ParentChildDTO.class);
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id), rowMapper)
                .parallelStream()
                .collect(Collectors.groupingBy(ParentChildDTO::getAncestor));
    }

    private Map<Long, List<ParentChildDTO>> buildParentNode(Long id) {
        // language=sql
        String value = "select nr.descendant as descendant, nr.ancestor as ancestor\n" +
                "from node_relationship as nr inner join (select n_child.id\n" +
                "from node n_child join node_relationship nr_child on (n_child.id = nr_child.ancestor)\n" +
                "where nr_child.descendant = (:id)) as node on nr.descendant = node.id\n" +
                "where nr.distance = 1";
        RowMapper<ParentChildDTO> rowMapper = BeanPropertyRowMapper.newInstance(ParentChildDTO.class);
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id), rowMapper)
                .parallelStream()
                .collect(Collectors.groupingBy(ParentChildDTO::getDescendant));
    }

    private List<NodeBaseDTO> findParent(Long id) {
        // language=sql
        String value = "select node0_.id as id, node0_.name as name, true as child,\n" +
                "(select n.ancestor from node_relationship n where n.descendant = node0_.id and n.distance = 1) as parentNodeId\n" +
                "from `node` node0_ inner join node_relationship noderelati1_ on (node0_.id = noderelati1_.ancestor)\n" +
                "where noderelati1_.descendant = (:descendant)\n";
        RowMapper<NodeBaseDTO> rowMapper = BeanPropertyRowMapper.newInstance(NodeBaseDTO.class);
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("descendant", id), rowMapper)
                .parallelStream()
                .filter(it -> it.getParentNodeId() != null)
                .collect(Collectors.toList());
    }

    public void depthGetChild(Long id) {
        Stack<NodeTreeDTO> nodeStack = new Stack<>();
    }

    private void getChild(Long id) {
        Map<Long, List<ParentChildDTO>> result = buildChildNode(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Map<String, List<NodeBaseDTO>> getChildAndParent(Long id, int depth) {
        Map<String, List<NodeBaseDTO>> map = new HashMap<>();
        map.put("child", findByChildNode(id, depth));
        map.put("parent", findParent(id));
        return map;
    }

    private Map<String, Object> buildNodes(Long id, String name) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    private Map<String, Object> buildLinks(Long sourceId, Long targetId, String relation) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("source", sourceId);
        map.put("target", targetId);
        map.put("relation", relation);
        return map;
    }

    //    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Map<String, Object> findByIdGraph(Long id) {

        LinkedList<Node> queue = new LinkedList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        List<Map<String, Object>> nodes = new ArrayList<>();

        Map<Long, List<ParentChildDTO>> childNode = buildChildNode(id);
        Map<Long, Node> childNodeData = buildChildNodeData(id);
        queue.add(childNodeData.get(id));
        while (!queue.isEmpty()) {
            Node first = queue.pollFirst();

            nodes.add(buildNodes(first.getId(), first.getName()));

            // child
            List<ParentChildDTO> list = childNode.get(first.getId());
            if (list == null) {
                continue;
            }
            list.forEach(it -> {
                Node child = childNodeData.get(it.getDescendant());
                queue.add(child);
                links.add(buildLinks(first.getId(), it.getDescendant(), "child"));
                nodes.add(buildNodes(child.getId(), child.getName()));
            });

            // relation
            if (first.getProperty() == null) {
                continue;
            }

            Map<String, List<Property.PropertyDTO>> stringListMap = first.getProperty().getProperty();

            if (stringListMap == null) {
                continue;
            }

            Set<Map.Entry<String, List<Property.PropertyDTO>>> entrySet = stringListMap.entrySet();

            for (Map.Entry<String, List<Property.PropertyDTO>> entry : entrySet) {
                for (Property.PropertyDTO it : entry.getValue()) {
                    if (it.getId() == null) {
                        continue;
                    }
                    links.add(buildLinks(first.getId(), it.getId(), entry.getKey()));
                    nodes.add(buildNodes(it.getId(), it.getName()));
                }
            }
        }

        Map<Long, Node> parentNodeData = buildParentNodeData(id);
        Map<Long, List<ParentChildDTO>> parentNode = buildParentNode(id);
        queue.add(parentNodeData.get(id));
        while (!queue.isEmpty()) {
            // parent
            Node node = queue.pollFirst();
            List<ParentChildDTO> list = parentNode.get(node.getId());
            if (list == null) {
                continue;
            }
            list.forEach(it -> {
                Node parent = parentNodeData.get(it.getAncestor());
                queue.add(parent);
                links.add(buildLinks(node.getId(), it.getAncestor(), "parent"));
                nodes.add(buildNodes(parent.getId(), parent.getName()));
            });
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);
        return result;
    }

    @CacheEvict(allEntries = true)
    public void deleteIdsAndRelationship(Set<Long> ids) {
        nodeRepository.deleteByIdIn(ids);
        nodeRelationshipRepository.deleteByNodeIdIn(ids);
    }

    @CacheEvict(allEntries = true)
    public void deleteIdAndRelationship(Long id) {
        nodeRepository.deleteById(id);
        nodeRelationshipRepository.deleteByNodeId(id);
    }

    public List<Long> findByItemIds(Collection<Integer> ids) {
        return nodeRepository.findByItemIdIn(ids);
    }

    @CacheEvict(allEntries = true)
    public void saveText(String text, Long id) {
        nodeRepository.saveText(id, text);
    }

    public String findText(Long id) {
        return nodeRepository.findTextById(id);
    }

    @CacheEvict(allEntries = true)
    public void move(Long id, Long target) {

    }
}
