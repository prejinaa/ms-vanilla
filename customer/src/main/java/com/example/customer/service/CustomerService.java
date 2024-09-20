package com.example.customer.service;
import com.example.customer.dto.AccountResponse;
import com.example.customer.dto.CustomerRequest;
import com.example.customer.dto.CustomerResponse;
import com.example.customer.dto.CustomerResponseWithAccount;
import com.example.customer.exception.CustomerNotFoundException;
import com.example.customer.exception.CustomerNotFoundWithUser;
import com.example.customer.model.Customer;
import com.example.customer.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private  final CustomerRepository customerRepository;
    private final WebClient webClient;

    public CustomerResponse createCustomer(CustomerRequest customerRequest){

        log.info("Creating  a new Customer With customer Name:{}",customerRequest.name());
        Customer customer= new Customer();
                customer.setName(customerRequest.name());
                customer.setContactNumber(customerRequest.contactNumber());
                customer.setAddress(customerRequest.address());
                customer.setEmail(customerRequest.email());
                customer.setUserId(customerRequest.userId());

                Customer saveCustomer=customerRepository.save(customer);

           log.info("Customer created with ID: {}", saveCustomer.getCustomerId());
                return new CustomerResponse(
                        saveCustomer.getCustomerId(),
                        saveCustomer.getName(),
                        saveCustomer.getContactNumber(),
                        saveCustomer.getAddress(),
                        saveCustomer.getEmail(),
                        saveCustomer.getUserId(),
                        saveCustomer.getCreateDate(),
                        saveCustomer.getLastModified());
    }

    public List<CustomerResponse> getAllCustomer(){
        log.info("Fetching all customer..");
        List<CustomerResponse> customer = customerRepository.findAllBy();

        log.info("Fetched {} customer", customer.size());
        return customer;
    }

    public CustomerResponseWithAccount getCustomerById(Long customerId) throws CustomerNotFoundException{
        log.info("Fetching customer with customerId: {}", customerId);
      Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->{
                    log.error("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException(customerId);
                });
        List<AccountResponse> accountResponse = webClient.get()
                .uri("http://localhost:8083/api/accounts/customer/" + customerId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AccountResponse>>() {})
                .onErrorResume(e -> {
                    log.error("Error fetching accounts for customer ID: {}: {}", customerId, e.getMessage());
                    return Mono.empty();
                })
                .block();
        log.info("Returning  Customer and account information for Customer ID: {}", customerId);
        return new CustomerResponseWithAccount(
                customer.getCustomerId(),
                customer.getName(),
                customer.getContactNumber(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getUserId(),
                customer.getCreateDate(),
                customer.getLastModified(),
                accountResponse
        );
    }

    public CustomerResponse updateCustomer(CustomerRequest customerRequest,Long customerId) throws  CustomerNotFoundException{
        log.info("Updating the customer with CustomerId:{}",customerId);

        Customer customer= customerRepository.findById(customerId).orElseThrow(()->{

            log.error("Customer Not found with Id:{}",customerId);
            return new CustomerNotFoundException(customerId);
        });
        customer.setName(customerRequest.name());
        customer.setContactNumber(customerRequest.contactNumber());
        customer.setAddress(customerRequest.address());
        customer.setEmail(customerRequest.email());
        customer.setUserId(customerRequest.userId());

        Customer updateCustomer=customerRepository.save(customer);
        log.info("Customer updated with Id:{}",updateCustomer.getCustomerId());

        return new CustomerResponse(
                updateCustomer.getCustomerId(),
                updateCustomer.getName(),
                updateCustomer.getContactNumber(),
                updateCustomer.getAddress(),
                updateCustomer.getEmail(),
                updateCustomer.getUserId(),
                updateCustomer.getCreateDate(),
                updateCustomer.getLastModified());



    }
    public List<CustomerResponseWithAccount> getCustomerByUserId(Long userId) throws CustomerNotFoundWithUser {

        log.info("Fetching Customer for user ID: {}", userId);
        List<Customer>customers=customerRepository.findByUserId(userId);

        if (customers.isEmpty()) {
            log.error("No customer found for user ID: {}", userId);
            throw new CustomerNotFoundWithUser(userId );
        }

        return customers.stream()
                .map(customer -> {
                    log.info("Fetching accounts for customer ID: {}", customer.getCustomerId());
                    List<AccountResponse> accountResponses = webClient.get()
                            .uri("http://localhost:8083/api/accounts/customer/" + customer.getCustomerId())
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<AccountResponse>>() {})
                            .onErrorResume(e -> {
                                log.error("Error fetching accounts for customer ID: {}: {}", customer.getCustomerId(), e.getMessage());
                                return Mono.empty();
                            })
                            .block();

                    return new CustomerResponseWithAccount(
                            customer.getCustomerId(),
                            customer.getName(),
                            customer.getContactNumber(),
                            customer.getAddress(),
                            customer.getEmail(),
                            customer.getUserId(),
                            customer.getCreateDate(),
                            customer.getLastModified(),
                            accountResponses);


                })
                .collect(Collectors.toList());
    }

    public void deleteCustomer(Long customerId){
        log.info("Received request to delete Customer with Customer ID: {}", customerId);

        if (!customerRepository.existsById(customerId)) {
            log.error("Customer not found with ID: {}", customerId);
            throw new CustomerNotFoundException(customerId);
        }

        customerRepository.deleteById(customerId);
        log.info("Customer with ID: {} deleted successfully", customerId);
    }


}
