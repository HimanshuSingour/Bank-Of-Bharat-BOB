package com.syc.finance.v1.bharat.repository;

import com.syc.finance.v1.bharat.entity.UpiInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UPIDetailsRepositories extends JpaRepository<UpiInformation, String> {

    @Query("SELECT u FROM UpiInformation u WHERE u.accountNumber = :accountNumber AND u.bankPassword = :bankPassword")
    UpiInformation findByAccountNumberAndPassword(@Param("accountNumber") String accountNumber, @Param("bankPassword") String bankPassword);

    @Query("SELECT u FROM UpiInformation u WHERE u.upiId = :upiId")
    UpiInformation findByUpiId(@Param("upiId") String upiId);



}
