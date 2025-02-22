package com.yagodaoud.comandae.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String cpfCnpj;
}
