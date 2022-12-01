package SpringAPIStudy.bookstore.app.item.service;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemImgService {

    void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception;

    List<ItemImg> getItemImgList(Long itemId);

    void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception;
}
