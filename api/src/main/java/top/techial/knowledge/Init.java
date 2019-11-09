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
            saveNodeVO();
            nodeRelationService.saveAll(buildNodeRelation());
        }

        log.info("end ...");
    }

    private void saveNodeVO() {
        NodeVO node1 = new NodeVO("暖温带半湿润大陆性季风气候", Collections.singleton("天气"), null, null);
        knowledgeNodeService.save(node1);
        NodeVO node2 = new NodeVO("济南市", Collections.singleton("城市"), null, null);
        knowledgeNodeService.save(node2);
        NodeVO node3 = new NodeVO("运城市", Collections.singleton("城市"), null, null);
        knowledgeNodeService.save(node3);
        NodeVO node4 = new NodeVO("阔叶树", Collections.singleton("项目"), Collections.singletonMap("baseInfoKeyList", "##界：##门：##纲："), null);
        knowledgeNodeService.save(node4);

        NodeVO node5 = new NodeVO("中国城市", null, null, null);
        KnowledgeNode knowledgeNode1 = knowledgeNodeService.save(node5);

        NodeVO node6 = new NodeVO("北京", null, null, null).setParentId(knowledgeNode1.getId());
        knowledgeNodeService.save(node6);
        NodeVO node7 = new NodeVO("上海", null, null, null).setParentId(knowledgeNode1.getId());
        knowledgeNodeService.save(node7);
        NodeVO node8 = new NodeVO("广州", null, null, null).setParentId(knowledgeNode1.getId());
        KnowledgeNode knowledgeNode2 = knowledgeNodeService.save(node8);
        NodeVO node9 = new NodeVO("深圳", null, null, null).setParentId(knowledgeNode1.getId());
        knowledgeNodeService.save(node9);

        NodeVO node10 = new NodeVO("天河区", null, null, null).setParentId(knowledgeNode2.getId());
        KnowledgeNode knowledgeNode3 = knowledgeNodeService.save(node10);

        NodeVO node11 = new NodeVO("车站", null, null, null).setParentId(knowledgeNode3.getId());
        knowledgeNodeService.save(node11);

        NodeVO node12 = new NodeVO("番禺区", null, null, null).setParentId(knowledgeNode2.getId());
        knowledgeNodeService.save(node12);

        NodeVO node13 = new NodeVO("中山市", null, null, null).setParentId(Long.MAX_VALUE);
        knowledgeNodeService.save(node13);


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
                .setStartNode(knowledgeNodeService.findByName(relationVO.getStartNode()).orElseThrow(NullPointerException::new))
                .setEndNode(knowledgeNodeService.findByName(relationVO.getEndNode()).orElseThrow(NullPointerException::new))
                .setProperty(relationVO.getProperty());
            nodeRelations.add(relation);
        }
        return nodeRelations;
    }
}
