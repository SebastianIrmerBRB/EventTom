package API.EventTom.DTO;

import API.EventTom.models.Voucher;
import API.EventTom.models.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoucherResponse {
    private String code;
    private BigDecimal amount;
    private LocalDateTime expirationDate;
    private VoucherType type;

    public static VoucherResponse fromVoucher(Voucher voucher) {
        return new VoucherResponse(
                voucher.getCode(),
                voucher.getAmount(),
                voucher.getExpirationDate(),
                voucher.getType()
        );
    }
}

