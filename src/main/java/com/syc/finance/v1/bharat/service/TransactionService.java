package com.syc.finance.v1.bharat.service;

import com.syc.finance.v1.bharat.dto.Accounts.Transaction.TransactionResponse;
import com.syc.finance.v1.bharat.dto.Transaction.TransactionRequest;

import java.util.List;

public interface TransactionService {

    void saveTransaction(TransactionRequest transactionRequest);
    List<TransactionResponse> getAllTransaction(String accountNumber);
}
