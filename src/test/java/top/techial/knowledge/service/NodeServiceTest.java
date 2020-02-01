package top.techial.knowledge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.vo.NodeVO;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeServiceTest {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void save() {
        NodeVO nodeVO = new NodeVO()
                .setName("Java程序开发基础")
                .setLabels(new HashSet<>(Arrays.asList(
                        "Java开发的实用基础知识",
                        "丰富的代码示例",
                        "清晰的讲解图例",
                        "大量的编程练习"
                )))
                .setItemId(2);
        Long itemId = itemService.findRootNodeId(nodeVO.getItemId())
                .orElseThrow(() -> new ItemException(nodeVO.getItemId()));
        nodeService.save(nodeVO, itemId);
    }


    @Test
    public void deleteIdAndRelationship() {
        nodeService.deleteIdAndRelationship(129L);
    }

    @Test
    public void findByNameLike() {
        List<NodeBaseDTO> result = nodeService.findByNameLike("java");
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void findContentByNameLike() {
        nodeService.findContentByNameLike("java", PageRequest.of(0, 10))
                .forEach(System.out::println);
    }

    @Test
    @SneakyThrows
    public void findByIdGraph() {
        System.out.println(objectMapper.writeValueAsString(nodeService.findByIdGraph(123L)));
    }

    @Test
    public void findByIds() {
        System.out.println(nodeService.findByIds(Collections.singletonList(123L)));
    }

    @Test
    public void depthGetChild() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(nodeService.getChildAndParent(153L)));
    }
}
