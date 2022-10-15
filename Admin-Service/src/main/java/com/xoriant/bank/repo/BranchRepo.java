package com.xoriant.bank.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xoriant.bank.model.Branch;

public interface BranchRepo extends JpaRepository<Branch, Long> {

//	Optional<Branch> findByBranchName(String upperCase);

	@Query(value = "select b.branch_id,b.branch_name,b.ifsc_code,b.address_id,city_name,house_name,house_number, street_name from branch_details\r\n"
			+ "b inner join common_address c on b.address_id=c.address_id;", nativeQuery = true)
	List<Branch> findAllBranchesWithAddressDetails();

	Optional<Branch> findByBranchName(String upperCase);
}
