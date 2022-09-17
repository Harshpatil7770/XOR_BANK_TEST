package com.xoriant.bank.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xoriant.bank.model.Branch;

public interface BranchRepo extends JpaRepository<Branch, Long>{

	Optional<Branch> findByBranchName(String upperCase);

}
