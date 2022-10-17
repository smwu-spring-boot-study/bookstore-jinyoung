package SpringAPIStudy.bookstore.app.auth.service;

import SpringAPIStudy.bookstore.app.auth.dto.CustomUserDetails;
import SpringAPIStudy.bookstore.app.auth.dto.OAuthAttributes;
import SpringAPIStudy.bookstore.app.auth.entity.User;
import SpringAPIStudy.bookstore.app.auth.respository.UserRepository;
import SpringAPIStudy.bookstore.app.auth.enums.Social;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {//userRequest: registrationId, accessToken

        OAuth2UserService oAuth2UserService  = new DefaultOAuth2UserService(); //성공 정보 바탕으로 oauth 서비스 객체 만듦
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest); //accessToken으로 user정보 조회

        //서비스 구분을 위한 작업 (구글: qoogle, 네이버: naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("[loadUser] registrationId = {}", registrationId);
        Social social = Social.valueOf(registrationId);

        //provider가 제공할 user정보 속성키 가져옴
        String userAttributeKey = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("[loadUser] userNameAttributeName = {}", userAttributeKey); //naver의 경우 response

        Map<String, Object> userAttributes = oAuth2User.getAttributes();//accessToken에 따라 제공된 user정보Map 응답

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(social, userAttributes); //provider, 속성key, value넘겨 속성 정보 클래스 반환
        //accesstoken으로 받은 정보들을 속성 객체로 반환

        if (oAuthAttributes.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException("이메일이 존재하지 않습니다.");
        }

        User user = joinOrLogin(oAuthAttributes); //회원가입 혹은 로그인

        return CustomUserDetails.create(user, userAttributes); //OAuth2User구현체 생성
        //successHandler가 사용할 수 있게 반환
    }

    //회원가입 혹은 로그인
    private User joinOrLogin(OAuthAttributes attributes) {
        log.info("[saveOrUpdate] socialid: {}", attributes.getSocialId());
        User user = userRepository.findBysocialId(attributes.getSocialId())
                .orElse(userRepository.save(attributes.toEntity()));
                 //없으면 새로 생성
        return user;
    }

}
