package SpringAPIStudy.bookstore.app.common.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    public static <T> ResponseEntity<T> success(T body) {
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    public static <T> ResponseEntity<T> fail(T body) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    public static <T> ResponseEntity<T> forbidden(T body) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}