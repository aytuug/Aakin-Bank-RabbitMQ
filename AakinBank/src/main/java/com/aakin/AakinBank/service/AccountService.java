package com.aakin.AakinBank.service;

import com.aakin.AakinBank.dto.*;
import com.aakin.AakinBank.model.Account;
import com.aakin.AakinBank.model.Customer;
import com.aakin.AakinBank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter accountDtoConverter;
    private final DirectExchange exchange;

    private final AmqpTemplate rabbitTemplate;

    @Value("${sample.rabbitmq.routingKey}")
    String routingKey;

    @Value("${sample.rabbitmq.queue}")
    String queueName;


    public AccountDto createAccount(CreateAccountRequest createAccountRequest){
        Customer customer = customerService.getCustomerByIdAccount(createAccountRequest.getCustomerId());

        if (customer.getId().equals("") || customer.getId() == null){
            return AccountDto.builder().build();
        }

        Account account = Account.builder()
                .id(createAccountRequest.getId())
                .balance(createAccountRequest.getBalance())
                .currency(createAccountRequest.getCurrency())
                .customerId(createAccountRequest.getCustomerId())
                .city(createAccountRequest.getCity())
                .build();
        return accountDtoConverter.convert(accountRepository.save(account));
    }

    public AccountDto updateAccount(String id, UpdateAccountRequest request){
        Customer customer = customerService.getCustomerByIdAccount(request.getCustomerId());

        if (customer.getId().equals("") || customer.getId() == null){
            return AccountDto.builder().build();
        }

        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            account.setBalance(request.getBalance());
            account.setCity(request.getCity());
            account.setCurrency(request.getCurrency());
            account.setCustomerId(request.getCustomerId());
            accountRepository.save(account);
        });

        return accountOptional.map(accountDtoConverter::convert).orElse(new AccountDto());
    }

    public List<AccountDto> getAllAccounts(){
        List<Account> accountList = accountRepository.findAll();

        return accountList.stream().map(accountDtoConverter::convert).collect(Collectors.toList());
    }

    public AccountDto getAccountById(String id){
        return accountRepository.findById(id)
                .map(accountDtoConverter::convert)
            .orElse(new AccountDto());
    }

    public void deleteAccount(String id){
        accountRepository.deleteById(id);
    }

    public AccountDto withdrawMoney(String id, Double amount){
        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
            if (account.getBalance() > amount){
                account.setBalance(account.getBalance() - amount);
                accountRepository.save(account);
            }else{
                System.out.println("Insufficient funds -> accountId: " + id + " balance: " + account.getBalance()
                + "amount: " + amount);
            }
        });

        return accountOptional.map(accountDtoConverter::convert).orElse(new AccountDto());

    }
    public AccountDto addMoney(String id, Double amount){
        Optional<Account> accountOptional = accountRepository.findById(id);
        accountOptional.ifPresent(account -> {
                account.setBalance(account.getBalance() + amount);
                accountRepository.save(account);

        });

        return accountOptional.map(accountDtoConverter::convert).orElse(new AccountDto());

    }
    public void transferMoney(MoneyTransferRequest transferRequest) {
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, transferRequest);
    }

    @RabbitListener(queues = "${sample.rabbitmq.queue}")
    public void transferMoneyMessage(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account -> {
                    if (account.getBalance() > transferRequest.getAmount()) {
                        account.setBalance(account.getBalance() - transferRequest.getAmount());
                        accountRepository.save(account);
                        rabbitTemplate.convertAndSend(exchange.getName(), "secondRoute", transferRequest);
                    } else {
                        System.out.println("Insufficient funds -> accountId: " + transferRequest.getFromId() + " balance: " + account.getBalance() + " amount: " + transferRequest.getAmount());
                    }},
                () -> System.out.println("Account not found")
        );
    }
    @RabbitListener(queues = "secondStepQueue")
    public void updateReceiverAccount(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getToId());
        accountOptional.ifPresentOrElse(account -> {
                    account.setBalance(account.getBalance() + transferRequest.getAmount());
                    accountRepository.save(account);
                    rabbitTemplate.convertAndSend(exchange.getName(), "thirdRoute", transferRequest);
                },
                () -> {
                    System.out.println("Receiver Account not found");
                    Optional<Account> senderAccount = accountRepository.findById(transferRequest.getFromId());
                    senderAccount.ifPresent(sender -> {
                        System.out.println("Money charge back to sender");
                        sender.setBalance(sender.getBalance() + transferRequest.getAmount());
                        accountRepository.save(sender);
                    });

                }
        );
    }

    @RabbitListener(queues = "thirdStepQueue")
    public void finalizeTransfer(MoneyTransferRequest transferRequest) {
        Optional<Account> accountOptional = accountRepository.findById(transferRequest.getFromId());
        accountOptional.ifPresentOrElse(account ->
                System.out.println("sender(" + account.getId() + ") new acc balance: " + account.getBalance()),
                () -> System.out.println("Account not found.")
        );

        Optional<Account> accountToOptional = accountRepository.findById(transferRequest.getToId());
        accountToOptional.ifPresentOrElse(account ->
             System.out.println("Receiver(" + account.getId() + ") new acc balanc: " + account.getBalance()),
                () -> System.out.println("ACC not found!")
        );


    }
}
