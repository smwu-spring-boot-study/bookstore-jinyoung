package SpringAPIStudy.bookstore.app.item.repository.dao;

import SpringAPIStudy.bookstore.app.item.entity.Item;

import java.util.List;

public interface ItemDao {

    List<Item> getAllWithCategory();

}
