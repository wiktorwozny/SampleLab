package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportDataRepository extends JpaRepository<ReportData, Long> {
}
