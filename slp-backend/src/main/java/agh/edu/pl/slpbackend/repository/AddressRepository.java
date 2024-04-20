package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
