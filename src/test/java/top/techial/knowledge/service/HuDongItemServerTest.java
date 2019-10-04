package top.techial.knowledge.service;

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
        System.out.println(huDongItemServer.findByTitle("主导地位"));
    }
}
