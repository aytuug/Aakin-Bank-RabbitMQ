package com.aakin.AakinBank.repository;

import com.aakin.AakinBank.model.Account;
import com.aakin.AakinBank.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {
    //select * from where balance > $(balance)
    List<Account> findAllByBalanceGreaterThan(Double balance);

    //select * from account where currency=$(currency) and balance < 100
    List<Account> findAllByCurrencyIsAndAndBalanceLessThan(Currency currency, Double balance);
}
