package top.techial.knowledge.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.domain.Record;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RecordServiceTest {
    @Autowired
    private RecordService recordService;

    @Test
    public void save() {
        Record record = new Record()
            .setMessage(RandomStringUtils.randomAlphanumeric(50000))
            .setOperator(OperatorMessageEnum.DELETE_NODE_RELATION)
            .setNodeId(10L);

        Record result = recordService.save(record);
        Assert.assertNotNull(result);
        System.out.println(result);
    }

}
