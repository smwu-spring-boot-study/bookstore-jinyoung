package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;
import SpringAPIStudy.bookstore.app.item.entity.CategoryItem;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DetailItemResponse {

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

    private List<ItemImgDto> itemImgDtoList;

    public static DetailItemResponse of(Item item) { //Entity->Dto
        DetailItemResponse detailItemResponse = CustomObjectMapper.objectMapper.convertValue(item, DetailItemResponse.class);
        detailItemResponse.categories = item.getCategoryItems().stream()
                .map(categoryItem -> new CategoryDto(categoryItem))
                .collect(Collectors.toList());
        return detailItemResponse;
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