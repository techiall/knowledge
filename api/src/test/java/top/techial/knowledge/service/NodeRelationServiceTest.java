package top.techial.knowledge.service;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Future;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
public class NodeRelationServiceTest {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    @SneakyThrows
    public void test() {
        Future<Integer> a = threadPoolTaskExecutor.submit(this::sum);
        Future<Integer> b = threadPoolTaskExecutor.submit(this::sum);
        Future<Integer> c = threadPoolTaskExecutor.submit(this::sum);

        log.info(a.get() + b.get() + c.get());
        Assert.assertNotNull(a.get());
    }

    @SneakyThrows
    public int sum() {
        Thread.sleep(1000L);
        int result = RandomUtils.nextInt();
        log.info(result);
        return result;
    }

}
