package com.exampl.merchant.repository;
import com.exampl.merchant.dto.MerchantResponse;
import com.exampl.merchant.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    List<MerchantResponse>findAllBy();


}
