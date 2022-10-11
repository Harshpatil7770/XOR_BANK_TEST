package com.xoriant.bank.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

	@Query(value = "select m.manager_id,m.first_name,m.last_name,m.user_type,m.address_id,ma.house_number,ma.house_name,\r\n"
			+ "ma.street_name,ma.city_name,m.branch_id,b.branch_name,b.ifsc_code,b.address_id,\r\n"
			+ "ba.house_number,ba.house_name,ba.street_name,ba.city_name,m.login_id,mc.user_name,mc.password from manager_basic_details m inner join\r\n"
			+ "common_address ma on m.address_id=ma.address_id inner join branch_details b on m.branch_id=b.branch_id \r\n"
			+ "inner join common_address ba on m.branch_id=ba.address_id inner join manager_creaditional mc on\r\n"
			+ "m.login_id=mc.id;", nativeQuery = true)
	List<Manager> findAllManagerWithTheirBranchDetails();
}
