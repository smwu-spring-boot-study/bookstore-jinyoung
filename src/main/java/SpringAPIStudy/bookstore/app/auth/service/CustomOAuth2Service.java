package SpringAPIStudy.bookstore.app.auth.service;

import SpringAPIStudy.bookstore.app.auth.dto.OAuthAttributes;
import SpringAPIStudy.bookstore.app.auth.entity.User;
import SpringAPIStudy.bookstore.app.auth.respository.UserRepository;
import SpringAPIStudy.bookstore.app.enums.Role;
import SpringAPIStudy.bookstore.app.enums.Social;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    UserRepository userRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {//userRequest: registrationId, accessToken

        OAuth2UserService oAuth2UserService  = new DefaultOAuth2UserService(); //성공 정보 바탕으로 oauth 서비스 객체 만듦
        OAuth2User oAuth2User = oAuth2UserService .loadUser(userRequest); //accessToken으로 user정보 조회

        //서비스 구분을 위한 작업 (구글: google, 네이버: naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        //provider가 제공할 user정보 속성키 가져옴
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        log.info("registrationId = {}", registrationId);
        log.info("userNameAttributeName = {}", userNameAttributeName);

        //oAuth2User.getAttributes(); //accessToken에 따라 제공된 user정보 응답
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); //provider, 속성key, value넘겨 속성 정보 클래스 반환
        //accesstoken으로 받은 정보들을 속성 클래스로 반환

        //로그인 한 유저 정보 -> 필요한 정보만 map으로 반환
        var userAttributesMap = oAuthAttributes.convertToMap();

        /*
        if(registrationId.equals("naver")) { //중첩으로 들어가있음
            Map<String, Object> hash = (Map<String, Object>) response.get("response");
            email = (String) hash.get("email");
            nickname = (String) hash.get("nickname");
        } else if (registrationId.equals("google")) { //KAKAO로 바꿀 것
            email = (String) response.get("email");
        } else {
            throw new OAuth2AuthenticationException("허용되지 않는 인증입니다.");
        }

        User user;
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) { //이미 가입한 회원
            user = optionalUser.get();
        } else {
            user = new User();
            user.setSocial(String.valueOf(Social.NAVER));
            user.setEmail(email);
            user.setNickname(nickname);
            user.setRole(Role.USER);
            userRepository.save(user);
        }
         */

        return new DefaultOAuth2User( //유저권한, 제공 속성값, 키
                Collections.singleton(new SimpleGrantedAuthority("USER"))
                , userAttributesMap
                , "email"); //successHandler가 사용할 수 있게 반환
    }

    ////User이미 있으면 Update
    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity()); //없으면 새로 생성
        userRepository.save(user);
        return user;
    }

    public Optional<User> oAuth2UsertoUser(OAuth2User oAuth2User) {
        
        String email = (String) oAuth2User.getAttributes().get("email");
        Optional<User> user = userRepository.findByEmail(email);
        return user;

    }
}
