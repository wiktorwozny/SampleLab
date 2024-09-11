package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExaminationRepository extends JpaRepository<Examination, Long> {
    @Query("SELECT e FROM Examination e WHERE e.sample.id = :sampleId")
    List<Examination> findBySampleId(@Param("sampleId") Long sampleId);
}
