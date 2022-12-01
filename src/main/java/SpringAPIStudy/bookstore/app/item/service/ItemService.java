package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.dto.AllItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.DetailItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;

public interface ItemService {

    Long uploadItem(ItemRequest itemRequest, List<MultipartFile> itemImgFileList) throws Exception;

    DetailItemResponse updateItem(ItemRequest itemRequest, List<MultipartFile> itemImgFileList) throws Exception;

    Long deleteItem(Long id) throws NoSuchElementException;

    List<AllItemResponse> getItems();

    DetailItemResponse getItem(Long id) throws NoSuchElementException;

}
