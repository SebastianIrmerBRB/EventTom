package API.EventTom.repositories;

import API.EventTom.models.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    public Voucher findByVoucherId(long voucherId);
}
