package danrusso.U5_W2_Progetto_Settimanale.exceptions;

import danrusso.U5_W2_Progetto_Settimanale.payloads.ErrorPayloadListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorPayloadListDTO handleValidationException(ValidationException ex) {
        return new ErrorPayloadListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorMessages());
    }

}
