package top.techial.knowledge.dao;

import org.junit.Test;
import top.techial.knowledge.domain.OperatorMessageEnum;

public class ParentChildRelationRepositoryTest {

    @Test
    public void test() {
        System.out.println(String.format(OperatorMessageEnum.DELETE_NODE_RELATION.getMessage(), 1L));
    }
}
