package com.xoriant.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	@Query(value = "select * from customer where id=?", nativeQuery = true)
	public Customer findCustomerAccountById(long customerId);
}
