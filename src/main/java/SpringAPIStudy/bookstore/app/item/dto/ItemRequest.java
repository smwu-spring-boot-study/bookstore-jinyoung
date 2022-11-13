package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequest {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String description;

    @NotNull
    @Min(0)
    private int price;

    @NotNull
    @Min(0)
    private int stock;

    @NotNull
    private List<Long> categoryIds = new ArrayList<>();

    public Item createItem(List<CategoryItem> categoryItems) { //Entity<-Dto
        Item item = CustomObjectMapper.to(this, Item.class);
        item.createItem(categoryItems);
        return item;
    }


}
