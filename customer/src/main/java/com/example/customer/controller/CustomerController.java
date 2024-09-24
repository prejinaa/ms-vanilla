package com.example.customer.controller;
import com.example.customer.dto.CustomerRequest;
import com.example.customer.dto.CustomerResponse;
import com.example.customer.dto.CustomerResponseWithAccount;
import com.example.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j

public class CustomerController {

    private  final CustomerService customerService;

    @GetMapping()
    private ResponseEntity<List<CustomerResponse>> getALlCustomer() {
        log.info("Received request to fetch all customer.");
        List<CustomerResponse> customer= customerService.getAllCustomer();

        log.info("Returning {} merchants.", customer.size());
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping()
    private ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        log.info("Received request to create a new customer with  name: {}", customerRequest.name());

        CustomerResponse customerResponse=customerService.createCustomer(customerRequest);
        log.info("Customer created successfully with ID: {}", customerResponse.customerId());
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    private ResponseEntity<CustomerResponseWithAccount> getCustomerById(@PathVariable Long customerId) {
        log.info("Received request to fetch Customer with ID: {}", customerId);
        CustomerResponseWithAccount responseWithAccount=customerService.getCustomerById(customerId);

        log.info("Returning customer details for customer ID: {}", customerId);
        return new ResponseEntity<>(responseWithAccount, HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    private ResponseEntity<CustomerResponse> updateCustomer(@RequestBody CustomerRequest customerRequest,@PathVariable Long customerId) {
        log.info("Received request to update Customer with ID: {}", customerId);
       CustomerResponse customerResponse= customerService.updateCustomer(customerRequest,customerId);

        log.info("Customer with ID: {} updated successfully.", customerId);
        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }



    @DeleteMapping("{customerId}")
    ResponseEntity<?> deleteAccount(@PathVariable Long customerId) {
        log.info("Received request to delete customer with Customer ID: {}", customerId);
        customerService.deleteCustomer(customerId);

        log.info("Customer with ID: {} deleted successfully", customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
