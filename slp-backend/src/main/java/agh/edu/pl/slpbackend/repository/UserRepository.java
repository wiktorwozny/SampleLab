package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
