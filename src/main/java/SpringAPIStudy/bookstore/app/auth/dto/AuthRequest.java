package SpringAPIStudy.bookstore.app.auth.dto;

import SpringAPIStudy.bookstore.app.enums.Social;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthRequest {

    private Social social;

    private String authToken;

}
