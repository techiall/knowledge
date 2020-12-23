package cc.techial.knowledge.service.mapper;

import cc.techial.knowledge.domain.Record;
import cc.techial.knowledge.service.dto.RecordDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecordMapper {

    @Mapping(source = "user.nickName", target = "nickName")
    RecordDTO toRecordDTO(Record record);
}
