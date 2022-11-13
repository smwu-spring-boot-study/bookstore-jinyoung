package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.entity.Category;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.repository.CategoryRepository;
import SpringAPIStudy.bookstore.app.item.repository.ItemRepository;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long uploadItemV2(ItemRequest itemRequest) {

        List<Category> categories = categoryRepository.findAllById(itemRequest.getCategoryIds());
        List<CategoryItem> categoryItems = CategoryItem.createCategoryItem(categories);

        Item item = itemRequest.createItem(categoryItems);

        return itemRepository.save(item).getId();
    }


    @Override
    @Transactional
    public Item updateItemV2(ItemRequest itemRequest) {
        Item findItem = itemRepository.findById(itemRequest.getId())
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});

        List<Category> categories = categoryRepository.findAllById(itemRequest.getCategoryIds());
        List<CategoryItem> categoryItems = CategoryItem.createCategoryItem(categories);

        Item requestItem = itemRequest.createItem(categoryItems);

        findItem.updateItem(requestItem); //변경 감지
        return findItem;
    }

    @Override
    @Transactional
    public Long deleteItem(Long id) {
        Item findItem = itemRepository.findById(id)
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});

        itemRepository.deleteById(findItem.getId());

        return findItem.getId();
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }

    @Override
    public Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});
    }

}
