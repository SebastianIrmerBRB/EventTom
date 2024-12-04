package API.EventTom.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    public String name;
    public String email;
    public List<VoucherDTO> vouchers;
    public List<TicketDTO> tickets;
}
