package SpringAPIStudy.bookstore.app.auth.entity;

import SpringAPIStudy.bookstore.app.common.entity.BaseTimeEntity;
import SpringAPIStudy.bookstore.app.auth.enums.Role;
import SpringAPIStudy.bookstore.app.auth.enums.Social;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "tb_user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String socialId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Social social;

    @Column
    private String refreshToken;


}
