package SpringAPIStudy.bookstore.app.common.db;

import SpringAPIStudy.bookstore.app.item.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class initDb {

    private final InitService initService;

    @PostConstruct
    public void init() { //Spring Bean 모두 등록된 시점에 호출
        initService.categoryInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        public void categoryInit() {
            Category child1 = Category.builder().name("현대").build();
            Category child2 = Category.builder().name("고전").build();
            Category child3 = Category.builder().name("현대").build();
            Category child4 = Category.builder().name("고전").build();


            Category parent1 = Category.createCategory("소설", child1, child2);
            Category parent2 = Category.createCategory("시", child3, child4);

            em.persist(parent1);
            em.persist(parent2);
        }
    }
}
