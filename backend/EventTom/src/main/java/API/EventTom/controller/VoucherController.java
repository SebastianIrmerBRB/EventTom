package API.EventTom.controller;


import API.EventTom.DTO.VoucherDTO;
import API.EventTom.services.interfaces.IVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/vouchers")
public class VoucherController {
    IVoucherService voucherService;
    // TODO: WRITE DTO FOR EACH REQUEST
    @GetMapping("/customer/{customerId}")  // Better path for customer-specific vouchers
    public ResponseEntity<List<VoucherDTO>> getCustomerVouchers(@PathVariable String customerId) {
        List<VoucherDTO> vouchers = voucherService.getVouchersByCustomerId(customerId);
        return ResponseEntity.ok(vouchers);
    }

    @GetMapping("/{voucherId}")  // More specific parameter name
    public ResponseEntity<VoucherDTO> getVoucher(@PathVariable long voucherId) {
        VoucherDTO voucherDTO = voucherService.getVoucherById(voucherId);
        return ResponseEntity.ok(voucherDTO);
    }
}