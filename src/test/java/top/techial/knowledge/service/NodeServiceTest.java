package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.exception.ItemException;
import top.techial.knowledge.vo.NodeVO;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeServiceTest {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ItemService itemService;

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
}
