package SpringAPIStudy.bookstore.app.item.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "category_item")
public class CategoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_item_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //상대 ROW가 존재해야 함
    @JoinColumn(name = "item_id", nullable = false) //item과 맵핑. 연관관계의 주인
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false) //category와 맵핑. 연관관계의 주인
    private Category category;


    /**
     * Entity 생성 메서드
     * CategoryItem 생성자 호출 -> Category 세팅
     */
    public static List<CategoryItem> createCategoryItem(List<Category> categories) {

        List<CategoryItem> categoryItems = new ArrayList<>();

        for (Category category : categories) {
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setCategory(category);
            categoryItems.add(categoryItem);
        }

        return categoryItems;
    }


}
