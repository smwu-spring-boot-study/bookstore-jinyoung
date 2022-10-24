package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

    public void reduceDescription() {
        if (this.description.length() > 30) this.description = description.substring(0, 30);
    }

    private static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //생성될 object에는 없는 필드는 옮기지 않는다
    public static List<AllItemResponse> of(List<Item> itemList) { //Entity->Dto

        return itemList.stream().map(item -> {
            AllItemResponse allItemResponse = objectMapper.convertValue(item, AllItemResponse.class);
            allItemResponse.reduceDescription();
            return allItemResponse;
        }).collect(Collectors.toList());

    }

}
