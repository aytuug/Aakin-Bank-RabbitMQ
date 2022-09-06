package com.aakin.AakinBank.model;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import java.util.SimpleTimeZone;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Customer {
    @Id
    private String id;
    private String name;
    private Integer dateOfBirth;
    private City city;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
}
