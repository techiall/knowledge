package top.techial.knowledge.service;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.HuDongItem;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HuDongItemServerTest {

    @Autowired
    private HuDongItemServer huDongItemServer;


    @Test
    @SneakyThrows
    public void findByTitle() {
        HuDongItem h = huDongItemServer.findByTitle("物理化学");
        System.out.println(h);
    }
}
