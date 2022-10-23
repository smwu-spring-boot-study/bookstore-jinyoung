package SpringAPIStudy.bookstore.app.auth.controller;

import SpringAPIStudy.bookstore.app.auth.dto.RefreshRequest;
import SpringAPIStudy.bookstore.app.auth.dto.Token;
import SpringAPIStudy.bookstore.app.auth.service.AuthService;
import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ApiOperation(value = "accessToken 갱신", notes = "Header에 Access, Refresh 값 첨부")
    @PostMapping("/refresh")
    public ApiResponse<Token> refreshToken(HttpServletRequest request) {
        final RefreshRequest refreshHeader = refreshHeader(request);
        return ApiResponse.success(authService.refreshToken(refreshHeader));
    }

    //refresh시 accesstoken, refreshtoken을 전달받음
    private RefreshRequest refreshHeader(HttpServletRequest request) {
        String accessToken = request.getHeader("Access");
        String refreshToken = request.getHeader("Refresh");
        return RefreshRequest.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}