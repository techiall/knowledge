package top.techial.knowledge.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import top.techial.beans.ResultBean;
import top.techial.knowledge.BasicTest;

public class NodeBaseDTOTest extends BasicTest<NodeBaseDTO> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NodeBaseDTO newInstance() {
        return null;
    }

    @Test
    public void toJson() throws JsonProcessingException {
        String result = objectMapper.writeValueAsString(new ResultBean<>(new NodeBaseDTO()
                .setId(RandomUtils.nextLong())
                .setName(RandomStringUtils.randomAlphanumeric(20))
        ));
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}