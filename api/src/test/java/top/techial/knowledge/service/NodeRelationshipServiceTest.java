package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.NodeRelationship;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeRelationshipServiceTest {

    @Autowired
    private NodeRelationshipService nodeRelationshipService;

    @Test
    public void save() {
        NodeRelationship nodeRelationship = new NodeRelationship()
                .setAncestor(1L)
                .setDescendant(1L)
                .setDistance(1L);
        nodeRelationshipService.save(nodeRelationship);
    }

}