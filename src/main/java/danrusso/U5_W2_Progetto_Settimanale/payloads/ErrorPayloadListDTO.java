package danrusso.U5_W2_Progetto_Settimanale.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorPayloadListDTO(String message, LocalDateTime timestamp, List<String> errorsList) {
}
