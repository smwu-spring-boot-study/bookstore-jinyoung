package SpringAPIStudy.bookstore.app.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserDto {

    private String email;
    private String name;
    private String nickname;

    @Builder
    public UserDto(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }
}
