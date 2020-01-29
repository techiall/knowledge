package top.techial.knowledge.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NodeRepositoryTest {
    @Autowired
    private NodeRepository nodeRepository;

    @Test
    public void findByItemIdIn() {
        List<Long> result = nodeRepository.findByItemIdIn(Collections.singletonList(1));
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}