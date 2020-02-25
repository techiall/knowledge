package top.techial.knowledge.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

public class ParentChildRelationRepositoryTest {

    @Test
    public void jsonTest() throws JsonProcessingException {
        System.out.println(new ObjectMapper().writeValueAsString(11L));
    }
}
