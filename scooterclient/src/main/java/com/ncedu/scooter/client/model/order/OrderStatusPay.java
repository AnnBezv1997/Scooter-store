package com.ncedu.scooter.client.model.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public enum OrderStatusPay {
    YES,
    NOT
}
