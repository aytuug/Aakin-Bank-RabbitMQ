package com.aakin.AakinBank;

import com.aakin.AakinBank.model.Account;
import com.aakin.AakinBank.model.City;
import com.aakin.AakinBank.model.Currency;
import com.aakin.AakinBank.model.Customer;
import com.aakin.AakinBank.repository.AccountRepository;
import com.aakin.AakinBank.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class AakinBankApplication implements CommandLineRunner {

	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;

	public AakinBankApplication(AccountRepository accountRepository, CustomerRepository customerRepository) {
		this.accountRepository = accountRepository;
		this.customerRepository = customerRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(AakinBankApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Customer c1 = Customer.builder()
				.id("3242342")
				.name("aytug akin")
				.city(City.LONDON)
				.address("Gurbet")
				.dateOfBirth(1999)
				.build();
		Customer c2 = Customer.builder()
				.id("65423425")
				.name("test1 test1")
				.city(City.TEKIRDAG)
				.address("EV")
				.dateOfBirth(2011)
				.build();
		Customer c3 = Customer.builder()
				.id("5652351")
				.name("TEST2 TEST2")
				.city(City.ISTANBUL)
				.address("is")
				.dateOfBirth(2000)
				.build();

		customerRepository.saveAll(Arrays.asList(c1, c2, c3));
		Account a1 = Account.builder()
				.id("100")
				.customerId("3242342")
				.city(City.LONDON)
				.balance(999.00)
				.currency(Currency.EUR)
				.build();

		Account a2 = Account.builder()
				.id("100")
				.customerId("65423425")
				.city(City.TEKIRDAG)
				.balance(222.12)
				.build();

		Account a3 = Account.builder()
				.id("100")
				.customerId("5652351")
				.city(City.ISTANBUL)
				.balance(762.98)
				.build();
		accountRepository.saveAll(Arrays.asList(a1, a2, a3));

	}
}
