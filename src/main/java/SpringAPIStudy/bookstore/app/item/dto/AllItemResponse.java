package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class AllItemResponse {

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String author;

    private String description;

    @NotNull
    private int price;


    private List<CategoryDto> categories;

    private void reduceDescription() {
        if (this.description.length() > 30) this.description = description.substring(0, 30);
    }

    public static List<AllItemResponse> of(List<Item> itemList) { //Entity->Dto

        return itemList.stream().map(item -> {
            AllItemResponse allItemResponse = CustomObjectMapper.objectMapper.convertValue(item, AllItemResponse.class);
            allItemResponse.reduceDescription();
            allItemResponse.categories = item.getCategoryItems().stream()
                    .map(categoryItem -> new CategoryDto(categoryItem))
                    .collect(Collectors.toList());
            return allItemResponse;
        }).collect(Collectors.toList());

    }

    @Data
    private static class CategoryDto {

        private Long parent_id;

        private Long id;

        public CategoryDto(CategoryItem categoryItem) {
            this.parent_id = categoryItem.getCategory().getParent().getId();
            this.id = categoryItem.getCategory().getId();
        }

    }
}
