package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.dto.ItemDTO;
import top.techial.knowledge.vo.ItemVO;

/**
 * @author techial
 */
@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDTO toItemDTO(Item item);

    Item toItem(ItemVO itemVO);
}
