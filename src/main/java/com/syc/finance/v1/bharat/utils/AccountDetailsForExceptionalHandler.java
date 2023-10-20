package com.syc.finance.v1.bharat.utils;

import lombok.*;

@Getter
@AllArgsConstructor
public enum AccountDetailsForExceptionalHandler {

        PENDING("Pending"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        COMPLETED("Completed");

        private final String label;

        public String getLabel() {
            return label;
        }
}

