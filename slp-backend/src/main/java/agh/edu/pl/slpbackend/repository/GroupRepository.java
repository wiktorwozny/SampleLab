package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
