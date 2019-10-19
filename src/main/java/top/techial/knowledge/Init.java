package top.techial.knowledge;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.service.KnowledgeNodeRelationService;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Component
public class Init implements CommandLineRunner {
    private final KnowledgeNodeService knowledgeNodeService;
    private final KnowledgeNodeRelationService knowledgeNodeRelationService;

    public Init(KnowledgeNodeService knowledgeNodeService, KnowledgeNodeRelationService knowledgeNodeRelationService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.knowledgeNodeRelationService = knowledgeNodeRelationService;
    }

    @Override
    public void run(String... args) {
        knowledgeNodeService.deleteAll();
        knowledgeNodeService.save(buildNodeVO());
    }

    private List<KnowledgeNode> buildNodeVO() {
        List<NodeVO> list = new ArrayList<>();
        list.add(new NodeVO("天气", "暖温带半湿润大陆性季风气候", null));
        list.add(new NodeVO("城市", "济南市", null));
        list.add(new NodeVO("城市", "运城市", null));
        list.add(new NodeVO("项目", "阔叶树", Collections.singletonMap("baseInfoKeyList", "##界：##门：##纲：")));
        return list.stream().map(NodeVO::toKnowledgeNode).collect(Collectors.toList());
    }
}
