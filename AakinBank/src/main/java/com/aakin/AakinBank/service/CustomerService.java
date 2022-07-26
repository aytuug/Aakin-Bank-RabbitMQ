package com.aakin.AakinBank.service;

import com.aakin.AakinBank.dto.CreateCustomerRequest;
import com.aakin.AakinBank.dto.CustomerDto;
import com.aakin.AakinBank.dto.CustomerDtoConverter;
import com.aakin.AakinBank.dto.UpdateCustomerRequest;
import com.aakin.AakinBank.model.City;
import com.aakin.AakinBank.model.Customer;
import com.aakin.AakinBank.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {


    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter customerDtoConverter;

    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter customerDtoConverter) {
        this.customerRepository = customerRepository;
        this.customerDtoConverter = customerDtoConverter;
    }

    public CustomerDto createCustomer(CreateCustomerRequest customerRequest){

        Customer customer = new Customer();
        customer.setId(customerRequest.getId());
        //customer.setAddress(customerRequest.getAddress());
        customer.setName(customerRequest.getName());
        customer.setDateOfBirth(customerRequest.getDateOfBirth());
        customer.setCity(City.valueOf(customerRequest.getCity().name()));

        customerRepository.save(customer);



        return customerDtoConverter.convert(customer);
    }

    public List<CustomerDto> getAllCustomers(){
        List<Customer> customerList = customerRepository.findAll();
        List<CustomerDto> customerDtoList = new ArrayList<>();

        for (Customer customer : customerList){
            customerDtoList.add(customerDtoConverter.convert(customer));
        }

        return customerDtoList;
    }


    public CustomerDto getCustomerById(String id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        return customerOptional.map(customerDtoConverter::convert).orElse(new CustomerDto());
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }

    public CustomerDto updateCustomer(String id, UpdateCustomerRequest updateCustomerRequest) {
        Optional<Customer> customerOptional = customerRepository.findById(id);

        customerOptional.ifPresent(customer -> {
            customer.setName(updateCustomerRequest.getName());
            //customer.setAddress(updateCustomerRequest.getAddress());
            customer.setCity(City.valueOf(updateCustomerRequest.getCity().name()));
            customer.setDateOfBirth(updateCustomerRequest.getDateOfBirth());

            customerRepository.save(customer);

        });
        return customerOptional.map(customerDtoConverter::convert).orElse(new CustomerDto());
    }

    protected Customer getCustomerByIdAccount(String id){
        return customerRepository.findById(id).orElse(new Customer());
    }

}
