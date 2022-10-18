package SpringAPIStudy.bookstore.app.common.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponse<T> {

    private int code; //200, 400 ...
    private T data;
    private String msg; //err내용

    public static <T> ApiResponse<T> success(T body) { //200
        return new ApiResponse<T>(HttpStatus.OK.value(), body,null);
    }

    public static <T> ApiResponse<T> created(T body) { //201
        return new ApiResponse<T>(HttpStatus.CREATED.value(), body,null);
    }

    public static <T> ApiResponse<T> badRequest(Exception e) { //400
        return new ApiResponse(HttpStatus.BAD_REQUEST.value(), null, e.getMessage());
    }

    public static <T> ApiResponse<T> unAuthorized(Exception e) { //401
        return new ApiResponse(HttpStatus.UNAUTHORIZED.value(), null, e.getMessage());
    }

    public static <T> ApiResponse<T> forbidden(Exception e) { //403
        return new ApiResponse(HttpStatus.FORBIDDEN.value(), null, e.getMessage());
    }

    public static <T> ApiResponse<T> notFound(Exception e) { //404
        return new ApiResponse(HttpStatus.NOT_FOUND.value(), null, e.getMessage());
    }

    public static <T> ApiResponse<T> internalServerError(Exception e) { //500
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getMessage());
    }

    public static <T> ApiResponse<T> fail(HttpStatus httpStatus) {
        return new ApiResponse(httpStatus.value(), null, httpStatus.getReasonPhrase());
    }


}