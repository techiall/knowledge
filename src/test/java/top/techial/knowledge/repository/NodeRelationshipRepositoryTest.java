package top.techial.knowledge.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NodeRelationshipRepositoryTest {
    @Autowired
    private NodeRelationshipRepository nodeRelationshipRepository;

    @Test
    public void add() {
        int t = nodeRelationshipRepository.insertNode(4L, 1L);
        System.out.println(t);
        assertNotEquals(0, t);
        t = nodeRelationshipRepository.insertNode(6L, 4L);
        System.out.println(t);
        assertNotEquals(0, t);
    }
}
