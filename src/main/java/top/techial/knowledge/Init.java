package top.techial.knowledge;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.HuDongItem;
import top.techial.knowledge.domain.NewNode;
import top.techial.knowledge.domain.WikiDataRelation;
import top.techial.knowledge.service.HuDongItemServer;
import top.techial.knowledge.service.NewNodeServer;
import top.techial.knowledge.service.WikiDataRelationServer;
import top.techial.knowledge.utils.ResourceUtils;

import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author techial
 */
@Component
@Log4j2
public class Init implements CommandLineRunner {
    private static final Function<String, WikiDataRelation> WIKI_DATA_RELATION_FUNCTION = Init::wikiDataRelationApply;
    private static final String HU_DONG_ITEM_REGEX =
        "\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\",\"([^\"]*)\"";
    private static final String NEW_NODE_REGEX = "\"([^\"]*)\",";
    private static final Pattern HU_DONG_PATTERN = Pattern.compile(HU_DONG_ITEM_REGEX, Pattern.MULTILINE);
    private static final Function<String, HuDongItem> HU_DONG_ITEM_FUNCTION = Init::huDongItemApply;
    private static final Pattern NEW_NODE_PATTERN = Pattern.compile(NEW_NODE_REGEX, Pattern.MULTILINE);
    private static final Function<String, NewNode> NEW_NODE_FUNCTION = Init::newNodeApply;
    private final WikiDataRelationServer wikiDataRelationServer;

    @Autowired
    public Init(HuDongItemServer huDongItemServer, WikiDataRelationServer wikiDataRelationServer, NewNodeServer newNodeServer) {
        this.huDongItemServer = huDongItemServer;
        this.wikiDataRelationServer = wikiDataRelationServer;
        this.newNodeServer = newNodeServer;
    }

    private static WikiDataRelation wikiDataRelationApply(String key) {
        String[] strings = key.split(",");
        return new WikiDataRelation()
            .setStartNode(new HuDongItem().setTitle(strings[0]))
            .setRelation(strings[1])
            .setEndNode(new HuDongItem().setTitle(strings[1]));
    }


    private final HuDongItemServer huDongItemServer;

    private static NewNode newNodeApply(String key) {
        Matcher matcher = NEW_NODE_PATTERN.matcher(key);
        if (!matcher.find()) {
            return new NewNode();
        }
        return new NewNode().setTitle(matcher.group(1));
    }

    private final NewNodeServer newNodeServer;

    private static HuDongItem huDongItemApply(String key) {
        Matcher matcher = HU_DONG_PATTERN.matcher(key);
        if (!matcher.find()) {
            return new HuDongItem();
        }
        return new HuDongItem()
            .setTitle(matcher.group(1))
            .setUrl(matcher.group(2))
            .setImage(matcher.group(3))
            .setOpenTypeList(matcher.group(4))
            .setDetail(matcher.group(5))
            .setBaseInfoKeyList(matcher.group(6))
            .setBaseInfoValueList(matcher.group(7));
    }

    private void wikiDataRelationFunction(WikiDataRelation w) {
        try {
            log.info(w);
            HuDongItem start = huDongItemServer.findByTitle(w.getStartNode().getTitle());
            HuDongItem end = huDongItemServer.findByTitle(w.getEndNode().getTitle());
            w.setStartNode(start);
            w.setEndNode(end);
            wikiDataRelationServer.save(w);
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    @SneakyThrows
    public void run(String... args) {
        log.info("init start");

        if (huDongItemServer.count() == 0L) {
            Iterator<HuDongItem> dongItemIterator = ResourceUtils.read("data/hudong_pedia.csv", HU_DONG_ITEM_FUNCTION);
            huDongItemServer.saveAll(() -> dongItemIterator);
        }
        if (newNodeServer.count() == 0L) {
            Iterator<NewNode> newNodeIterator = ResourceUtils.read("data/new_node.csv", NEW_NODE_FUNCTION);
            newNodeServer.saveAll(() -> newNodeIterator);
        }

        wikiDataRelationServer.deleteAll();
        if (wikiDataRelationServer.count() == 0L) {
            Iterator<WikiDataRelation> wikiDataRelationIterator =
                ResourceUtils.read("data/wikidata_relation.csv", WIKI_DATA_RELATION_FUNCTION);
            wikiDataRelationIterator.forEachRemaining(this::wikiDataRelationFunction);
        }


        log.info("init success");
    }
}
