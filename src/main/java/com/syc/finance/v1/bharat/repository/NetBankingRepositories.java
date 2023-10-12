package com.syc.finance.v1.bharat.repository;

import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.entity.NetBankingInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NetBankingRepositories extends JpaRepository<NetBankingInformation , String> {

    @Query("SELECT u FROM NetBankingInformation u WHERE u.accountNumber = :accountNumber AND u.ifscCode = :ifscCode")
    NetBankingInformation findByAccountIdAndIfscCode(String accountNumber, String ifscCode);

}
