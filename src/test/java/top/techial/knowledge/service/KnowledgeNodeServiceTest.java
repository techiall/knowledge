package top.techial.knowledge.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeServiceTest {

    @Autowired
    private KnowledgeNodeService knowledgeNodeService;

    @Autowired
    private KnowledgeNodeRelationService knowledgeNodeRelationService;

    @Test
    public void findByIdGraph() {
        Page<KnowledgeNodeRelation> list = knowledgeNodeRelationService.findAll(PageRequest.of(0, 100));

        System.out.println(list);
        KnowledgeNode k = knowledgeNodeService.findById(247028L);
        Assert.assertNotNull(k);
        Object id = knowledgeNodeService.findByIdGraph(247028L);
        Assert.assertNotNull(id);
    }
}
