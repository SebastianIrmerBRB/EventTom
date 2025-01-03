package API.EventTom.services.vouchers.interfaces;

import API.EventTom.models.Voucher;

import java.math.BigDecimal;

public interface IVoucherUsageService {
    Voucher useVoucherForPurchase(String code, Long customerId, BigDecimal purchaseAmount);
    BigDecimal calculateDiscountedAmount(BigDecimal originalAmount, Voucher voucher);
    Voucher validateVoucher(String code);

}
