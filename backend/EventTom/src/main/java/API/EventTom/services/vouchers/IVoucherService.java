package API.EventTom.services.vouchers;

import API.EventTom.DTO.VoucherDTO;

import java.util.List;

public interface IVoucherService {

    List<VoucherDTO> getVouchersByCustomerId(String customerId);
    VoucherDTO getVoucherById(long voucherId);

}
