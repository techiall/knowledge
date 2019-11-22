package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import top.techial.beans.ResultBean;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.dto.NodeDTO;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.vo.NodeVO;

import java.util.List;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/node")
public class KnowledgeController {
    private final KnowledgeNodeService knowledgeNodeService;

    public KnowledgeController(KnowledgeNodeService knowledgeNodeService) {
        this.knowledgeNodeService = knowledgeNodeService;
    }

    /**
     * list
     *
     * @param pageable page
     * @return nodes
     */
    @GetMapping
    public ResultBean<Page<NodeDTO>> findAll(@PageableDefault Pageable pageable) {
        return new ResultBean<>(knowledgeNodeService.findAll(pageable));
    }

    /**
     * find by id
     *
     * @param id id
     * @return node
     */
    @GetMapping("/{id}")
    public ResultBean<NodeInfoDTO> findById(@PathVariable Long id) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.findById(id)));
    }

    /**
     * save
     *
     * @param nodeVO node
     * @return node
     */
    @PostMapping
    public ResultBean<NodeInfoDTO> save(@RequestBody NodeVO nodeVO) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.save(nodeVO)));
    }

    /**
     * update
     *
     * @param id     id
     * @param nodeVO node
     * @return node
     */
    @PutMapping("/{id}")
    public ResultBean<NodeInfoDTO> update(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.update(id, nodeVO)));
    }

    /**
     * update node name
     *
     * @param id     id
     * @param nodeVO node
     * @return node
     */
    @PatchMapping("/{id}/name")
    public ResultBean<NodeInfoDTO> updateName(@PathVariable Long id, @RequestBody NodeVO nodeVO) {
        return new ResultBean<>(new NodeInfoDTO(knowledgeNodeService.updateName(id, nodeVO.getName())));
    }

    /**
     * delete graph
     *
     * @param id id
     * @return boolean
     */
    @DeleteMapping("/{id}")
    public ResultBean<Boolean> deleteById(@PathVariable Long id) {
        knowledgeNodeService.deleteById(id);
        return new ResultBean<>(true);
    }


    /**
     * graph
     *
     * @param id id
     * @return object
     */
    @GetMapping("/{id}/graph")
    public ResultBean<Object> findByIdGraph(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByIdGraph(id));
    }

    @GetMapping("/{id}/child")
    public ResultBean<List<NodeDTO>> findChildNode(@PathVariable Long id) {
        return new ResultBean<>(knowledgeNodeService.findByChildNode(id));
    }

    /**
     * query like name
     *
     * @param name     name
     * @param pageable page
     * @return nodes
     */
    @GetMapping("/name")
    public ResultBean<Page<NodeBaseDTO>> findByName(@RequestParam(name = "query") String name, @PageableDefault Pageable pageable) {
        return new ResultBean<>(knowledgeNodeService.findByNameLike("*" + name + "*", pageable).map(NodeBaseDTO::new));
    }


}
