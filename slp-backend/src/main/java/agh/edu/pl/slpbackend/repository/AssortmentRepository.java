package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Assortment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssortmentRepository extends JpaRepository<Assortment, Long> {
}
