package top.techial.knowledge.repository;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeRelationshipRepositoryTest {
    @Autowired
    private NodeRelationshipRepository nodeRelationshipRepository;

    @Test
    public void add() {
        int t = nodeRelationshipRepository.insertNode(4L, 1L);
        System.out.println(t);
        Assert.assertNotEquals(0, t);
        t = nodeRelationshipRepository.insertNode(6L, 4L);
        System.out.println(t);
        Assert.assertNotEquals(0, t);
    }
}
