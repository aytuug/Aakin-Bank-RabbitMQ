package com.aakin.AakinBank.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder


@Entity
public class Account {

    @Id
    private String id;



    private String customerId;
    private Double balance;
    private City city;
    private Currency currency;




}
