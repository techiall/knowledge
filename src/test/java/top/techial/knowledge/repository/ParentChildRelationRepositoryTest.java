package top.techial.knowledge.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import top.techial.knowledge.utils.JsonUtils;

public class ParentChildRelationRepositoryTest {

    @Test
    public void jsonTest() throws JsonProcessingException {
        System.out.println(JsonUtils.writeValueAsString(11L));
    }
}
