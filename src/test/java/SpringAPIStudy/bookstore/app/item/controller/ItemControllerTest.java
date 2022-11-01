package SpringAPIStudy.bookstore.app.item.controller;

import SpringAPIStudy.bookstore.app.auth.service.CustomOAuth2Service;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.enums.ItemStatus;
import SpringAPIStudy.bookstore.app.item.service.impl.ItemServiceImpl;
import SpringAPIStudy.bookstore.app.user.controller.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@RunWith(SpringRunner.class) //junit이랑 스프링이랑 같이 실행
@SpringBootTest //스프링 컨테이너 안의 빈 모두 가져옴
@Transactional //ROllback-> 결과적으로 db에 쿼리 안날림
@AutoConfigureMockMvc
@WebAppConfiguration
class ItemControllerTest {

    private static final String BASE_URL = "/api/v1/item/";
    @Autowired private WebApplicationContext webContext;
    @Autowired private MockMvc mockMvc;

    @MockBean ItemServiceImpl itemService;

    @MockBean CustomOAuth2Service customOAuth2Service;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockCustomUser(socialId = "socialId", nickname = "testNick")
    void getItem() throws Exception {
        Item item =  Item.builder()
                .title("책1")
                .author("작가1")
                .description("재미있어요")
                .price(10000)
                .stock(100)
                .build();


        Long itemId = itemService.uploadItem(item);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .get(BASE_URL + itemId);

        mockMvc.perform(builder)
                //.andExpect(status().isOk())
                //ndExpect(jsonPath("$..['title'").exists())
                .andDo(print());

    }

    @Test
    @WithMockCustomUser(socialId = "socialId", nickname = "testNick")
    //@WithMockCustomOAuth2Account(registrationId = "naver", role = "ROLE_ADMIN")
    void uploadItem() throws Exception {
        Item item =  Item.builder()
                .title("책1")
                .author("작가1")
                .description("재미있어요")
                .price(10000)
                .stock(100)
                .build();

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .post(BASE_URL)
                .content(new ObjectMapper().writeValueAsString(item))
                .contentType(MediaType.APPLICATION_JSON)
                .with(oauth2Login()
                        .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        .attributes(attributes -> {
                            attributes.put("socialId", "socialId");
                            attributes.put("nickname", "testNickname");
                            attributes.put("email", "test@naver.com");
                        }));

        //given(itemService.getItem(itemId))
        //        .willReturn(item);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andDo(print());
    }
}