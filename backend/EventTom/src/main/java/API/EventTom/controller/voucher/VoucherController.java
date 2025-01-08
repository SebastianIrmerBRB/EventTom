package API.EventTom.controller.voucher;

import API.EventTom.DTO.VoucherResponse;
import API.EventTom.DTO.request.BulkVoucherGenerationRequest;
import API.EventTom.DTO.request.VoucherGenerationRequest;
import API.EventTom.config.AuthenticatedUserId;
import API.EventTom.models.Voucher;
import API.EventTom.services.vouchers.interfaces.IVoucherClaimService;
import API.EventTom.services.vouchers.interfaces.IVoucherGenerationService;
import API.EventTom.services.vouchers.interfaces.IVoucherUsageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/vouchers")
@RequiredArgsConstructor
@Validated
public class VoucherController {
    private final IVoucherGenerationService voucherGenerationService;
    private final IVoucherClaimService voucherClaimService;
    private final IVoucherUsageService voucherUsageService;

    @PostMapping
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<VoucherResponse> generateVoucher(
            @Valid @RequestBody VoucherGenerationRequest request) {
        Voucher voucher = voucherGenerationService.generateVoucher(
                request.getAmount(),
                request.getValidityDays(),
                request.getType()
        );
        return ResponseEntity.ok(VoucherResponse.fromVoucher(voucher));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('EVENT_MANAGER')")
    public ResponseEntity<List<VoucherResponse>> generateBulkVouchers(
            @Valid @RequestBody BulkVoucherGenerationRequest request) {
        List<Voucher> vouchers = voucherGenerationService.generateBulkVouchers(
                request.getAmount(),
                request.getValidityDays(),
                request.getType(),
                request.getCount()
        );
        return ResponseEntity.ok(
                vouchers.stream()
                        .map(VoucherResponse::fromVoucher)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{code}/validate")
    public ResponseEntity<VoucherResponse> validateVoucher(@PathVariable String code) {
        Voucher voucher = voucherUsageService.validateVoucher(code);
        return ResponseEntity.ok(VoucherResponse.fromVoucher(voucher));
    }

    @PutMapping("/{code}/claim")
    public ResponseEntity<VoucherResponse> claimVoucher(
            @PathVariable String code,
            @AuthenticatedUserId Long userId) {
        Voucher voucher = voucherClaimService.claimVoucher(code, userId);
        return ResponseEntity.ok(VoucherResponse.fromVoucher(voucher));
    }

}