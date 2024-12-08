package API.EventTom.services.voucher;


import API.EventTom.DTO.VoucherDTO;

import java.util.List;

public interface IVoucherService {

    List<VoucherDTO> getVouchersByCustomerId(String customerId);
    VoucherDTO getVoucherById(long voucherId);

}
