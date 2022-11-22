package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.NoSuchElementException;

public interface ItemImgService {

    void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception;

    void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception;
}
