package SpringAPIStudy.bookstore.app.item.controller;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import SpringAPIStudy.bookstore.app.item.dto.DetailItemResponse;
import SpringAPIStudy.bookstore.app.item.dto.ItemRequest;
import SpringAPIStudy.bookstore.app.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Slf4j
@RestController
@RequestMapping("/api/v1/admin/item")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminItemController {

    private final ItemService itemService;


    @Operation(summary = "Item 등록V2", description = "RequestBody에 Item정보 첨부")
    @PostMapping()
    public ApiResponse<Long> createItem(@RequestBody @Valid ItemRequest item) {
        return ApiResponse.created(itemService.uploadItem(item));
    }

    @Operation(summary = "Item 수정V2", description = "RequestBody에 Item정보 첨부")
    @PutMapping()
    public ApiResponse<DetailItemResponse> updateItem(@RequestBody @Valid ItemRequest item) {
        return ApiResponse.success(itemService.updateItem(item));
    }

    @Operation(summary = "Item 삭제", description = "PathVariable으로 특정 Item id 첨부")
    @DeleteMapping("/{id}")
    public ApiResponse<Long> deleteItem(@PathVariable("id") Long id) {
        return ApiResponse.success(itemService.deleteItem(id));
    }




}
