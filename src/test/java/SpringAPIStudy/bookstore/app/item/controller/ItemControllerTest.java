package SpringAPIStudy.bookstore.app.item.controller;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ItemController.class)
@Transactional
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //가짜 객체 -> 실제 행위는 개발자가 given()을 통해 정의해야함
    ItemServiceImpl itemService;

    @Test
    void getAllItems() {

    }

    @Test
    void getItem() {
        given(itemService.getItem(123L)).willReturn(
                new Item(123L, "책1", "작가1", "재미있어요", 10000, 100)
        );
        String itemId = "123";

        //mockMvc.perform(get("/api/v1/item/" + itemId)).andExpect()
    }
}