package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.dto.AllItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.DetailItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemService {

    Long uploadItem(ItemRequest itemRequest);

    DetailItemResponse updateItem(ItemRequest itemRequest) throws NoSuchElementException;

    Long deleteItem(Long id) throws NoSuchElementException;

    List<AllItemResponse> getItems();

    DetailItemResponse getItem(Long id) throws NoSuchElementException;

}
