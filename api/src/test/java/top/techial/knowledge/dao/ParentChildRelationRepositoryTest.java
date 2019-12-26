package top.techial.knowledge.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import top.techial.knowledge.domain.OperatorMessageEnum;

public class ParentChildRelationRepositoryTest {

    @Test
    public void test() {
        System.out.println(String.format(OperatorMessageEnum.DELETE_NODE_RELATION.getMessage(), 1L));
    }

    @Test
    public void jsonTest() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(Long.valueOf(11L)));
    }
}
