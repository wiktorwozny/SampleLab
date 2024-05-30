package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SampleRepository extends JpaRepository<Sample, Long> {
    Optional<Sample> findByReportDataId(Long id);
}
