package SpringAPIStudy.bookstore.app.auth.dto;

import SpringAPIStudy.bookstore.app.auth.entity.User;
import SpringAPIStudy.bookstore.app.enums.Role;
import SpringAPIStudy.bookstore.app.enums.Social;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthAttributes { //provider마다 제공해주는 정보 형태가 다르기 때문에 분기 정리 클래스
    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String nickname;
    private Social social;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String nameAttributeKey, String name, String email, String nickname, Social social){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.social = social;
    }

    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String,Object> attributes){
        //provider 판단 -> accessToken으로 받은 속성 클래스 저장
        switch (provider) {
            case "naver":
                return ofNaver("id", attributes);
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

    private static OAuthAttributes ofNaver(String attributeKey,
                                           Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .nickname((String) response.get("nickname"))
                .social(Social.NAVER)
                .attributes(response)
                .nameAttributeKey(attributeKey)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributeKey); //id랑 key왜 둘 다 nameAttributeKey인지 의문
        map.put("key", nameAttributeKey);
        map.put("name", name);
        map.put("email", email);
        map.put("nickname", nickname);
        map.put("social", social);

        return map;
    }

    public User toEntity(){ //User Entity를 리턴
        return User.builder()
                .social(social)
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }
}
