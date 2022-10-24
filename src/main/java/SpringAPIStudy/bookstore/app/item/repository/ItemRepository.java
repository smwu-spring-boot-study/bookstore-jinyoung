package SpringAPIStudy.bookstore.app.item.repository;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {


}
