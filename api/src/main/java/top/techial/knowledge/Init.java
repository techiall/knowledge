package top.techial.knowledge;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.NodeRelation;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.NodeRelationService;
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
    private final NodeRelationService nodeRelationService;

    public Init(KnowledgeNodeService knowledgeNodeService, NodeRelationService nodeRelationService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.nodeRelationService = nodeRelationService;
    }

    @Override
    public void run(String... args) {
        log.info("init ...");
        if (knowledgeNodeService.count() == 0L) {
            knowledgeNodeService.saveAll(buildNodeVO());
            nodeRelationService.saveAll(buildNodeRelation());
        }

        log.info("end ...");
    }

    private List<KnowledgeNode> buildNodeVO() {
        List<NodeVO> list = new ArrayList<>();
        list.add(new NodeVO("暖温带半湿润大陆性季风气候", Collections.singleton("天气"), null));
        list.add(new NodeVO("济南市", Collections.singleton("城市"), null));
        list.add(new NodeVO("运城市", Collections.singleton("城市"), null));
        list.add(new NodeVO("阔叶树", Collections.singleton("项目"), Collections.singletonMap("baseInfoKeyList", "##界：##门：##纲：")));
        return list.stream().map(NodeVO::toKnowledgeNode).collect(Collectors.toList());
    }

    private List<RelationVO> buildRelationVO() {
        List<RelationVO> list = new ArrayList<>();
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "济南市", Collections.singletonMap("type", "气候")));
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "运城市", Collections.singletonMap("type", "气候")));
        list.add(new RelationVO("暖温带半湿润大陆性季风气候", "阔叶树", Collections.singletonMap("type", "包含")));
        list.add(new RelationVO("济南市", "运城市", Collections.singletonMap("type", "城市")));
        list.add(new RelationVO("运城市", "济南市", Collections.singletonMap("type", "城市")));
        list.add(new RelationVO("济南市", "阔叶树", Collections.singletonMap("type", "种植")));
        list.add(new RelationVO("运城市", "阔叶树", Collections.singletonMap("type", "种植")));
        return list;
    }

    private List<NodeRelation> buildNodeRelation() {
        List<RelationVO> list = buildRelationVO();
        List<NodeRelation> nodeRelations = new ArrayList<>();
        for (RelationVO relationVO : list) {
            NodeRelation relation = new NodeRelation()
                .setStartNode(knowledgeNodeService.findByName(relationVO.getStartNode()))
                .setEndNode(knowledgeNodeService.findByName(relationVO.getEndNode()))
                .setProperty(relationVO.getProperty());
            nodeRelations.add(relation);
        }
        return nodeRelations;
    }
}
