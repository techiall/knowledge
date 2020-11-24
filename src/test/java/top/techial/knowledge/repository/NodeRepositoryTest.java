package top.techial.knowledge.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class NodeRepositoryTest {
    @Autowired
    private NodeRepository nodeRepository;

    @Test
    public void findByItemIdIn() {
        List<Long> result = nodeRepository.findByItemIdIn(Collections.emptyList());
        System.out.println(result);
        assertNotNull(result);
    }
}
