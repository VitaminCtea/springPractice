package spring.flows.pizza.service.customer;

import org.springframework.stereotype.Service;
import spring.flows.pizza.customer.Customer;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    public CustomerServiceImpl() {}
    public Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException {
        System.out.println("lookupCustomer");
        if("9725551234".equals(phoneNumber)) {
            Customer customer = new Customer();
            customer.setId(123);
            customer.setName("Craig Walls");
            customer.setAddress("3700 Dunlavy Rd");
            customer.setCity("Denton");
            customer.setState("TX");
            customer.setZipCode("76210");
            customer.setPhoneNumber(phoneNumber);
            return customer;
        } else {
            throw new CustomerNotFoundException();
        }
    }
}
