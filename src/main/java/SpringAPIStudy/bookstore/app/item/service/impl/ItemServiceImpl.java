package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.item.dto.AllItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.DetailItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.entity.Category;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import SpringAPIStudy.bookstore.app.item.repository.CategoryRepository;
import SpringAPIStudy.bookstore.app.item.repository.ItemRepository;
import SpringAPIStudy.bookstore.app.item.repository.dao.ItemDao;
import SpringAPIStudy.bookstore.app.item.service.ItemImgService;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemDao itemDao;
    private final ItemImgService itemImgService;

    @Override
    @Transactional
    public Long uploadItem(ItemRequest itemRequest,  List<MultipartFile> itemImgFileList) throws Exception {
//        if (itemImgFileList.get(0).isEmpty()){
//            throw new RuntimeException("First Img Value is Necessary");
//        }
        List<Category> categories = categoryRepository.findAllById(itemRequest.getCategoryIds());
        List<CategoryItem> categoryItems = CategoryItem.createCategoryItem(categories);

        Item item = itemRequest.createItem(categoryItems);

        Long saveId = itemRepository.save(item).getId(); //아이템 등록

        uploadImgs(item, itemImgFileList); //이미지 등록

        return saveId;
    }

    @Override
    @Transactional
    public DetailItemResponse updateItem(ItemRequest itemRequest) throws NoSuchElementException {
        Item findItem = itemRepository.findById(itemRequest.getId())
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});

        List<Category> categories = categoryRepository.findAllById(itemRequest.getCategoryIds());
        List<CategoryItem> categoryItems = CategoryItem.createCategoryItem(categories);

        Item requestItem = itemRequest.createItem(categoryItems);

        findItem.updateItem(requestItem); //변경 감지
        return DetailItemResponse.of(findItem);
    }

    @Override
    @Transactional
    public Long deleteItem(Long id) throws NoSuchElementException {
        Item findItem = itemRepository.findById(id)
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});

        itemRepository.deleteById(findItem.getId());

        return findItem.getId();
    }

    @Override
    public List<AllItemResponse> getItems() {
        return AllItemResponse.of(itemDao.getAllWithCategory());
    }

    @Override
    public DetailItemResponse getItem(Long id) throws NoSuchElementException {
        Item findItem = itemRepository.findById(id)
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});
        return DetailItemResponse.of(findItem);
    }

    private void uploadImgs(Item item, List<MultipartFile> itemImgFileList) throws Exception {
        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);

            if(i == 0)
                itemImg.setRepimgYn("Y"); //대표 이미지
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
    }

}
