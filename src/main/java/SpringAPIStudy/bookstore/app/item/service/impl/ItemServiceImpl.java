package SpringAPIStudy.bookstore.app.item.service.impl;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.repository.ItemRepository;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Long uploadItem(Item item) {
        item.validateForSale();
        return itemRepository.save(item).getId();
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

    @Override
    @Transactional
    public Item updateItem(Item item) {
        Item findItem = itemRepository.findById(item.getId())
                .orElseThrow(()->{ throw new NoSuchElementException("Item Not Found");});

        findItem.updateItem(item); //변경 감지
        findItem.validateForSale();
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
}
