package SpringAPIStudy.bookstore.app.item.repository;

import SpringAPIStudy.bookstore.app.item.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
}
