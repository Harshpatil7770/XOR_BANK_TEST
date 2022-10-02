package com.xoriant.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.xoriant.bank.model.ManagerCredential;

@Repository
public interface CustomerLoginRepo extends JpaRepository<ManagerCredential, Long> {

}
