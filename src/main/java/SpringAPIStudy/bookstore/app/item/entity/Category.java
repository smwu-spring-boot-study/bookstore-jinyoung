package SpringAPIStudy.bookstore.app.item.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Category parent; //주인

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) //자식 list추가 후 저장 시 해당 entity도 persist
    @ToString.Exclude
    @JsonIgnore
    private List<Category> child = new ArrayList<>(); //종속

    //==연관관계 메서드==양방향일 때//
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }

    public static Category createCategory(String name, Category... child){
        Category category = new Category();
        category.setName(name);
        Arrays.stream(child).forEach(c -> category.addChildCategory(c));
        return category;
    }

}
