package SpringAPIStudy.bookstore.app.item.entity;

import SpringAPIStudy.bookstore.app.common.entity.BaseTimeEntity;
import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;
import SpringAPIStudy.bookstore.app.item.enums.ItemStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    //@ManyToMany(mappedBy = "items") //category의 items 필드에 의해 매핑됨
    //private List<Category> categories = new ArrayList<>();
    //item-orderItem 단방향이기 때문에 item에서는 orderItem에 접근 불가 - 매핑 부분 필요없음

    //==비즈니스 로직==//
    //setter 외부(레포지토리)에서 호출하기보다는 엔티티 단에서 처리할 수 있는 로직은 엔티티 안에 만들기

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

    public void updateItem(Item item) {
        this.setTitle(item.getTitle());
        this.setAuthor(item.getAuthor());
        this.setDescription(item.getDescription());
        this.setPrice(item.getPrice());
        this.setStock(item.getStock());
    }

    public static Item of(Object o){ //Entity<-Dto
        return CustomObjectMapper.objectMapper.convertValue(o, Item.class);
    }
}
