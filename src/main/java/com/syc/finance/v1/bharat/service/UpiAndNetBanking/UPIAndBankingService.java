package com.syc.finance.v1.bharat.service.UpiAndNetBanking;

import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;

public interface UPIAndBankingService {

    UPIResponse upiCreate(UPIRequest upiRequest);

}
