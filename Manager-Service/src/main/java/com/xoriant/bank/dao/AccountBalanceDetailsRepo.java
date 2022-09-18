package com.xoriant.bank.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.AccountBalanceDetails;
import com.xoriant.bank.model.Customer;

@Repository
public interface AccountBalanceDetailsRepo extends JpaRepository<AccountBalanceDetails, Long> {

	Optional<Customer> findByAccountNumber(long accountNumber);

}
