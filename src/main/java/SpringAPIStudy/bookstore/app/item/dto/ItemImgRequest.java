package SpringAPIStudy.bookstore.app.item.dto;

import SpringAPIStudy.bookstore.app.common.utils.CustomObjectMapper;

import SpringAPIStudy.bookstore.app.item.entity.ItemImg;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemImgRequest {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    public static ItemImgRequest of(ItemImg itemImg) { //Entity->Dto
        return CustomObjectMapper.to(itemImg, ItemImgRequest.class);
    }
}
