package SpringAPIStudy.bookstore.app.item.repository.dao.impl;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.repository.dao.ItemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemDaoImpl implements ItemDao {

    private final EntityManager em;

    @Override
    public List<Item> getAllWithCategory() {
        //item<->categoryItems 양방향
        //collection하나에만 fetch join 가능
        //1:m fetch join -> paging불가
        return em.createQuery(
                        "select distinct i from Item i" +
                                " join fetch i.categoryItems ci" +
                                " join fetch ci.category c", Item.class)
                .getResultList();
    }
}
