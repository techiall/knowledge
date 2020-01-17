package top.techial.knowledge.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import top.techial.knowledge.BasicTest;

import java.time.Instant;


public class ItemTest extends BasicTest<Item> {

    public Item newInstance() {
        return new Item()
                .setId(RandomUtils.nextInt())
                .setShare(RandomUtils.nextBoolean())
                .setName(RandomStringUtils.randomAlphanumeric(10))
                .setDescription(RandomStringUtils.randomAlphanumeric(100))
                .setCreateTime(Instant.now())
                .setUpdateTime(Instant.now());
    }

    @Test
    public void toJson() throws JsonProcessingException {
        String result = objectMapper.writeValueAsString(newInstance());
        System.out.println(result);
        Assert.assertNotNull(result);
    }

}