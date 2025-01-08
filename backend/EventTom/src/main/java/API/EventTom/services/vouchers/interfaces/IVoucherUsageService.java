package API.EventTom.services.vouchers.interfaces;

import API.EventTom.models.Voucher;

import java.math.BigDecimal;
import java.util.List;

public interface IVoucherUsageService {
    void useVoucherForPurchase(String code, Long customerId);
    BigDecimal calculateDiscountedAmount(BigDecimal originalAmount, Voucher voucher);
    Voucher validateVoucher(String code);
    List<Voucher> validateVouchers(List<String> code);
    BigDecimal calculateTotalDiscount(List<Voucher> vouchers);
    void markVouchersAsUsed(List<Voucher> vouchers, Long customerId);
}