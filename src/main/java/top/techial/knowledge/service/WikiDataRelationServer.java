package top.techial.knowledge.service;

import org.springframework.stereotype.Service;
import top.techial.knowledge.dao.WikiDataRelationRepository;
import top.techial.knowledge.domain.WikiDataRelation;

/**
 * @author techial
 */
@Service
public class WikiDataRelationServer {
    private final WikiDataRelationRepository wikiDataRelationRepository;

    public WikiDataRelationServer(WikiDataRelationRepository wikiDataRelationRepository) {
        this.wikiDataRelationRepository = wikiDataRelationRepository;
    }

    public WikiDataRelation save(WikiDataRelation wikiDataRelation) {
        return wikiDataRelationRepository.save(wikiDataRelation);
    }

    public long count() {
        return wikiDataRelationRepository.count();
    }

    public Iterable<WikiDataRelation> saveAll(Iterable<WikiDataRelation> object) {
        return wikiDataRelationRepository.saveAll(object);
    }

    public void deleteAll() {
        wikiDataRelationRepository.deleteAll();
    }
}
