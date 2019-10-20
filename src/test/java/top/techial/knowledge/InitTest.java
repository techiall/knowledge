package top.techial.knowledge;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.HuDongItem;

import java.util.function.Function;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
public class InitTest {

    private static final String REGEX = "\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.MULTILINE);
    private static final Function<String, HuDongItem> HU_DONG_ITEM_FUNCTION = InitTest::apply;

    private static HuDongItem apply(String key) {
        String[] strings = key.split(",");
        return new HuDongItem()
            .setTitle(strings[0])
            .setBaseInfoValueList(strings[1])
            .setBaseInfoKeyList(strings[2]);
    }

    @Test
    public void test() {

    }


    @Test
    public void importTest() {
//        ResourceUtils.read("data/hudong_pedia.csv", HU_DONG_ITEM_FUNCTION);
    }

}
