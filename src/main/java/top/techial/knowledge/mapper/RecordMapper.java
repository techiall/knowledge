package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.dto.RecordDTO;

@Mapper
public interface RecordMapper {
    RecordMapper INSTANCE = Mappers.getMapper(RecordMapper.class);

    @Mapping(source = "user.nickName", target = "nickName")
    RecordDTO toRecordDTO(Record record);
}