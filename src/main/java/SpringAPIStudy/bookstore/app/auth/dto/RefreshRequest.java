package SpringAPIStudy.bookstore.app.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshRequest {

    private String accessToken;
    private String refreshToken;

}
