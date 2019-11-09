package top.techial.knowledge.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.ParentChildRelation;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParentChildRelationRepositoryTest {

    @Autowired
    private ParentChildRelationRepository parentChildRelationRepository;

    @Test
    public void test() {
        ParentChildRelation relation = parentChildRelationRepository.findFirstByStartNodeIdAndEndNodeId(17L, 60L)
            .orElseThrow(NullPointerException::new);
        Assert.assertNotNull(relation);
        System.out.println(relation);
    }
}
