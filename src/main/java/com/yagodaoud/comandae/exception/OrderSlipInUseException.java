package com.yagodaoud.comandae.exception;

public class OrderSlipInUseException extends RuntimeException {

    public OrderSlipInUseException(Integer orderSlipId) {
        super("Comanda Nª " + orderSlipId + " em uso.");
    }
}