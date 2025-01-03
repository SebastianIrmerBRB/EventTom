package API.EventTom.DTO.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkVoucherGenerationRequest extends VoucherGenerationRequest {
    @NotNull(message = "Count is required")
    @Positive(message = "Count must be positive")
    @Max(value = 1000, message = "Cannot generate more than 1000 vouchers at once")
    private Integer count;
}

