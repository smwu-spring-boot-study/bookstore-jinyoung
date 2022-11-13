package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.entity.Item;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemService {

    Long uploadItemV2(ItemRequest itemRequest);

    Item updateItemV2(ItemRequest itemRequest) throws NoSuchElementException;

    Long deleteItem(Long id) throws NoSuchElementException;

    List<Item> getItems();

    Item getItem(Long id) throws NoSuchElementException;


}
