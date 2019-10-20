package top.techial.knowledge.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HuDongItemServerTest {

    @Autowired
    private HuDongItemServer huDongItemServer;


    @Test
    public void findByTitle() {
        Assert.assertNotNull(huDongItemServer.findByTitle("物理化学"));

    }
}
