package agh.edu.pl.slpbackend.repository;

import agh.edu.pl.slpbackend.model.ProductGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductGroupRepository extends JpaRepository<ProductGroup, Long> {

    Optional<ProductGroup> findByName(String name);
}
