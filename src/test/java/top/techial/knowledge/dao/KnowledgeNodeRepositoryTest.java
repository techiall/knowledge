package top.techial.knowledge.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeRepositoryTest {

    @Autowired
    private KnowledgeNodeRepository knowledgeNodeRepository;

    @Autowired
    private KnowledgeNodeRelationRepository knowledgeNodeRelationRepository;

    @Test
    public void save() {
        knowledgeNodeRepository.deleteAll();
        Map<String, String> map = new HashMap<>();
        map.put("admin", "admin-value");
        map.put("techial", "techial-value");
        KnowledgeNode knowledgeNode1 = new KnowledgeNode()
            .setName("techial")
            .setLabels(Collections.singletonList("techial"))
            .setProperty(map);
        knowledgeNodeRepository.save(knowledgeNode1);
        KnowledgeNode knowledgeNode2 = new KnowledgeNode()
            .setName("admin")
            .setLabels(Collections.singletonList("techial"))
            .setProperty(map);
        KnowledgeNodeRelation knowledgeNodeRelation = new KnowledgeNodeRelation()
            .setEndNode(knowledgeNodeRepository.save(knowledgeNode2))
            .setStartNode(knowledgeNodeRepository.save(knowledgeNode1))
            .setProperty(Collections.singletonMap("user", "name"));
        knowledgeNodeRelationRepository.save(knowledgeNodeRelation);


    }

}
