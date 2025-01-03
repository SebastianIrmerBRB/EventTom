package API.EventTom.mappers;

import API.EventTom.DTO.*;
import API.EventTom.models.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StandardDTOMapper {

    public CustomerDTO mapCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        User user = customer.getUser();
        customerDTO.setName(user.getFirstName() + " " + user.getLastName());
        customerDTO.setEmail(user.getEmail());
        customerDTO.setTickets(customer.getTickets().stream()
                .map(this::mapTicketToTicketDTO)
                .collect(Collectors.toList()));
        customerDTO.setVouchers(customer.getVouchers().stream()
                .map(this::mapVoucherToVoucherDTO)
                .collect(Collectors.toList()));
        System.out.println(customerDTO);
        return customerDTO;
    }

    public EmployeeDTO mapEmployeeToEmployeeDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        User user = employee.getUser();
        employeeDTO.setEmail(user.getEmail());
        employeeDTO.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        employeeDTO.setName(user.getFirstName() + " " + user.getLastName());
        return employeeDTO;
    }

    public EventDTO mapEventToEventDTO(Event event) {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle(event.getTitle());
        eventDTO.setSoldTickets(event.getTotalSoldTickets());
        eventDTO.setBasePrice(event.getBasePrice());
        eventDTO.setThresholdValue(event.getThresholdValue());
        eventDTO.setDateOfEvent(event.getDateOfEvent());
        return eventDTO;
    }

    public VoucherDTO mapVoucherToVoucherDTO(Voucher voucher) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setCustomerId(voucher.getCustomer().getUser().getId());
        voucherDTO.setAmount(voucher.getAmount());
        voucherDTO.setTicketValidUntil(voucher.getExpirationDate());
        return voucherDTO;
    }

    public TicketDTO mapTicketToTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setFinalPrice(ticket.getBasePrice());
        ticketDTO.setCustomerId(ticket.getCustomer().getUser().getId());
        ticketDTO.setStatusUsed(ticket.isStatusUsed());
        ticketDTO.setPurchaseDate(ticket.getPurchaseDate());
        ticketDTO.setEventId(ticket.getEvent().getEventId());
        return ticketDTO;
    }
}