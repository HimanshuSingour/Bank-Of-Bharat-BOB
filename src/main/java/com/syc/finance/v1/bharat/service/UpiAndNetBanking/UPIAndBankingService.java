package com.syc.finance.v1.bharat.service.UpiAndNetBanking;

import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingResponse;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;

public interface UPIAndBankingService {

    UPIResponse upiCreate(UPIRequest upiRequest);

    NetBankingResponse createNetBanking(NetBankingRequest netBankingRequest);


}
