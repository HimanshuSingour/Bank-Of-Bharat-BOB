package com.syc.finance.v1.bharat.repository;

import com.syc.finance.v1.bharat.entity.TransactionDetailsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionDetailsHistory, String> {

    @Query("SELECT u FROM TransactionDetailsHistory u WHERE u.accountNumber = :accountNumber")
    List<TransactionDetailsHistory> findAllByAccountNumberAndIfscCode(String accountNumber);

    long countByAccountNumberAndLocalDateTimeBetween(
            String accountNumber,
            LocalDateTime startTimestamp,
            LocalDateTime endTimestamp
    );
}
