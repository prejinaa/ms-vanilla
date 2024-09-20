package com.example.customer.repo;

import com.example.customer.dto.CustomerResponse;
import com.example.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    @Query("SELECT c FROM Customer c WHERE c.userId = :userId")
    List<Customer> findByUserId(Long userId);
    List<CustomerResponse>findAllBy();

}
