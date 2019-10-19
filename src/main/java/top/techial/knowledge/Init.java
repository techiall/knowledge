package top.techial.knowledge;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.KnowledgeNodeRelation;
import top.techial.knowledge.service.KnowledgeNodeRelationService;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;
import top.techial.knowledge.vo.RelationVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Component
@Log4j2
public class Init implements CommandLineRunner {
    private final KnowledgeNodeService knowledgeNodeService;
    private final KnowledgeNodeRelationService knowledgeNodeRelationService;

    public Init(KnowledgeNodeService knowledgeNodeService, KnowledgeNodeRelationService knowledgeNodeRelationService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.knowledgeNodeRelationService = knowledgeNodeRelationService;
    }

    @Override
    public void run(String... args) {
        log.info("init ...");
        knowledgeNodeService.deleteAll();
        knowledgeNodeService.saveAll(buildNodeVO());

        knowledgeNodeRelationService.saveAll(buildNodeRelation());
        log.info("end ...");
    }

    private List<KnowledgeNode> buildNodeVO() {
        List<NodeVO> list = new ArrayList<>();
        list.add(new NodeVO("天气", "暖温带半湿润大陆性季风气候", null));
        list.add(new NodeVO("城市", "济南市", null));
        list.add(new NodeVO("城市", "运城市", null));
        list.add(new NodeVO("项目", "阔叶树", Collections.singletonMap("baseInfoKeyList", "##界：##门：##纲：")));
        return list.stream().map(NodeVO::toKnowledgeNode).collect(Collectors.toList());
    }

    private List<RelationVO> buildRelationVO() {
        List<RelationVO> list = new ArrayList<>();
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "济南市", Collections.singletonMap("type", "气候")));
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "运城市", Collections.singletonMap("type", "气候")));
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "阔叶树", Collections.singletonMap("type", "包含")));
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "阔叶树", Collections.singletonMap("type=1", "包含=1")));
        return list;
    }

    private List<KnowledgeNodeRelation> buildNodeRelation() {
        List<RelationVO> list = buildRelationVO();
        List<KnowledgeNodeRelation> knowledgeNodeRelations = new ArrayList<>();
        for (RelationVO relationVO : list) {
            knowledgeNodeRelations.add(new KnowledgeNodeRelation()
                .setStartNode(knowledgeNodeService.findByName(relationVO.getBeginNodeName()))
                .setEndNode(knowledgeNodeService.findByName(relationVO.getEndNodeName()))
                .setProperty(relationVO.getRelation()));
        }
        return knowledgeNodeRelations;
    }
}
