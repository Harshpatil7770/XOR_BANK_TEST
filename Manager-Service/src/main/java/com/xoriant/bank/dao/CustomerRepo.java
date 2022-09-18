package com.xoriant.bank.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

	@Query(value = "select * from customer where id=?", nativeQuery = true)
	public Customer findCustomerAccountById(long customerId);

	@Query(value = "select c.id,c.adhar_number,c.date_of_birth,c.first_name,c.last_name,c.gender,c.mobile_number,c.customer_details_address_id,a.address_line1,a.address_line2,a.address_line3,c.customer_details_account_id,ad.account_opening_date,ad.account_type,act.account_type,ad.account_details_info_account_number,abd.account_balance,abd.creadit_amount,abd.debit_amount from customer_details c inner join customer_address_details a on c.customer_details_address_id=a.address_id inner join account_details_info ad on c.customer_details_account_id=ad.account_number inner join account_type act on ad.account_type=act.account_type_id inner join account_balance_details abd on ad.account_details_info_account_number=abd.transactin_id;", nativeQuery = true)
	public List<Customer> findByInfoWall();

	public Optional<Customer> findByFirstNameAndLastName(String firstName, String lastName);

	/*
	 * @Query(value="",nativeQuery = true) public List<Customer>
	 * findByCustomerAccountNumberWithIncreasingOrder();
	 */
}
