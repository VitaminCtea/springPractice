package spring.flows.pizza.service.customer;

import spring.flows.pizza.customer.Customer;

public interface CustomerService {
    Customer lookupCustomer(String phoneNumber) throws CustomerNotFoundException;
}
