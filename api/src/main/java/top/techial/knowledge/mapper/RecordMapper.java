package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.dto.RecordDTO;

/**
 * @author techial
 */
@Mapper
public interface RecordMapper {

    RecordMapper INSTANCE = Mappers.getMapper(RecordMapper.class);

    RecordDTO toRecordDTO(Record record);

}
