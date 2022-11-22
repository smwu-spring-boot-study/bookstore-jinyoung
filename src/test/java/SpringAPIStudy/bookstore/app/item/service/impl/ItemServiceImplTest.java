package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.dto.AllItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.entity.Category;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import SpringAPIStudy.bookstore.app.item.repository.CategoryRepository;
import SpringAPIStudy.bookstore.app.item.repository.ItemImgRepository;
import SpringAPIStudy.bookstore.app.item.repository.ItemRepository;
import SpringAPIStudy.bookstore.app.item.repository.dao.ItemDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class) //junit이랑 스프링이랑 같이 실행
@SpringBootTest //스프링 컨테이너 안의 빈 모두 가져옴
@Transactional //ROllback-> 결과적으로 db에 쿼리 안날림
class ItemServiceImplTest {

    @Autowired private ItemRepository itemRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ItemDao itemDao;
    @Autowired private ItemImgRepository itemImgRepository;
    @Autowired private ItemImgServiceImpl itemImgService;

    @Autowired private ItemServiceImpl itemService;
    @PersistenceContext private EntityManager em;

    @BeforeEach
    void setUpTest() {
        itemService = new ItemServiceImpl(itemRepository, categoryRepository, itemDao, itemImgService);
    }


    @Test
    void uploadItem() throws Exception{
        List<MultipartFile> multipartFileList = createMultipartFiles();

        ItemRequest itemRequest = createItemRequest();
        Long saveId = itemService.uploadItem(itemRequest, multipartFileList);
        em.flush(); //컨텍스트 변경 내용이 db에 반영

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(saveId);

        assertEquals(itemRequest.getTitle(), itemRepository.getById(saveId).getTitle());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName());

    }

    @Test
    void getItems() {
        createItems();
        em.flush();

        List<AllItemResponse> items = itemService.getItems();
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
        itemRepository.save(item);
        em.flush();

        List<Long> categoryIds = createCategoryIds();

        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .title("변경된 제목")
                .author("변경된 작가")
                .description("")
                .price(29000)
                .stock(0)
                .categoryIds(categoryIds)
                .build();

        itemService.updateItem(itemRequest);
        em.flush();
        
        assertEquals(itemService.getItem(1L).getTitle(), "변경된 제목");

    }

    @Test
    void deleteItem() {
        Item item = createItem();
        Long saveId = itemRepository.save(item).getId();
        em.flush();
        assertEquals(1, itemService.getItems().size());

        itemService.deleteItem(saveId);
        em.flush();

        assertEquals(0, itemService.getItems().size());
    }

    @Test
    void deleteItemException(){
        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> itemService.deleteItem(1L));
        assertEquals(e.getMessage(), "Item Not Found");
    }

    private ItemRequest createItemRequest() {
        List<Long> categoryIds = createCategoryIds();

        ItemRequest itemRequest = ItemRequest.builder()
                .title("제목1")
                .author("작가1")
                .description("내용1")
                .price(1000)
                .stock(100)
                .categoryIds(categoryIds)
                .build();
        return itemRequest;
    }

    private List<CategoryItem> createCategoryItems() {
        List<Long> categoryIds = createCategoryIds();
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        List<CategoryItem> categoryItems = CategoryItem.createCategoryItem(categories);
        return categoryItems;
    }

    private List<Long> createCategoryIds() {
        List<Long> categoryIds = new ArrayList<>();
        categoryIds.add(2L);
        categoryIds.add(5L);
        return categoryIds;
    }
    private Item createItem() {
        ItemRequest itemRequest = createItemRequest();
        Item item = itemRequest.createItem(createCategoryItems());
        return item;
    }

    private void createItems() {
        ItemRequest itemRequest1 = createItemRequest();
        Item item1 = itemRequest1.createItem(createCategoryItems());

        itemRepository.save(item1);

        ItemRequest itemRequest2 = createItemRequest();
        Item item2 = itemRequest2.createItem(createCategoryItems());

        itemRepository.save(item2);
    }

    private List<MultipartFile> createMultipartFiles() throws Exception{

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i=0;i<5;i++){
            String path = "D:/jinyoung/Documents/2022SpringAPI_Study/bookstore/img/item/";
            String imageName = "image" + i + ".png";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }
}