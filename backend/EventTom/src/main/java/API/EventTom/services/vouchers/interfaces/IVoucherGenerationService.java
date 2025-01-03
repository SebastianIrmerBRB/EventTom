package API.EventTom.services.vouchers.interfaces;

import API.EventTom.models.Voucher;
import API.EventTom.models.VoucherType;

import java.math.BigDecimal;
import java.util.List;

public interface IVoucherGenerationService {
    Voucher generateVoucher(BigDecimal amount, int validityDays, VoucherType type);
    List<Voucher> generateBulkVouchers(BigDecimal amount, int validityDays, VoucherType type, int count);
}