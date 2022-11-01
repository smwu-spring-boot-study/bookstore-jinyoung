package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.enums.ItemStatus;
import SpringAPIStudy.bookstore.app.item.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import java.util.List;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) //junit이랑 스프링이랑 같이 실행
@SpringBootTest //스프링 컨테이너 안의 빈 모두 가져옴
@Transactional //ROllback-> 결과적으로 db에 쿼리 안날림
class ItemServiceImplTest {

    @Autowired private ItemRepository itemRepository;
    @Autowired private ItemServiceImpl itemService;
    @PersistenceContext EntityManager em;

    @BeforeEach
    void setUpTest() {
        itemService = new ItemServiceImpl(itemRepository);
    }


    @Test
    void uploadItem() {
        Item givenItem = createItem();

        Long saveId = itemService.uploadItem(givenItem);
        em.flush(); //컨텍스트 변경 내용이 db에 반영

        assertEquals(givenItem.getTitle(), itemRepository.getById(saveId).getTitle());
    }

    @Test
    void getItems() {
        createItems();
        em.flush();

        List<Item> items = itemService.getItems();
        assertEquals(2, items.size());
    }

    @Test()
    void getItemException() {
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> itemService.getItem(1L));
        assertEquals(e.getMessage(), "Item Not Found");
    }

    @Test
    void updateItem() {
        Item item = createItem();
        itemService.uploadItem(item);
        em.flush();

        item.setTitle("변경된 제목");
        
        itemService.updateItem(item);
        em.flush();
        
        assertEquals(itemService.getItem(1L).getTitle(), "변경된 제목");

    }

    @Test
    void deleteItem() {
        Item item = createItem();
        itemService.uploadItem(item);
        em.flush();
        assertEquals(1, itemService.getItems().size());

        itemService.deleteItem(item.getId());
        em.flush();

        assertEquals(0, itemService.getItems().size());
    }

    @Test
    void deleteItemException(){
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> itemService.deleteItem(1L));
        assertEquals(e.getMessage(), "Item Not Found");
    }

    private Item createItem() {
        Item item = Item.builder()
                .title("제목1")
                .author("작가1")
                .description("재미있는 책")
                .price(10000)
                .stock(1000)
                .build();
        return item;
    }

    private void createItems() {
        Item item1 = Item.builder()
                .title("제목1")
                .author("작가1")
                .description("재미있는 책")
                .price(10000)
                .stock(1000)
                .build();

        itemRepository.save(item1);
        Item item2 = Item.builder()
                .title("제목1")
                .author("작가1")
                .description("재미있는 책")
                .price(10000)
                .stock(1000)
                .build();

       itemRepository.save(item2);
    }
}