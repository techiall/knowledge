package top.techial.knowledge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import top.techial.knowledge.security.UserPrincipal;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collections;

@Data
@Document
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Indexed(unique = true)
    private String userName;

    private String nickName;

    @JsonIgnore
    private String password;

    @CreatedDate
    private Instant createTime;

    @LastModifiedDate
    private Instant updateTime;

    public UserPrincipal toUserPrincipal() {
        return new UserPrincipal(this.id, this.userName, this.password,
            Collections.singleton(new SimpleGrantedAuthority("user")));
    }
}
