package com.xoriant.bank.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xoriant.bank.model.Address;

public interface AddressRepo extends JpaRepository<Address, Long> {

}
