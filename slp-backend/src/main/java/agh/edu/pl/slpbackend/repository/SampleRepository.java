package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {
    Optional<Sample> findByReportDataId(Long id);
}
