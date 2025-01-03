package API.EventTom.services.vouchers;
import API.EventTom.exceptions.RuntimeExceptions.ExpiredVoucherException;
import API.EventTom.exceptions.RuntimeExceptions.VoucherNotFoundException;
import API.EventTom.exceptions.UnauthorizedVoucherUseException;
import API.EventTom.exceptions.VoucherAlreadyClaimedException;
import API.EventTom.exceptions.VoucherAlreadyUsedException;
import API.EventTom.models.Voucher;
import API.EventTom.repositories.VoucherRepository;

import API.EventTom.services.vouchers.interfaces.IVoucherValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class VoucherValidationServiceImpl implements IVoucherValidationService {
    private final VoucherRepository voucherRepository;

    @Override
    public Voucher validateVoucherExists(String code) {
        return voucherRepository.findByCode(code)
                .orElseThrow(() -> new VoucherNotFoundException("Invalid voucher code"));
    }

    @Override
    public void validateVoucherNotExpired(Voucher voucher) {
        if (voucher.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new ExpiredVoucherException("This voucher has expired");
        }
    }

    @Override
    public void validateVoucherNotUsed(Voucher voucher) {
        if (voucher.isUsed()) {
            throw new VoucherAlreadyUsedException("This voucher has already been used");
        }
    }

    @Override
    public void validateVoucherNotClaimed(Voucher voucher) {
        if (voucher.getCustomer() != null) {
            throw new VoucherAlreadyClaimedException("This voucher has already been claimed");
        }
    }

    @Override
    public void validateVoucherOwnership(Voucher voucher, Long customerId) {
        if (!voucher.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedVoucherUseException("This voucher belongs to another customer");
        }
    }
}
