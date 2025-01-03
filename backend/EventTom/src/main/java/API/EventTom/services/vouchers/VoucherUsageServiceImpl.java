package API.EventTom.services.vouchers;

import API.EventTom.models.Voucher;
import API.EventTom.repositories.VoucherRepository;
import API.EventTom.services.vouchers.interfaces.IVoucherUsageService;
import API.EventTom.services.vouchers.interfaces.IVoucherValidationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class VoucherUsageServiceImpl implements IVoucherUsageService {
    private final VoucherRepository voucherRepository;
    private final IVoucherValidationService validationService;

    @Override
    @Transactional
    public Voucher useVoucherForPurchase(String code, Long customerId, BigDecimal purchaseAmount) {
        Voucher voucher = validationService.validateVoucherExists(code);
        validationService.validateVoucherNotExpired(voucher);
        validationService.validateVoucherNotUsed(voucher);
        validationService.validateVoucherOwnership(voucher, customerId);

        voucher.setUsed(true);
        return voucherRepository.save(voucher);
    }

    @Override
    public BigDecimal calculateDiscountedAmount(BigDecimal originalAmount, Voucher voucher) {
        return originalAmount.subtract(voucher.getAmount()).max(BigDecimal.ZERO);
    }

    @Override
    public Voucher validateVoucher(String code) {
        Voucher voucher = validationService.validateVoucherExists(code);

        validationService.validateVoucherNotExpired(voucher);
        validationService.validateVoucherNotUsed(voucher);

        return voucher;
    }

}