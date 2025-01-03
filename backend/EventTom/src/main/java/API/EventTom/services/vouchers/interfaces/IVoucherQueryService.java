package API.EventTom.services.vouchers.interfaces;

import API.EventTom.DTO.VoucherDTO;

import java.util.List;

public interface IVoucherQueryService {

    List<VoucherDTO> getVouchersByCustomerId(String customerId);
    VoucherDTO getVoucherById(long voucherId);

}
