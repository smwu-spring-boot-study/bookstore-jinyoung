package SpringAPIStudy.bookstore.app.auth.config.jwt;

import lombok.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Token {
    private String token;
    private String refreshToken;

    public Token(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
