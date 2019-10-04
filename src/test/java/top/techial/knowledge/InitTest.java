package top.techial.knowledge;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.techial.knowledge.domain.HuDongItem;
import top.techial.knowledge.utils.ResourceUtils;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log4j2
public class InitTest {

    private static final String REGEX = "\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"";
    private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.MULTILINE);
    private static final Function<String, HuDongItem> HU_DONG_ITEM_FUNCTION = InitTest::apply;

    private static HuDongItem apply(String key) {
        Matcher matcher = PATTERN.matcher(key);
        if (matcher.find()) {
            log.info(matcher.group());
            return new HuDongItem()
                .setTitle(matcher.group(1))
                .setUrl(matcher.group(2))
                .setImage(matcher.group(3))
                .setOpenTypeList(matcher.group(4))
                .setDetail(matcher.group(5))
                .setBaseInfoKeyList(matcher.group(6))
                .setBaseInfoValueList(matcher.group(7));
        }
        return null;
    }


    @Test
    public void importTest() {
        ResourceUtils.read("data/hu-dong.csv", HU_DONG_ITEM_FUNCTION)
            .forEachRemaining(System.out::println);
    }

}
