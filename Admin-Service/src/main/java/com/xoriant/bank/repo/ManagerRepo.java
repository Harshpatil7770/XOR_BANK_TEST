package com.xoriant.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xoriant.bank.model.Manager;

public interface ManagerRepo extends JpaRepository<Manager, Long> {

//	Optional<Manager> findByUserName(String userName);
//
//	@Query(value = "select * from manager m inner join branch b on m.branch_id=b.branch_id inner join address a on m.address_address_id=a.address_id", nativeQuery = true)
//	List<Manager> findAllDetails();
//
//	Manager findByFirstNameAndLastName(String firstName, String lastName);
//
//	Manager findByBranchId(long branchId);

}
