package com.example.Account.repository;
import com.example.Account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

     @Query("SELECT a FROM Account a WHERE a.merchantId = :merchantId")
     List<Account> findByMerchantId(@Param("merchantId") Long merchantId);

     @Query("SELECT b FROM Account b WHERE b.customerId = :customerId")
     List<Account> findByCustomerId(@Param(("customerId")) Long customerId);
}

