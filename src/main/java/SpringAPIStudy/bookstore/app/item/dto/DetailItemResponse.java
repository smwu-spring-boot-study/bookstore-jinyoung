package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.item.entity.Item;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
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

    private static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //생성될 object에는 없는 필드는 옮기지 않는다
            .registerModule(new JavaTimeModule());

    public static DetailItemResponse of(Item item) { //Entity->Dto
        return objectMapper.convertValue(item, DetailItemResponse.class);
    }

}