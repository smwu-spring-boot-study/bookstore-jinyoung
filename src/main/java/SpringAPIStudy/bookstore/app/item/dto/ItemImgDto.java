package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public static List<ItemImgDto> of(List<ItemImg> itemImgList) { //Entity->Dto
        return itemImgList.stream()
                .map(itemImg -> CustomObjectMapper.objectMapper.convertValue(itemImg, ItemImgDto.class))
                .collect(Collectors.toList());
    }
}
