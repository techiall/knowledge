package cc.techial.knowledge.service.mapper;

import cc.techial.knowledge.domain.Item;
import cc.techial.knowledge.service.dto.ItemDTO;
import cc.techial.knowledge.web.rest.vm.ItemVM;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    ItemDTO toItemDTO(Item item);

    Item toItem(ItemVM itemVM);

    default List<SimpleGrantedAuthority> toListSimpleGrantedAuthority(Collection<Integer> items) {
        return items
                .parallelStream()
                .map(it -> "ITEM_" + it)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
