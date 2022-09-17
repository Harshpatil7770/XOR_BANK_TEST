package com.xoriant.bank.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.AccountType;

@Repository
public interface AccountTypeRepo extends JpaRepository<AccountType, Long> {

	String findByAccountType(String accountType);

	@Query(value = "select account_type from account_type", nativeQuery = true)
	List<String> findByAccountType();

	@Query(value = "select account_type_id from account_type where account_type=?", nativeQuery = true)
	int findAccountTypeId(String accountType);

}
