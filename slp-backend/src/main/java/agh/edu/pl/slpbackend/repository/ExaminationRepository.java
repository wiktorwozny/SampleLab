package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
}
