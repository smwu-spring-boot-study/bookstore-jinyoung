package SpringAPIStudy.bookstore.app.item.repository;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long saveId);
}
