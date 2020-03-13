package top.techial.knowledge.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.service.dto.RecordDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordMapper {

    @Mapping(source = "user.nickName", target = "nickName")
    RecordDTO toRecordDTO(Record record);
}
