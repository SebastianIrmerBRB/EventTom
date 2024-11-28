package API.EventTom.controller;


import API.EventTom.DTO.VoucherDTO;
import API.EventTom.services.interfaces.IVoucherService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class VoucherController  {

    IVoucherService voucherService;

    // TODO: WRITE DTO FOR EACH REQUEST

}
