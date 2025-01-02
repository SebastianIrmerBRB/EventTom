package API.EventTom.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class ErrorMessageDTO {
    private String message;
    private String error;
    private Date timestamp;
    private int status;


}