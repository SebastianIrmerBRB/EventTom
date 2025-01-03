package API.EventTom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {

    public long customerId;
    public BigDecimal amount;
    public LocalDateTime ticketValidUntil;
}
