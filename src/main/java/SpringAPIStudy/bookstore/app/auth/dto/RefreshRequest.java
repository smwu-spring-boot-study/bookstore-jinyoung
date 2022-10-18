package SpringAPIStudy.bookstore.app.auth.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RefreshRequest {

    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;

}
