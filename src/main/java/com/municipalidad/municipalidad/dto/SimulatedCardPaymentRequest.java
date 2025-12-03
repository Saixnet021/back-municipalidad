package com.municipalidad.municipalidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SimulatedCardPaymentRequest {
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String ownerName;
}
