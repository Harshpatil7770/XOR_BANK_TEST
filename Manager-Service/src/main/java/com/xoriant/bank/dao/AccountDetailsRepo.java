package com.xoriant.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.AccountDetails;
@Repository
public interface AccountDetailsRepo extends JpaRepository<AccountDetails, Long> {

	
}
