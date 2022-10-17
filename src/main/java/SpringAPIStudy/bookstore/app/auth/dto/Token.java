package SpringAPIStudy.bookstore.app.auth.dto;

import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Token {

    private String nickname;
    private String accessToken;
    private String refreshToken;

    public Token(String nickname, String accessToken, String refreshToken) {
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
