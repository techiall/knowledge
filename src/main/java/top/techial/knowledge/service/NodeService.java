package top.techial.knowledge.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageImpl;
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
import top.techial.knowledge.dto.ParentChildDTO;
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
@CacheConfig(cacheNames = "common")
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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public Optional<Node> findByItemIdAndName(String name, Integer itemId) {
        return nodeRepository.findByItemIdAndName(name, itemId);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node save(Node node) {
        return nodeRepository.save(node);
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public Node save(NodeVO nodeVO, Long itemRootNodeId) {
        Node node = NodeMapper.INSTANCE.toNode(nodeVO.setProperty(buildProperty(nodeVO.getProperty())));
        node = nodeRepository.save(node);
        if (nodeVO.getParentId() != null) {
            nodeRelationshipRepository.insertNode(node.getId(), nodeVO.getParentId());
        } else {
            nodeRelationshipRepository.insertNode(node.getId(), itemRootNodeId);
        }
        return node;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Map<String, List<Property.PropertyDTO>> buildProperty(Map<String, List<Property.PropertyDTO>> property) {
        if (property == null || property.values() == null || property.values().isEmpty()) {
            return Collections.emptyMap();
        }
        property.entrySet().forEach(stringListEntry -> {
            List<Long> list = stringListEntry
                    .getValue()
                    .stream()
                    .map(Property.PropertyDTO::getId)
                    .collect(Collectors.toList());
            stringListEntry.setValue(findByIds(list));
        });
        return property;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public List<Property.PropertyDTO> findByIds(List<Long> ids) {
        // language=sql
        String value = "select n.id, n.name from node n where id in (:ids)";
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        return namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(Property.PropertyDTO.class));
    }

    @Transactional
    @CacheEvict(allEntries = true)
    public void deleteByItemId(Integer id) {
        nodeRepository.deleteAllByItemId(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Node findById(Long id) {
        Node node = nodeRepository.findById(id).orElseThrow(() -> new NodeNotFoundException(id));
        node.getProperty().setProperty(buildProperty(node.getProperty().getProperty()));
        return node;
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public List<NodeBaseDTO> findByNameLike(String name, Integer itemId) {
        // language=sql
        String value = "select id, name from node where name like :name and item_id = :itemId\n" +
                "order by update_time desc limit 15;";
        RowMapper<NodeBaseDTO> rowMapper = BeanPropertyRowMapper.newInstance(NodeBaseDTO.class);

        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        map.put("itemId", itemId);
        return namedParameterJdbcTemplate.query(value, map, rowMapper);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0 + #p1", unless = "#result == null")
    public PageImpl<SearchDTO> findContentByNameLike(String name, Pageable pageable) {
        // language=sql
        String value = "select i.name as itemName, n.name as nodeName, u.nick_name as nodeAuthorNickName, n.text as text,\n" +
                "n.id as nodeId, n.labels as labels, n.property as property\n" +
                "from node n inner join item i on n.item_id = i.id inner join user u on i.author_id = u.id\n" +
                "where n.name like :name and i.share = true order by n.update_time desc limit :limit offset :page";

        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        map.put("limit", pageable.getPageSize());
        map.put("page", pageable.getOffset());
        List<SearchDTO> content = namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(SearchDTO.class));

        // language=sql
        value = "select count(*) from node n inner join item i on n.item_id = i.id\n" +
                "inner join user u on i.author_id = u.id\n" +
                "where n.name like :name and i.share = true";
        Long count = namedParameterJdbcTemplate.queryForObject(value, map, Long.class);
        return new PageImpl<>(content, pageable, count == null ? 0 : count);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public List<NodeBaseDTO> findByNameLike(String name) {
        // language=sql
        String value = "select n.id, n.name from node n inner join item i on n.item_id = i.id\n" +
                "where n.name like (:name) and i.share = true order by n.update_time desc limit 10";
        Map<String, Object> map = new HashMap<>();
        map.put("name", '%' + name + '%');
        return namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(NodeBaseDTO.class));
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

        Map<String, Object> map = new HashMap<>();
        map.put("ancestor", id);
        map.put("depth", depth);
        return namedParameterJdbcTemplate.query(value, map, new BeanPropertyRowMapper<>(NodeBaseDTO.class));
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
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id), new BeanPropertyRowMapper<>(ParentChildDTO.class))
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
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("id", id), new BeanPropertyRowMapper<>(ParentChildDTO.class))
                .parallelStream()
                .collect(Collectors.groupingBy(ParentChildDTO::getDescendant));
    }

    private List<NodeBaseDTO> findParent(Long id) {
        // language=sql
        String value = "select node0_.id as id, node0_.name as name, true as child,\n" +
                "(select n.ancestor from node_relationship n where n.descendant = node0_.id and n.distance = 1) as parentNodeId\n" +
                "from `node` node0_ inner join node_relationship noderelati1_ on (node0_.id = noderelati1_.ancestor)\n" +
                "where noderelati1_.descendant = (:descendant)\n";
        return namedParameterJdbcTemplate.query(value, Collections.singletonMap("descendant", id), new BeanPropertyRowMapper<>(NodeBaseDTO.class))
                .parallelStream()
                .filter(it -> it.getParentNodeId() != null)
                .collect(Collectors.toList());
    }

    private NodeTreeDTO depthGetChild(Long id) {
        Map<Long, List<ParentChildDTO>> childNode = buildChildNode(id);
        Map<Long, Node> childNodeData = buildChildNodeData(id);
        Set<Long> flag = new HashSet<>();

        NodeTreeDTO nodeTreeDTO = NodeMapper.INSTANCE.toNodeTreeDTO(childNodeData.get(id));
        List<ParentChildDTO> child = childNode.get(id);

        getChild(nodeTreeDTO, child, flag, childNodeData, childNode);

        return nodeTreeDTO;
    }

    private void getChild(NodeTreeDTO node, List<ParentChildDTO> list, Set<Long> flag, Map<Long, Node> childNodeData, Map<Long, List<ParentChildDTO>> childNode) {
        List<NodeTreeDTO> childList = new ArrayList<>();

        if (list == null) {
            return;
        }
        for (ParentChildDTO parentChildDTO : list) {
            if (flag.contains(parentChildDTO.getDescendant())) {
                continue;
            }
            flag.add(parentChildDTO.getDescendant());
            NodeTreeDTO n = NodeMapper.INSTANCE.toNodeTreeDTO(childNodeData.get(parentChildDTO.getDescendant()));
            getChild(n, childNode.get(parentChildDTO.getDescendant()), flag, childNodeData, childNode);
            childList.add(n);
        }
        node.setChild(childList);
    }

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Map<String, Object> getChildAndParent(Long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("child", depthGetChild(id));
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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
    public Map<String, Object> findByIdGraph(Long id) {

        LinkedList<Node> queue = new LinkedList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        List<Map<String, Object>> nodes = new ArrayList<>();

        // Item root node
        Long rootNode = nodeRepository.findItemRootNodeId(id);


        // relation
        Node node1 = findById(id);
        nodes.add(buildNodes(node1.getId(), node1.getName()));
        node1.getProperty().getProperty().forEach((key, value) -> value.forEach(it -> {
            links.add(buildLinks(node1.getId(), it.getId(), key));
            nodes.add(buildNodes(it.getId(), it.getName()));
        }));

        // child
        Map<Long, List<ParentChildDTO>> childNode = buildChildNode(id);
        Map<Long, Node> childNodeData = buildChildNodeData(id);
        queue.add(childNodeData.get(id));
        while (!queue.isEmpty()) {
            Node first = queue.pollFirst();

            List<ParentChildDTO> list = childNode.get(first.getId());
            if (list == null) {
                continue;
            }
            for (ParentChildDTO it : list) {
                Node child = childNodeData.get(it.getDescendant());
                if (Objects.equals(rootNode, child.getId())) {
                    continue;
                }
                queue.add(child);
                links.add(buildLinks(first.getId(), it.getDescendant(), "child"));
                nodes.add(buildNodes(child.getId(), child.getName()));
            }
        }

        // parent
        Map<Long, Node> parentNodeData = buildParentNodeData(id);
        Map<Long, List<ParentChildDTO>> parentNode = buildParentNode(id);
        queue.add(parentNodeData.get(id));
        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            List<ParentChildDTO> list = parentNode.get(node.getId());
            if (list == null) {
                continue;
            }
            for (ParentChildDTO it : list) {
                Node parent = parentNodeData.get(it.getAncestor());
                if (Objects.equals(rootNode, parent.getId())) {
                    continue;
                }
                queue.add(parent);
                links.add(buildLinks(node.getId(), it.getAncestor(), "parent"));
                nodes.add(buildNodes(parent.getId(), parent.getName()));
            }
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

    @Cacheable(key = "#root.targetClass.simpleName + #root.methodName + #p0", unless = "#result == null")
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
        Map<String, Long> result = new HashMap<>();
        result.put("id", id);
        result.put("target", target);
        // language=sql
        String value = "delete from node_relationship\n" +
                "WHERE descendant IN (select tmp1.descendant from (SELECT d.descendant FROM node_relationship d WHERE d.ancestor = :id) as tmp1)\n" +
                "  and ancestor IN (select tmp2.ancestor from (SELECT a.ancestor FROM node_relationship a WHERE a.descendant = :id AND a.ancestor != a.descendant) as tmp2)\n";
        namedParameterJdbcTemplate.update(value, result);

        // language=sql
        value = "INSERT INTO node_relationship(ancestor, descendant, distance) SELECT super.ancestor, sub.descendant,\n" +
                "CASE WHEN super.ancestor = :target AND sub.descendant = :id THEN :target ELSE 0 END pc\n" +
                "FROM node_relationship super CROSS JOIN node_relationship sub\n" +
                "WHERE super.descendant = :target AND sub.ancestor = :id\n";
        namedParameterJdbcTemplate.update(value, result);
    }
}
