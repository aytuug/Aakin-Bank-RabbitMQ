package com.aakin.AakinBank.repository;

import com.aakin.AakinBank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
