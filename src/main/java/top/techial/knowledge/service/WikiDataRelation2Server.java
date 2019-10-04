package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.WikiDataRelation2Repository;
import top.techial.knowledge.domain.WikiDataRelation2;

/**
 * @author techial
 */
@Service
public class WikiDataRelation2Server {
    private final WikiDataRelation2Repository wikiDataRelation2Repository;

    public WikiDataRelation2Server(WikiDataRelation2Repository wikiDataRelation2Repository) {
        this.wikiDataRelation2Repository = wikiDataRelation2Repository;
    }

    public WikiDataRelation2 findById(Long id) {
        return wikiDataRelation2Repository.findById(id).orElseThrow(NullPointerException::new);
    }
}
