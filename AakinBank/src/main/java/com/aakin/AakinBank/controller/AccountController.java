package com.aakin.AakinBank.controller;

import com.aakin.AakinBank.dto.AccountDto;
import com.aakin.AakinBank.dto.CreateAccountRequest;
import com.aakin.AakinBank.dto.MoneyTransferRequest;
import com.aakin.AakinBank.dto.UpdateAccountRequest;
import com.aakin.AakinBank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/account")
public class AccountController {

    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return ResponseEntity.ok(accountService.getAllAccounts())   ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String id){
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable String id, @RequestBody UpdateAccountRequest updateAccountRequest){
        return ResponseEntity.ok(accountService.updateAccount(id, updateAccountRequest));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAccount(@PathVariable String id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/withdraw/{id}/{amount}")
    public ResponseEntity<AccountDto> withdrawMoney(@PathVariable String id,
                                                    @PathVariable Double amount){
        return ResponseEntity.ok(accountService.withdrawMoney(id, amount));
    }

    @PutMapping("/add/{id}/{amount}")
    public ResponseEntity<AccountDto> addMoney(@PathVariable String id,
                                                    @PathVariable Double amount){
        return ResponseEntity.ok(accountService.addMoney(id, amount));
    }

    @PutMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest){
        accountService.transferMoney(transferRequest);
        return ResponseEntity.ok("isleminiz basariyla tamamlandi");
    }



}
