package top.techial.knowledge.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.service.dto.UserDTO;
import top.techial.knowledge.web.rest.vm.RegisterVM;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "userName", target = "nickName")
    User toUser(RegisterVM registerVM);

    UserDTO toUserDTO(User user);
}
