package top.techial.knowledge.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.ParentChildRelation;
import top.techial.knowledge.service.KnowledgeNodeService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParentChildRelationRepositoryTest {

    @Autowired
    private ParentChildRelationRepository parentChildRelationRepository;

    @Autowired
    private KnowledgeNodeService knowledgeNodeService;

    @Test
    public void test() {
        ParentChildRelation relation = parentChildRelationRepository.save(
            new ParentChildRelation().setStartNode(
                knowledgeNodeService.findByName("广州").orElseThrow(NullPointerException::new)
            ).setEndNode(
                knowledgeNodeService.findByName("番禺区").orElseThrow(NullPointerException::new)
            )
        );
        Assert.assertNotNull(relation);
        System.out.println(relation);
    }
}
