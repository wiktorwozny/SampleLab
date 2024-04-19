package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends JpaRepository<Code, String> {
}
