package top.techial.knowledge.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.vo.NodeVO;

import java.util.Collections;

@SpringBootTest
@RunWith(SpringRunner.class)
public class KnowledgeNodeServiceTest {

    @Autowired
    private KnowledgeNodeService knowledgeNodeService;

    private NodeVO nodeVO;

    private static final Integer USER_ID = 1;

    @Before
    public void before() {
        nodeVO = new NodeVO()
            .setName(RandomStringUtils.randomAlphanumeric(8, 10))
            .setProperty(Collections.singletonMap(RandomStringUtils.randomAlphanumeric(3),
                RandomStringUtils.randomAlphanumeric(6, 10)));
        System.out.println(nodeVO);
        saveTest();
    }

    private void saveTest() {
        KnowledgeNode node = knowledgeNodeService.save(USER_ID, nodeVO);
        Assert.assertNotNull(node);
        System.out.println(node);
    }

    @Test
    public void findByIdGraph() {
        KnowledgeNode k = knowledgeNodeService.findById(1L);
        Assert.assertNotNull(k);
        Object id = knowledgeNodeService.findByIdGraph(1L, USER_ID);
        Assert.assertNotNull(id);
    }

    @Test
    public void findAllTest() {
        Page<NodeDTO> result = knowledgeNodeService.findAll(USER_ID, PageRequest.of(0, 10), 10);
        Assert.assertNotNull(result.getContent());
        System.out.println(result.getContent());
        Assert.assertFalse(result.getContent().isEmpty());
    }

}
