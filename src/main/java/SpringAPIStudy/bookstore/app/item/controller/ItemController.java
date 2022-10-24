package SpringAPIStudy.bookstore.app.item.controller;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.item.dto.AllItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.DetailItemResponse;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @ApiOperation(value = "전체 Item 조회", notes = "Item 전체 조회")
    @GetMapping()
    public ApiResponse<List<AllItemResponse>> getAllItems() {
        return ApiResponse.success(AllItemResponse.of(itemService.getItems()));
    }

    @ApiOperation(value = "특정 Item 조회", notes = "PathVariable으로 특정 Item id 첨부")
    @GetMapping("/{id}")
    public ApiResponse<DetailItemResponse> getItem(@PathVariable("id") Long id) {
        return ApiResponse.success(DetailItemResponse.of(itemService.getItem(id)));
    }


}