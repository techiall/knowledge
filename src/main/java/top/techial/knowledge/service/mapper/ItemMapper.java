package top.techial.knowledge.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.service.dto.ItemDTO;
import top.techial.knowledge.web.rest.vm.ItemVM;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDTO toItemDTO(Item item);

    Item toItem(ItemVM itemVM);

    default List<SimpleGrantedAuthority> toListSimpleGrantedAuthority(Collection<Item> items) {
        return items
                .parallelStream()
                .map(Item::getId)
                .map(it -> "ITEM_" + it)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
