package com.aakin.AakinBank.repository;

import com.aakin.AakinBank.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, String> {

}
