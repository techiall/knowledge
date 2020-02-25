package top.techial.knowledge.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import top.techial.knowledge.BasicTest;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.service.dto.ItemDTO;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ItemDTOTest extends BasicTest<ItemDTO> {

    @Override
    public ItemDTO newInstance() {
        return new ItemDTO()
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

    @Test
    public void resultBeanInstanceToJson() throws JsonProcessingException {
        String result = objectMapper.writeValueAsString(ResultBean.ok(newInstance()));
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void resultBeanToJson() throws JsonProcessingException {
        String result = objectMapper.writeValueAsString(ResultBean.ok());
        System.out.println(result);
        Assert.assertNotNull(result);
    }

    @Test
    public void resultListBeanToJson() throws JsonProcessingException {
        List<ItemDTO> list = IntStream.range(0, 10).mapToObj(it -> newInstance()).collect(Collectors.toList());
        String result = objectMapper.writeValueAsString(ResultBean.ok(list));
        System.out.println(result);
        Assert.assertNotNull(result);
    }
}