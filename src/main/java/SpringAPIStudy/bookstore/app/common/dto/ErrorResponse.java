package SpringAPIStudy.bookstore.app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {

    @Enumerated(EnumType.ORDINAL)
    private HttpStatus status;
    private String msg;

}
