package com.xoriant.bank.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.xoriant.bank.model.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

}
