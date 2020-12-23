package cc.techial.knowledge.service.mapper;

import cc.techial.knowledge.domain.User;
import cc.techial.knowledge.service.dto.UserDTO;
import cc.techial.knowledge.web.rest.vm.RegisterVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "userName", target = "nickName")
    User toUser(RegisterVM registerVM);

    UserDTO toUserDTO(User user);
}
