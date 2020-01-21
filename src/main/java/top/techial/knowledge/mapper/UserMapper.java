package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.UserDTO;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.vo.RegisterVO;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userName", target = "nickName")
    User toUser(RegisterVO registerVO);

    UserDTO toUserDTO(User user);

    default UserPrincipal toUserPrincipal(User user) {
        List<SimpleGrantedAuthority> item = ItemMapper.INSTANCE.toListSimpleGrantedAuthority(user.getItem());
        return new UserPrincipal(user.getId(), user.getUserName(), user.getPassword(), item);
    }
}
