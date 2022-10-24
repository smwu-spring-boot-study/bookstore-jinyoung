package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.entity.Item;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemService {


    Long uploadItem(Item item);

    List<Item> getItems();

    Item getItem(Long id) throws NoSuchElementException;

    Item updateItem(Item item) throws NoSuchElementException;

    Long deleteItem(Long id) throws NoSuchElementException;

}
