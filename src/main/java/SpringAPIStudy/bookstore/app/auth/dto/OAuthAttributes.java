package SpringAPIStudy.bookstore.app.auth.dto;

import SpringAPIStudy.bookstore.app.auth.entity.User;
import SpringAPIStudy.bookstore.app.auth.enums.Role;
import SpringAPIStudy.bookstore.app.auth.enums.Social;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthAttributes { //provider마다 제공해주는 정보 형태가 다르기 때문에 분기 정리 클래스
    private Map<String, Object> attributes;
    private String socialId;
    private String email;
    private String nickname;
    private Social social;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String socialId, String email, String nickname, Social social){
        this.attributes = attributes;
        this.socialId = socialId;
        this.email = email;
        this.nickname = nickname;
        this.social = social;
    }

    public static OAuthAttributes of(Social provider, Map<String,Object> attributes){
        //provider 판단 -> accessToken으로 받은 속성 클래스 저장
        switch (provider) {
            case naver:
                return ofNaver(attributes);
            /*case "kakao":
                return ofKakao("email", attributes);*/
            default:
                throw new RuntimeException();
        }
    }

    /*private static OAuthAttributes ofKakao(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String)kakaoProfile.get("profile_image_url"))
                .attributes(kakaoAccount)
                .attributeKey(attributeKey)
                .build();
    }*/

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        log.info("response: {}, email : {}", response.keySet(), (String) response.get("email"));
        return OAuthAttributes.builder()
                .email((String) response.get("email"))
                .nickname((String) response.get("nickname"))
                .social(Social.naver)
                .attributes(response)
                .socialId((String) response.get("id"))
                .build();
    }

    public User toEntity(){ //User Entity를 리턴
        return User.builder()
                .socialId(socialId)
                .social(social)
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
