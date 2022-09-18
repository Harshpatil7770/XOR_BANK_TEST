package com.xoriant.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.xoriant.bank.model.LoginConstant;

@Repository
public interface CustomerLoginRepo extends JpaRepository<LoginConstant, Long> {

}
