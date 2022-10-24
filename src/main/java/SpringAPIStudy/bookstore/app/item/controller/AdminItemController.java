package SpringAPIStudy.bookstore.app.item.controller;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.entity.Item;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/v1/admin/item")
@RequiredArgsConstructor
public class AdminItemController {

    private final ItemService itemService;

    @ApiOperation(value = "Item 등록", notes = "RequestBody에 Item정보 첨부")
    @PostMapping()
    public ApiResponse<Long> createItem(@RequestBody @Valid ItemRequest item) {
        return ApiResponse.created(itemService.uploadItem(Item.of(item)));
    }

    @ApiOperation(value = "Item 수정", notes = "RequestBody에 Item정보 첨부")
    @PutMapping()
    public ApiResponse<Item> updateItem(@RequestBody @Valid ItemRequest item) {
        return ApiResponse.success(itemService.updateItem(Item.of(item)));
    }

    @ApiOperation(value = "Item 삭제", notes = "PathVariable으로 특정 Item id 첨부")
    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteItem(@PathVariable("id") Long id) {
        return ApiResponse.success(itemService.deleteItem(id));
    }




}
