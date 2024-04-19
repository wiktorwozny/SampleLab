package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Indication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndicationRepository extends JpaRepository<Indication, Long> {
}
