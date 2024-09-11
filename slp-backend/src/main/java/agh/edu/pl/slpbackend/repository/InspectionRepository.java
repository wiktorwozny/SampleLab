package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
}
