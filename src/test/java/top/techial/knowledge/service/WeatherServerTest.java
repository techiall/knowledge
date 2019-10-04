package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WeatherServerTest {
    @Autowired
    private WeatherServer weatherServer;

    @Test
    public void findByTitle() {
        System.out.println(weatherServer.findByTitle("亚热带季风性湿润气候"));
    }
}
