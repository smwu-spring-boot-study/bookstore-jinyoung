package SpringAPIStudy.bookstore.app.item.entity;

import SpringAPIStudy.bookstore.app.common.entity.BaseTimeEntity;
import SpringAPIStudy.bookstore.app.item.enums.ItemStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "item")
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock; //재고

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus = ItemStatus.FOR_SALE;

    //categoryItem의 item필드에 의해 매핑됨.양방향
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) //item persist시 필드categoryItems까지 persist 전파
    @ToString.Exclude
    private List<CategoryItem> categoryItems = new ArrayList<>();


    /**
     * 연관관계 메서드
     * categoryItem 세팅 시 CategoryItem 객체에도 Item 세팅
     */
    public void addCategoryItem(CategoryItem categoryItem) {
        this.categoryItems.add(categoryItem);
        categoryItem.setItem(this);
    }

    /**
     * Entity 생성 메서드
     * Item 생성 시 CategoryItem 필드 세팅
     */

    public void createItem(List<CategoryItem> categoryItems){

        this.validateForSale();

        for (CategoryItem categoryItem : categoryItems) {
            this.addCategoryItem(categoryItem);
        }

    }


    //==비즈니스 로직==//
    //setter 외부(레포지토리)에서 호출하기보다는 엔티티 단에서 처리할 수 있는 로직은 엔티티 안에 만들기

    public void updateItem(Item item) {
        this.title = item.getTitle();
        this.author = item.getAuthor();
        this.description = item.getDescription();
        this.price =item.getPrice();
        this.stock = item.getStock();
        this.categoryItems.clear();
        item.getCategoryItems().stream().forEach(this::addCategoryItem);

        this.validateForSale();
    }

    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stock += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stock - quantity; //남은 수량
        if (restStock < 0) {
            throw new RuntimeException("need more stock");
        }
        this.stock = restStock;
    }

    /**
     * stock이 0인데 FOR_SALE이면 SOLD_OUT 변경
     * stock이 1이상인데 SOLD_OUT이면 FOR_SALE변경
     */
    public void validateForSale() {
        if (this.stock < 1) {
            if (this.itemStatus.equals(ItemStatus.FOR_SALE))
                this.itemStatus = ItemStatus.SOLD_OUT;
        } else {
            if (this.itemStatus.equals(ItemStatus.SOLD_OUT))
                this.itemStatus = ItemStatus.FOR_SALE;
        }
    }


}
