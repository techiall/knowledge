package top.techial.knowledge;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.HuDongItem;
import top.techial.knowledge.service.HuDongItemServer;
import top.techial.knowledge.utils.ResourceUtils;

import java.util.List;
import java.util.function.Function;

/**
 * @author techial
 */
@Component
@Log4j2
public class Init implements CommandLineRunner {
    private static final Function<String[], HuDongItem> HU_DONG_ITEM_FUNCTION = (String[] key) ->
        new HuDongItem()
            .setTitle(key[0].trim().replace("\"", ""))
            .setUrl(key[1].trim().replace("\"", ""))
            .setImage(key[2].trim().replace("\"", ""))
            .setOpenTypeList(key[3].trim().replace("\"", ""))
            .setDetail(key[4].trim().replace("\"", ""))
            .setBaseInfoKeyList(key[5].trim().replace("\"", ""))
            .setBaseInfoValueList(key[6].trim().replace("\"", ""));
    private final HuDongItemServer huDongItemServer;

    public Init(HuDongItemServer huDongItemServer) {
        this.huDongItemServer = huDongItemServer;
    }

    @Override
    public void run(String... args) {
        List<HuDongItem> list = ResourceUtils.read("data/hu-dong.csv", HU_DONG_ITEM_FUNCTION, ",");

        huDongItemServer.deleteAll();
        list.forEach(huDongItemServer::save);
    }
}
