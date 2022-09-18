package com.xoriant.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.LoginDetails;

@Repository
public interface LoginDetailRepo extends JpaRepository<LoginDetails, Long> {

	LoginDetails findByAccountNumber(long accountNumber);

}
