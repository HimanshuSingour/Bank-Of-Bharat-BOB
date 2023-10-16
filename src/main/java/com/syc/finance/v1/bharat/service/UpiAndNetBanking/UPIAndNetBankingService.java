package com.syc.finance.v1.bharat.service.UpiAndNetBanking;

import com.syc.finance.v1.bharat.dto.InternetBanking.GetNetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingResponse;
import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyRequest;
import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyResponse;
import com.syc.finance.v1.bharat.dto.UPI.GetUPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;

public interface UPIAndNetBankingService {

    UPIResponse upiCreate(UPIRequest upiRequest);
    NetBankingResponse createNetBanking(NetBankingRequest netBankingRequest);
    UPIResponse getYourUpiInInfo(GetUPIRequest upiRequest);
    NetBankingResponse getYourNetBankingInfo(GetNetBankingRequest netBankingRequest);

    TransferMoneyResponse moneyTransferAccountToAccount(TransferMoneyRequest transferMoneyRequest);


}
