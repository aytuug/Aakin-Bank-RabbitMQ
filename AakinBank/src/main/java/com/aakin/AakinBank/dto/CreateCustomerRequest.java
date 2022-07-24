package com.aakin.AakinBank.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest extends BaseCustomerRequest{
    private String id;

}
