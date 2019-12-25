package top.techial.knowledge.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.vo.RegisterVO;

import java.util.Collections;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "userName", target = "nickName")
    User toUser(RegisterVO registerVO);


    default UserPrincipal toUserPrincipal(User user) {
        return new UserPrincipal(user.getId(), user.getUserName(), user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("user")));
    }
}
