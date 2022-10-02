package SpringAPIStudy.bookstore.app.auth.entity;


import SpringAPIStudy.bookstore.app.common.entity.BaseTimeEntity;
import SpringAPIStudy.bookstore.app.enums.Role;
import SpringAPIStudy.bookstore.app.enums.Social;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String email;

    private String nickname;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
