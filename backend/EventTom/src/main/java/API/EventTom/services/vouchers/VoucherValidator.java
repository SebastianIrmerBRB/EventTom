package API.EventTom.services.vouchers;
import API.EventTom.exceptions.RuntimeExceptions.ExpiredVoucherException;
import API.EventTom.exceptions.RuntimeExceptions.InvalidVoucherException;
import API.EventTom.exceptions.RuntimeExceptions.VoucherNotFoundException;
import API.EventTom.models.Voucher;
import API.EventTom.repositories.VoucherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class VoucherValidator implements IVoucherValidator {
    private final VoucherRepository voucherRepository;

    @Override
    public void validateVoucher(String voucherCode, String customerId, long purchaseAmount) {
        if (voucherCode == null) {
            return;
        }

        Voucher voucher = voucherRepository.findByCode(voucherCode)
                .orElseThrow(() -> new VoucherNotFoundException(voucherCode));

        validateVoucherOwnership(voucher, customerId);
        validateVoucherExpiration(voucher);
        validateVoucherAmount(voucher);
        validatePurchaseAmount(voucher, purchaseAmount);
    }

    @Override
    public long calculateDiscountedAmount(String voucherCode, long originalAmount) {
        if (voucherCode == null) {
            return originalAmount;
        }

        Voucher voucher = voucherRepository.findByCode(voucherCode)
                .orElseThrow(() -> new VoucherNotFoundException(voucherCode));

        return Math.max(0, originalAmount - voucher.getAmount());
    }

    private void validateVoucherOwnership(Voucher voucher, String customerId) {
        if (!voucher.getCustomer().getCustomerNumber().equals(customerId))
            throw new InvalidVoucherException("Voucher does not belong to this customer");
    }

    private void validateVoucherExpiration(Voucher voucher) {
        if (LocalDateTime.now().isAfter(voucher.getDateValidUntil())) {
            throw new ExpiredVoucherException("Voucher has expired");
        }
    }

    private void validateVoucherAmount(Voucher voucher) {
        if (voucher.getAmount() <= 0) {
            throw new InvalidVoucherException("Voucher amount is invalid");
        }
    }

    private void validatePurchaseAmount(Voucher voucher, long purchaseAmount) {
        if (purchaseAmount < voucher.getAmount()) {
            throw new InvalidVoucherException("Purchase amount must be greater than voucher amount");
        }
    }
}