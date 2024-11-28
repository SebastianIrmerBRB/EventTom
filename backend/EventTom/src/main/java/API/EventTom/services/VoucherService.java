package API.EventTom.services;


import API.EventTom.DTO.VoucherDTO;
import API.EventTom.mappers.StandardDTOMapper;
import API.EventTom.models.Voucher;
import API.EventTom.repositories.VoucherRepository;
import API.EventTom.services.interfaces.IVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VoucherService implements IVoucherService {

    VoucherRepository voucherRepository;
    StandardDTOMapper standardDTOMapper;

    @Override
    public List<VoucherDTO> getByCustomerId(String customerId) {
        List<Voucher> vouchers = voucherRepository.findAllVouchersByCustomerNumber(customerId);

        return vouchers.stream()
                .map(standardDTOMapper::mapVoucherToVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherDTO getVoucherById(long voucherId) {
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Could not find Voucher with ID"));
        return standardDTOMapper.mapVoucherToVoucherDTO(voucher);
    }
}