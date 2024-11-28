package API.EventTom.services.interfaces;


import API.EventTom.DTO.TicketDTO;
import API.EventTom.DTO.VoucherDTO;

import java.util.List;

public interface IVoucherService {

    public List<VoucherDTO> getByCustomerId(String customerId);
    public VoucherDTO getVoucherById(long voucherId);

}
