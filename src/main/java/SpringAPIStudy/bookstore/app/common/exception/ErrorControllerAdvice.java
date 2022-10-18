package SpringAPIStudy.bookstore.app.common.exception;

import SpringAPIStudy.bookstore.app.common.dto.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(value = NoSuchElementException.class)
    public ApiResponse<Exception> handleNotFoundException(NoSuchElementException e) {
        return ApiResponse.notFound(e);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ApiResponse<Exception> handleValidException(MethodArgumentNotValidException e) {
        return ApiResponse.badRequest(e);
    }

    @ExceptionHandler(value = JwtException.class)
    public ApiResponse<Exception> handleJwtException(JwtException e) {
        return ApiResponse.unAuthorized(e);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ApiResponse<Exception> handleBadRequestException(RuntimeException e) {
        return ApiResponse.badRequest(e);
    }

    @ExceptionHandler
    public ApiResponse<Exception> handleInternalServerErrorException(Exception e) {
        return ApiResponse.internalServerError(e);
    }
}
