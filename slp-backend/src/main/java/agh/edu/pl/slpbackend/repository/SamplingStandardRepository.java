package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.SamplingStandard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SamplingStandardRepository extends JpaRepository<SamplingStandard, Long> {

    Optional<SamplingStandard> findByName(String name);
}
