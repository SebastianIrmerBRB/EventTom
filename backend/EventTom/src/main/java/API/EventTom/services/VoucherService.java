package API.EventTom.services;


import API.EventTom.DTO.VoucherDTO;
import API.EventTom.services.interfaces.IVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoucherService implements IVoucherService {
    @Override
    public List<VoucherDTO> getByCustomerId(int customerId) {
        return List.of();
    }

    @Override
    public VoucherDTO getVoucherById(int voucherId) {
        return null;
    }
}
