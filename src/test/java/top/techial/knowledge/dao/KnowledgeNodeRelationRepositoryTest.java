package top.techial.knowledge.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeRelationRepositoryTest {

    @Autowired
    private KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    @Test
    public void findByStartNodeId() {
        List<KnowledgeNodeRelation> k = knowledgeNodeRelationRepository.findFirstByStartNodeName("暖温带半湿润大陆性季风气候");
        Assert.assertNotNull(k);
    }
}
