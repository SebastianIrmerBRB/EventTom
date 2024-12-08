package API.EventTom.services.vouchers;

public interface IVoucherValidator {
    void validateVoucher(String voucherCode, String customerId, long purchaseAmount);
    long calculateDiscountedAmount(String voucherCode, long originalAmount);
}