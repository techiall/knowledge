package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.vo.NodeVO;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeServiceTest {

    @Autowired
    private NodeService nodeService;

    @Test
    public void test() {
        nodeService.findByChildNode(28L, 3).forEach(System.out::println);
    }

    @Test
    public void save() {
        NodeVO nodeVO = new NodeVO()
                .setName("Java程序开发基础");
        Node node = nodeService.save(nodeVO);

        NodeVO nodeVO1 = new NodeVO()
                .setName("Java 开发简介")
                .setParentId(node.getId());
        Node node1 = nodeService.save(nodeVO1);

        NodeVO nodeVO4 = new NodeVO()
                .setName("Java语言简介")
                .setParentId(node1.getId());
        nodeService.save(nodeVO4);

        NodeVO nodeVO2 = new NodeVO()
                .setName("类和对象")
                .setParentId(node.getId());
        nodeService.save(nodeVO2);

        NodeVO nodeVO3 = new NodeVO()
                .setName("继承和多态")
                .setParentId(node.getId());
        nodeService.save(nodeVO3);


    }

    @Test
    public void getChildAndParent() {
        System.out.println(nodeService.getChildAndParent(30L, 4));
    }
}