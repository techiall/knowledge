package top.techial.knowledge.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeRepositoryTest {

    @Autowired
    private KnowledgeNodeRepository knowledgeNodeRepository;

    @Test
    public void save() {
        knowledgeNodeRepository.deleteAll();
        Map<String, String> map = new HashMap<>();
        map.put("admin", "admin-value");
        map.put("techial", "techial-value");
        KnowledgeNode knowledgeNode = new KnowledgeNode()
            .setLabels(Collections.singletonList("techial"))
            .setProperty(map);
        knowledgeNodeRepository.save(knowledgeNode);
    }

}
