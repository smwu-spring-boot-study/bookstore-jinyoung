package SpringAPIStudy.bookstore.app.common.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@AllArgsConstructor
public class ResponseData {
    private HttpStatus code;
    private String msg;
    private Object data;
}
