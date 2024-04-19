package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
