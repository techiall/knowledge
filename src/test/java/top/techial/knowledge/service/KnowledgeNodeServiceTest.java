package top.techial.knowledge.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNode;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeServiceTest {

    @Autowired
    private KnowledgeNodeService knowledgeNodeService;

    @Test
    public void findByIdGraph() {
        KnowledgeNode k = knowledgeNodeService.findById(1L);
        Assert.assertNotNull(k);
        Object id = knowledgeNodeService.findByIdGraph(1L);
        Assert.assertNotNull(id);
    }
}
