package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.dto.ItemDTO;
import top.techial.knowledge.dto.ItemShareDTO;
import top.techial.knowledge.vo.ItemVO;

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

    @Mapping(source = "author.id", target = "userId")
    @Mapping(source = "author.userName", target = "userName")
    @Mapping(source = "author.nickName", target = "nickName")
    ItemShareDTO toItemShareDTO(Item item);

    Item toItem(ItemVO itemVO);

    default List<SimpleGrantedAuthority> toListSimpleGrantedAuthority(Collection<Item> items) {
        return items
                .parallelStream()
                .map(Item::getId)
                .map(String::valueOf)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
