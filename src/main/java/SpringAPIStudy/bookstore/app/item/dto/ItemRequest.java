package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


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

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ItemRequest of(Item item) { //Entity->Dto
        return objectMapper.convertValue(item, ItemRequest.class);
    }


}
