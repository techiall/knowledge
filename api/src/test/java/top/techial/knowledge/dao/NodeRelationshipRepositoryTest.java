package top.techial.knowledge.dao;

import org.junit.Test;
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
    public void test() {
        int t = nodeRelationshipRepository.insertNode(4L, 1L);
        System.out.println(t);
    }

}