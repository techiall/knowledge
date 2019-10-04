package top.techial.knowledge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WikiDataRelation2ServerTest {
    @Autowired
    private WikiDataRelation2Server wikiDataRelation2Server;

    @Test
    public void findById() {
        System.out.println(wikiDataRelation2Server.findById(28903L));
    }
}
